package college.springcloud.aliyun.utils;

import java.io.File;

/**
 * @author: xuxianbei
 * Date: 2020/5/21
 * Time: 11:25
 * Version:V1.0
 */
public class FileDeleteUtils {

    public static void deleteDir(String dirPath) {
        File file = new File(dirPath);// 读取
        if (file.isFile()) { // 判断是否是文件夹
            file.delete();// 删除
        } else {
            File[] files = file.listFiles(); // 获取文件
            for (File file1 : files) {
                if (file1.isFile() && file.canExecute()) {
                    file.delete();// 删除
                }
            }
        }
    }
}
