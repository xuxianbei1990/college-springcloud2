package college.springcloud.student.controller;

import college.springcloud.student.dto.ObjectStudent;
import org.openjdk.jol.info.ClassLayout;

/**
 * @author: xuxianbei
 * Date: 2019/12/31
 * Time: 19:58
 * Version:V1.0
 */
public class Java8Object {
    public static void main(String[] args) {
        /**
         * 一个实例在64位操作系统下一定是8的倍数
         *
         * 对象头 12byte
         * 实例数据
         * 数据对其
         */
        ObjectStudent objectStudent = new ObjectStudent();
        System.out.println(ClassLayout.parseInstance(objectStudent).toPrintable());
        System.out.println(Integer.toHexString(objectStudent.hashCode()));
        System.out.println(ClassLayout.parseInstance(objectStudent).toPrintable());

        synchronized (objectStudent) {
            System.out.println(ClassLayout.parseInstance(objectStudent).toPrintable());
        }
    }

}
