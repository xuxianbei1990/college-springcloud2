package college.springcloud.io.seata.core.rpc.netty.v1;

import college.springcloud.io.seata.core.codec.Codec;
import college.springcloud.io.seata.core.codec.CodecFactory;
import college.springcloud.io.seata.core.compressor.Compressor;
import college.springcloud.io.seata.core.compressor.CompressorFactory;
import college.springcloud.io.seata.core.protocol.ProtocolConstants;
import college.springcloud.io.seata.core.protocol.RpcMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 编码
 *
 * @author: xuxianbei
 * Date: 2020/3/2
 * Time: 20:35
 * Version:V1.0
 */
@Slf4j
public class ProtocolV1Encoder extends MessageToByteEncoder {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        try {
            if (msg instanceof RpcMessage) {
                RpcMessage rpcMessage = (RpcMessage) msg;
                int fullLength = ProtocolConstants.V1_HEAD_LENGTH;
                int headLength = ProtocolConstants.V1_HEAD_LENGTH;
                byte messageType = rpcMessage.getMessageType();
                out.writeBytes(ProtocolConstants.MAGIC_CODE_BYTES);
                out.writeByte(ProtocolConstants.VERSION);
                out.writerIndex(out.writerIndex() + 6);
                out.writeByte(messageType);
                out.writeByte(rpcMessage.getCodec());
                out.writeByte(rpcMessage.getCompressor());
                out.writeInt(rpcMessage.getId());
                // direct write head with zero-copy
                Map<String, String> headMap = rpcMessage.getHeadMap();
                if (headMap != null && !headMap.isEmpty()) {
                    int headMapBytesLength = HeadMapSerializer.getInstance().encode(headMap, out);
                    headLength += headMapBytesLength;
                    fullLength += headMapBytesLength;
                }

                byte[] bodyBytes = null;
                if (messageType != ProtocolConstants.MSGTYPE_HEARTBEAT_REQUEST
                        && messageType != ProtocolConstants.MSGTYPE_HEARTBEAT_RESPONSE) {
                    // heartbeat has no body
                    Codec codec = CodecFactory.getCodec(rpcMessage.getCodec());
                    bodyBytes = codec.encode(rpcMessage.getBody());
                    Compressor compressor = CompressorFactory.getCompressor(rpcMessage.getCompressor());
                    bodyBytes = compressor.compress(bodyBytes);
                    fullLength += bodyBytes.length;
                }

                if (bodyBytes != null) {
                    out.writeBytes(bodyBytes);
                }

                // fix fullLength and headLength
                int writeIndex = out.writerIndex();
                // skip magic code(2B) + version(1B)
                out.writerIndex(writeIndex - fullLength + 3);
                out.writeInt(fullLength);
                out.writeShort(headLength);
                out.writerIndex(writeIndex);
            } else {
                throw new UnsupportedOperationException("Not support this class:" + msg.getClass());
            }
        } catch (Throwable e) {
            log.error("Encode request error!", e);
        }
    }
}
