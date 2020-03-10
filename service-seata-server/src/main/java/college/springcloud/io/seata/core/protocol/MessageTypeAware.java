package college.springcloud.io.seata.core.protocol;

/**  为什么后缀是aware？
 * @author: xuxianbei
 * Date: 2020/3/10
 * Time: 9:37
 * Version:V1.0
 */
public interface MessageTypeAware {
    /**
     * return the message type
     * @return
     */
    short getTypeCode();
}
