package college.springcloud.common.interceptor.message.business;

import lombok.Data;

/**
 * 待发送信息
 *
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
    @Deprecated
    private String oldKey;

    /**
     * 就状态
     */
    private String oldStatus;

    /**
     * 新key
     */
    @Deprecated
    private String newKey;

    /**
     * 新状态
     */
    private String newStatus;

    /**
     * 额外参数
     */
    private Object object;

    /**
     * 参数类型
     */
    private Class<?> clazz;
}
