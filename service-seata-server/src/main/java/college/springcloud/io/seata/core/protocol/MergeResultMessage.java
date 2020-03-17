package college.springcloud.io.seata.core.protocol;

import lombok.Data;

/**
 * @author: xuxianbei
 * Date: 2020/3/12
 * Time: 14:44
 * Version:V1.0
 */
@Data
public class MergeResultMessage extends AbstractMessage implements MergeMessage {

    public AbstractResultMessage[] msgs;

    @Override
    public short getTypeCode() {
        return MessageType.TYPE_SEATA_MERGE_RESULT;
    }
}
