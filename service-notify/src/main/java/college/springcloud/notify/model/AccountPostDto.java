package college.springcloud.notify.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: xuxianbei
 * Date: 2019/12/23
 * Time: 11:14
 * Version:V1.0
 */
@Data
public class AccountPostDto implements Serializable {
    private static final long serialVersionUID = 1297176312147915460L;
    private int bodyId;
}
