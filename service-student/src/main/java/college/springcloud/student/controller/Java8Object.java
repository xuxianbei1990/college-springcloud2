package college.springcloud.student.controller;

import java.util.HashMap;
import java.util.Map;

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
         * 12byte
         * 对象头
         * 实例数据
         * 数据对其
         */
//        ObjectStudent objectStudent = new ObjectStudent();
//        System.out.println(Integer.toHexString(objectStudent.hashCode()));
//        System.out.println(ClassLayout.parseInstance(objectStudent).toPrintable());

        Map<String, Integer> map = new HashMap<>();
        map.put("the", 1);
        map.put("xxb", 1);
        System.out.println(map);
    }
}
