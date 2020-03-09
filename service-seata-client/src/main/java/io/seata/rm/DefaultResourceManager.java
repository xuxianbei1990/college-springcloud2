package io.seata.rm;

import college.springcloud.io.seata.core.model.ResourceManager;

/**
 * @author: xuxianbei
 * Date: 2020/3/9
 * Time: 14:56
 * Version:V1.0
 */
public class DefaultResourceManager implements ResourceManager {

    public static DefaultResourceManager get() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static DefaultResourceManager INSTANCE = new DefaultResourceManager();
    }
}
