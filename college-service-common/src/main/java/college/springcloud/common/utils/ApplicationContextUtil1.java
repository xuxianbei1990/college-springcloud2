package college.springcloud.common.utils;

import college.springcloud.log.plug.hot.ClassHotStart;

import java.net.URL;

/**
 * @author: xuxianbei
 * Date: 2021/2/24
 * Time: 10:17
 * Version:V1.0
 */
public class ApplicationContextUtil1 {

    private static final String HOT_START_IGNORE = String.valueOf(System.nanoTime());

    /**
     * 启动
     *
     * @param source         启动的类
     * @param openDoc        打开文档
     * @param docPackageName 文档扫描包名称
     * @param args           启动指定参数
     */
    public synchronized static void run(Class source, Boolean openDoc, String docPackageName, String... args) {
        if (isConsoleStart()) {
            boolean isNormalStartFlag = false;
            for (String str : args) {
                if (str.equals(HOT_START_IGNORE)) {
                    isNormalStartFlag = true;
                }
            }
            if (!isNormalStartFlag) {
                //加入热加载
//                ClassHotStart.start(source, copy(args, HOT_START_IGNORE));
                return;
            }

        }
    }

    /**
     * 是否始控制台的启动
     *
     * @return
     */
    public static boolean isConsoleStart(){
        URL resource = ClassLoader.getSystemResource("");
        return resource != null && ("file".equals(resource.getProtocol()));
    }
}
