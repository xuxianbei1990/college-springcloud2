package college.springcloud.log.handler;

import ch.qos.logback.core.rolling.RollingFileAppender;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.*;

/**
 * @author: xuxianbei
 * Date: 2021/3/2
 * Time: 15:40
 * Version:V1.0
 */
public class CustomRollingFileAppender extends RollingFileAppender {

    private static final ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() >> 1);
    private static Map<String, CustomRollingFileAppender> appenderMap = new ConcurrentHashMap();
    private static Set<File> LOCK_FILE_MAP = new CopyOnWriteArraySet<>();

    static {
        scheduled.scheduleWithFixedDelay(new TimerTask() {
            @Override
            public void run() {
                //保持文件删除还能继续
                CustomRollingFileAppender.appenderMap.keySet().forEach((x) -> {

                    if (!Files.exists(Paths.get(x)) && CustomRollingFileAppender.appenderMap.get(x).isStarted()) {
                        try {
                            CustomRollingFileAppender.appenderMap.get(x).openFile(x);
                        } catch (IOException var2) {
                        }
                    }
                });

                //应用结束后关闭此程序
                for (Iterator<File> iterator = LOCK_FILE_MAP.iterator(); iterator.hasNext(); ) {
                    File next = iterator.next();
                    if (!next.canWrite()) {
                        next.setWritable(true);
                    }
                    iterator.remove();
                }
            }
        }, 1L, 1L, TimeUnit.MINUTES);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            scheduled.shutdown();
            LOCK_FILE_MAP.forEach(x -> x.setWritable(true));
        }));
    }

    private int MAX_FILE_SIZE = 10;
    private String fileSuffix = "";

    public CustomRollingFileAppender() {

        scheduled.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    CustomRollingFileAppender.appenderMap.put(CustomRollingFileAppender.this.getFile(), CustomRollingFileAppender.this);
                } catch (Exception var2) {
                }

            }
        }, 30L, TimeUnit.SECONDS);
    }

    @Override
    public void start() {
        this.setMultipleFile();
        super.start();
    }

    private void setMultipleFile() {
        String[] fileName = getFile().split("/");
        File currentFile = new File(getFile().substring(0, getFile().indexOf(fileName[fileName.length - 1])),
                "." + fileName[fileName.length - 1]);
        if (!currentFile.exists()) {
            try {
                currentFile.createNewFile();
            } catch (IOException e) {
            }
        }
        if (currentFile.canWrite()) {
            currentFile.setWritable(false);
        } else {
            for (int i = 0; i < MAX_FILE_SIZE; i++) {
                File childFile = new File(currentFile.getParent(), "." + fileName[fileName.length - 1] + i);

                if (!childFile.exists()) {
                    try {
                        childFile.createNewFile();
                    } catch (IOException e) {
                    }
                }
                if (childFile.canWrite()) {
                    childFile.setWritable(false);
                    currentFile = childFile;
                    fileSuffix = i + "";
                    break;
                }
            }

        }
        LOCK_FILE_MAP.add(currentFile);
    }

    @Override
    public String getFile() {
        return super.getFile() + fileSuffix;
    }

}
