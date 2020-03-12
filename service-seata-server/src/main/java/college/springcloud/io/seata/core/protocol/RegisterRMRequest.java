package college.springcloud.io.seata.core.protocol;

import lombok.Data;

/**
 * @author: xuxianbei
 * Date: 2020/3/11
 * Time: 11:42
 * Version:V1.0
 */
@Data
public class RegisterRMRequest extends AbstractIdentifyRequest {

    private String resourceIds;

    public RegisterRMRequest(String applicationId, String transactionServiceGroup) {
        super(applicationId, transactionServiceGroup);
    }

    @Override
    public short getTypeCode() {
        return MessageType.TYPE_REG_RM;
    }
}
