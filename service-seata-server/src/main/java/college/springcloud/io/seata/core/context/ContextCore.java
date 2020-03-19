package college.springcloud.io.seata.core.context;

import java.util.Map;

/**
 * @author: xuxianbei
 * Date: 2020/3/12
 * Time: 17:43
 * Version:V1.0
 */
public interface ContextCore {

    /**
     * Put string.
     *
     * @param key   the key
     * @param value the value
     * @return the string
     */
    String put(String key, String value);

    /**
     * Get string.
     *
     * @param key the key
     * @return the string
     */
    String get(String key);

    /**
     * Remove string.
     *
     * @param key the key
     * @return the string
     */
    String remove(String key);

    /**
     * entries
     *
     * @return
     */
    Map<String, String> entries();
}
