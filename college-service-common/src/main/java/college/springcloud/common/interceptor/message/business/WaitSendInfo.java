package college.springcloud.common.interceptor.message.business;

import lombok.Data;

/** 待发送信息
 * @author: xuxianbei
 * Date: 2019/12/24
 * Time: 16:45
 * Version:V1.0
 */
@Data
public class WaitSendInfo {

    /**
     * 发送对象Id
     */
    private Long targetId;

    /**
     * 业务Id
     */
    private String businessId;

    /**
     * 旧key
     */
    private String oldKey;

    /**
     * 新key
     */
    private String newKey;

    /**
     * 额外参数
     */
    private Object object;

    /**
     * 参数类型
     */
    private Class<?> clazz;
}
