package college.springcloud.io.seata.core.rpc.netty.v1;

import college.springcloud.io.seata.core.codec.Codec;
import college.springcloud.io.seata.core.codec.CodecFactory;
import college.springcloud.io.seata.core.compressor.Compressor;
import college.springcloud.io.seata.core.compressor.CompressorFactory;
import college.springcloud.io.seata.core.protocol.HeartbeatMessage;
import college.springcloud.io.seata.core.protocol.ProtocolConstants;
import college.springcloud.io.seata.core.protocol.RpcMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/** 定义一条消息怎么解析的。非常基础的类
 * @author: xuxianbei
 * Date: 2020/3/2
 * Time: 17:55
 * Version:V1.0
 */
@Slf4j
public class ProtocolV1Decoder extends LengthFieldBasedFrameDecoder {

    public ProtocolV1Decoder() {
        this(8 * 1024 * 1024);
    }

    public ProtocolV1Decoder(int maxFrameLength) {
        /*
        int maxFrameLength,
        int lengthFieldOffset,  magic code is 2B, and version is 1B, and then FullLength. so value is 3
        int lengthFieldLength,  FullLength is int(4B). so values is 4
        int lengthAdjustment,   FullLength include all data and read 7 bytes before, so the left length is (FullLength-7). so values is -7
        int initialBytesToStrip we will check magic code and version self, so do not strip any bytes. so values is 0
        简单的说定义了协议的规则
        */
        super(maxFrameLength, 3, 4, -7, 0);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        Object decoded = super.decode(ctx, in);
        if (decoded instanceof ByteBuf) {
            ByteBuf frame = (ByteBuf) decoded;
            try {
                return decodeFrame(frame);
            } catch (Exception e) {
                log.error("Decode frame error!", e);
                throw e;
            } finally {
                frame.release();
            }
        }
        return decoded;
    }

    public Object decodeFrame(ByteBuf frame) {
        byte b0 = frame.readByte();
        byte b1 = frame.readByte();
        if (ProtocolConstants.MAGIC_CODE_BYTES[0] != b0
                || ProtocolConstants.MAGIC_CODE_BYTES[1] != b1) {
            throw new IllegalArgumentException("Unknown magic code: " + b0 + ", " + b1);
        }

        byte version = frame.readByte();
        // TODO  check version compatible here

        int fullLength = frame.readInt();
        short headLength = frame.readShort();
        byte messageType = frame.readByte();
        byte codecType = frame.readByte();
        byte compressorType = frame.readByte();
        int requestId = frame.readInt();

        RpcMessage rpcMessage = new RpcMessage();
        rpcMessage.setCodec(codecType);
        rpcMessage.setId(requestId);
        rpcMessage.setCompressor(compressorType);
        rpcMessage.setMessageType(messageType);

        // direct read head with zero-copy
        int headMapLength = headLength - ProtocolConstants.V1_HEAD_LENGTH;
        if (headMapLength > 0) {
            Map<String, String> map = HeadMapSerializer.getInstance().decode(frame, headMapLength);
            rpcMessage.getHeadMap().putAll(map);
        }

        // read body
        if (messageType == ProtocolConstants.MSGTYPE_HEARTBEAT_REQUEST) {
            rpcMessage.setBody(HeartbeatMessage.PING);
        } else if (messageType == ProtocolConstants.MSGTYPE_HEARTBEAT_RESPONSE) {
            rpcMessage.setBody(HeartbeatMessage.PONG);
        } else {
            int bodyLength = fullLength - headLength;
            if (bodyLength > 0) {
                byte[] bs = new byte[bodyLength];
                frame.readBytes(bs);
                Compressor compressor = CompressorFactory.getCompressor(compressorType);
                bs = compressor.decompress(bs);
                Codec codec = CodecFactory.getCodec(codecType);
                rpcMessage.setBody(codec.decode(bs));
            }
        }

        return rpcMessage;
    }
}
