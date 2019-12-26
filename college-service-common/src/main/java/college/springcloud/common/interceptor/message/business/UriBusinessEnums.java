package college.springcloud.common.interceptor.message.business;

import java.util.Objects;

/**
 * uri业务枚举
 *
 * @author: xuxianbei
 * Date: 2019/12/23
 * Time: 17:07
 * Version:V1.0
 */
public enum UriBusinessEnums {
    DEFAULT("", Object.class),
    REGISTER("/notify/account/post/register", MobileRegister.class);
    private String uri;
    private Class<?> clazz;

    UriBusinessEnums(String uri, Class<?> clazz) {
        this.uri = uri;
        this.clazz = clazz;
    }

    public String getUri() {
        return uri;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public static UriBusinessEnums getByUri(String uri) {
        for (UriBusinessEnums value : values()) {
            if (Objects.equals(value.getUri(), uri)) {
                return value;
            }
        }
        return DEFAULT;
    }
}
