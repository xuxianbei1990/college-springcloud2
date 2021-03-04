package college.springcloud.log.plug.hot;

import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;

/**
 * @author: xuxianbei
 * Date: 2021/2/24
 * Time: 10:24
 * Version:V1.0
 */
public class ClassHotStart {

    private static String classPath;
    private static String staticPath;
    private static String targetStaticPath;
    private static String RESOURCES = "resources";


    static {
        URL resource = Thread.currentThread().
                getContextClassLoader().
                getResource("");
        //file:/E:/study/GitHub/college-springcloud2/college-service-common/out/production/classes/
        if (resource != null) {
            File currentFile = new File(resource.getPath());
            ///E:/study/GitHub/college-springcloud2/college-service-common/out/production/classes/
            classPath = currentFile.getPath();
            staticPath = getCurrentDicPath();
            targetStaticPath = currentFile.getParent() + File.separator + RESOURCES;
        }
    }

    private static String getCurrentDicPath() {
//        String className = findMainApplicationClass().getSimpleName();
//        if (className.isEmpty()) {
//            return "";
//        }
        return "";
    }


    private static Class<?> findMainApplicationClass() {
        try {
            StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();
            for (StackTraceElement stackTraceElement : stackTrace) {
                if ("main".equals(stackTraceElement.getMethodName())) {
                    return Class.forName(stackTraceElement.getClassName());
                }
            }
        } catch (ClassNotFoundException ex) {
        }
        return null;
    }

    public static void start(Class classz, String... args) {
        new ClassFileChange(
                arg -> {
                    try {
                        String filePath = (String) arg;
                        //更新静态文件和类
                        if (filePath.startsWith(classPath)) {
                            String canonicalName = filePath.substring(classPath.length() + 1).replaceAll("//|/|\\\\", ".");
                            Class<?> aClass = ClassHotLoader.getInstance().loadClass(canonicalName.substring(0, canonicalName.lastIndexOf(".")));
                            if (aClass != null && isSpringAnnotation(aClass.getAnnotations()) && ClassHotLoader.isOk()) {}
                            synchronized (ClassHotStart.class) {
                                //启动类
                                ClassHotLoader.getInstance().start(classz, args);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
        ).start();
    }

    /**
     * 判断是否是spring的注解,用来判断是否需要重启服务
     *
     * @param annotations
     */
    private static boolean isSpringAnnotation(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (!annotation.annotationType().getCanonicalName().startsWith("org.springframework")) {
                continue;
            }

            if (annotation.annotationType().getDeclaredAnnotation(Component.class) != null) {
                return true;

            } else {


                return isSpringAnnotation(annotation.annotationType().getDeclaredAnnotations());
            }

        }
        return false;
    }

    public static void main(String[] args) {
        String className = findMainApplicationClass().getSimpleName();
        System.out.println(className.split("\\$")[0].replaceAll("\\.",
                File.separator.equalsIgnoreCase("\\") ? "\\\\" : File.separator) + ".java");
    }
}
