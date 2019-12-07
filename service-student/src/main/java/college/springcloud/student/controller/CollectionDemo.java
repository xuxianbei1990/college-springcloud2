package college.springcloud.student.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: xuxianbei
 * Date: 2019/12/6
 * Time: 14:34
 * Version:V1.0
 */
public class CollectionDemo {

    public static void main(String[] args) {
        //集合合并
        List<List<Long>> lists = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            List<Long> sonList = new ArrayList<>();
            sonList.add(Long.valueOf(i));
            sonList.add(Long.valueOf(i * 2));
            sonList.add(Long.valueOf(i * 3));
            lists.add(sonList);
        }
        System.out.println(lists.stream().flatMap(t -> t.stream()).collect(Collectors.toList()));
    }
}
