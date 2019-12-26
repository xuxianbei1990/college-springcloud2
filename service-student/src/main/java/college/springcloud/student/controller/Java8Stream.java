package college.springcloud.student.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: xuxianbei
 * Date: 2019/12/25
 * Time: 13:50
 * Version:V1.0
 */
public class Java8Stream {

    private static final Integer MAX_NUMBER = 3;

    /**
     * 计算切分次数
     */
    private static Integer countStep(Integer size) {
        return (size + MAX_NUMBER - 1) / MAX_NUMBER;
    }

    /**
     * list 指定次数 分割
     * @param list
     * @param cutting
     * @param <T>
     * @return
     */
    private static <T> List<List<T>> cuttingList(List<T> list, Integer cutting) {
        int limit = (list.size() + cutting - 1) / cutting;
        List<List<T>> splitList = Stream.iterate(0, n -> n + 1).limit(limit).parallel().
                map(a -> list.stream().skip(a * cutting).limit(cutting).parallel().collect(Collectors.toList())).collect(Collectors.toList());
        return splitList;
    }

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
        int limit = countStep(list.size());
        List<List<Integer>> mglist = new ArrayList<>();
        Stream.iterate(0, n -> n + 1).limit(limit).forEach(i -> {
            mglist.add(list.stream().skip(i * MAX_NUMBER).limit(MAX_NUMBER).collect(Collectors.toList()));
        });

        System.out.println(mglist);
        List<List<Integer>> splitList = Stream.iterate(0, n -> n + 1).limit(limit).parallel().map(a -> list.stream().skip(a * MAX_NUMBER).limit(MAX_NUMBER).parallel().collect(Collectors.toList())).collect(Collectors.toList());

        System.out.println(splitList);

        System.out.println(cuttingList(list, 3));
    }
}
