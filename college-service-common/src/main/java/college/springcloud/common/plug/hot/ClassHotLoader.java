package college.springcloud.common.plug.hot;

import org.springframework.context.ConfigurableApplicationContext;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author: xuxianbei
 * Date: 2021/2/24
 * Time: 10:12
 * Version:V1.0
 */
public class ClassHotLoader {
    private static volatile ClassHotLoader instance = null;
    private static volatile boolean START_FLAG = true;
    private ConfigurableApplicationContext rootContexts;
    private CustomClassLoader classLoader;
    private String classPath;

    public static ClassHotLoader getInstance() {
        if (instance == null) {
            synchronized (ClassHotLoader.class) {
                if (instance == null) {
                    instance = new ClassHotLoader();
                    URL resource = Thread.currentThread().
                            getContextClassLoader().
                            getResource("");
                    if (resource != null) {
                        instance.classPath = resource.getPath();
                    }
                }
            }
        }
        return instance;
    }

    /**
     * 自定义类加载引擎
     *
     * @param name
     * @return
     * @throws ClassNotFoundException
     */
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        synchronized (this) {
            classLoader = new CustomClassLoader(this.classPath);
            Class<?> findClass = classLoader.findClass(name);
            if (findClass != null) {
                return findClass;
            }
        }
        return classLoader.loadClass(name);
    }

    public static boolean isOk() {
        return START_FLAG;
    }

    private void stop() {
        System.gc();
        System.runFinalization();
        if (rootContexts != null) {
            rootContexts.close();
        }


    }

    public void start(Class classz, String... args) {
        Thread thread = new Thread(() -> {

            try {
                START_FLAG = false;
                stop();
                Class<?> startClass = loadClass(classz.getCanonicalName());
                Thread.currentThread().setContextClassLoader(classLoader);
                Method mainMethod = startClass.getDeclaredMethod("main", String[].class);
                mainMethod.invoke(null, (Object) args);
                START_FLAG = true;

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public static class CustomClassLoader extends ClassLoader {
        private String classPath;

        public CustomClassLoader(String classPath) {
            super(ClassLoader.getSystemClassLoader());
            this.classPath = classPath;
        }

        /**
         * 重写findClass
         */
        @Override
        public Class<?> findClass(String name) throws ClassNotFoundException {
            byte[] classByte = readClassFile(name);

            if (classByte == null || classByte.length == 0 ||
                    ClassHotLoader.class.getCanonicalName().equalsIgnoreCase(name)) {
                throw new ClassNotFoundException("ClassNotFound : " + name);
            }

            Class<?> newClass = this.defineClass(name, classByte, 0, classByte.length);
            if (newClass.getPackage() == null) {
                definePackage(name.substring(0, name.lastIndexOf('.'))
                        , null, null, null, null, null, null, null);

            }
            return newClass;
        }

        /**
         * 读取类文件
         *
         * @param name
         * @return
         * @throws ClassNotFoundException
         */
        private byte[] readClassFile(String name) throws ClassNotFoundException {
            String fileName = name.replace(".", "/") + ".class";
            File classFile = new File(this.classPath, fileName);
            if (!classFile.exists() || classFile.isDirectory()) {
                throw new ClassNotFoundException("ClassNotFound : " + name);
            }
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(classFile);
                int available = fis.available();
                int bufferSize = Math.max(Math.min(1024, available), 256);
                ByteBuffer buf = ByteBuffer.allocate(bufferSize);
                byte[] bytes = null;
                FileChannel channel = fis.getChannel();

                while (channel.read(buf) > 0) {
                    buf.flip();
                    bytes = translateArray(bytes, buf);
                    buf.clear();
                }

                return bytes;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                closeIOQuiet(fis);
            }
            return null;
        }

        /**
         * 关闭io流
         *
         * @param closeable
         */
        public static void closeIOQuiet(Closeable closeable) {

            try {
                if (closeable != null) {
                    closeable.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * 数组转换
         *
         * @param bytes
         * @param buf
         * @return
         */
        public byte[] translateArray(byte[] bytes, ByteBuffer buf) {
            if (bytes == null) {
                bytes = new byte[0];
            }
            byte[] _array;
            if (buf.hasArray()) {
                _array = new byte[buf.limit()];
                System.arraycopy(buf.array(), 0, _array, 0, _array.length);
            } else {
                _array = new byte[0];
            }
            byte[] _implyArray = new byte[bytes.length + _array.length];
            System.arraycopy(bytes, 0, _implyArray, 0, bytes.length);
            System.arraycopy(_array, 0, _implyArray, bytes.length, _array.length);
            bytes = _implyArray;
            return bytes;
        }

    }


    public void addApplicationContext(ConfigurableApplicationContext context) {
        if (context == null) {
            return;
        }
        synchronized (this) {
            rootContexts = context;
        }
    }
}
