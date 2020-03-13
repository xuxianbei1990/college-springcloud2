package college.springcloud.io.seata.core.protocol;

import lombok.Data;

/**
 * @author: xuxianbei
 * Date: 2020/3/10
 * Time: 9:59
 * Version:V1.0
 */
@Data
public abstract class AbstractResultMessage extends AbstractMessage {

    private ResultCode resultCode;

    private String msg;


}
