package college.springcloud.io.seata.core.protocol;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: xuxianbei
 * Date: 2020/3/2
 * Time: 15:52
 * Version:V1.0
 */
@Data
public class RpcMessage {

    private int id;
    private byte messageType;
    private byte codec;
    //压缩方式
    private byte compressor;
    private Map<String, String> headMap = new HashMap<>();
    private Object body;

    /**
     * Gets head.
     *
     * @param headKey the head key
     * @return the head
     */
    public String getHead(String headKey) {
        return headMap.get(headKey);
    }

    /**
     * Put head.
     *
     * @param headKey   the head key
     * @param headValue the head value
     */
    public void putHead(String headKey, String headValue) {
        headMap.put(headKey, headValue);
    }
}
