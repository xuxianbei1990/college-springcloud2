package college.springcloud.common.enums;

/**
 * 异常顶级接口
 *
 * @author: xuxianbei
 * Date: 2020/5/22
 * Time: 14:17
 * Version:V1.0
 * 1. 定义模块异常。大概1天。
 * 2. 配置模块异常，负责人。
 * 3. 钉钉调研，@
 */
public interface IExceptionCode {

    default String getModule() {
        return "";
    }


    String getCode();

    String getMsg();
}
