package college.springcloud.io.seata.core.protocol;

/**
 * @author: xuxianbei
 * Date: 2020/3/12
 * Time: 14:44
 * Version:V1.0
 */
public class MergeResultMessage extends AbstractMessage implements MergeMessage {
    @Override
    public short getTypeCode() {
        return MessageType.TYPE_SEATA_MERGE_RESULT;
    }
}
