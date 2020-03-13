package college.springcloud.io.seata.core.protocol;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: xuxianbei
 * Date: 2020/3/12
 * Time: 14:47
 * Version:V1.0
 */
public class MergedWarpMessage extends AbstractMessage implements Serializable, MergeMessage {


    public List<AbstractMessage> msgs = new ArrayList<>();

    public List<Integer> msgIds = new ArrayList<>();

    @Override
    public short getTypeCode() {
        return MessageType.TYPE_SEATA_MERGE;
    }
}
