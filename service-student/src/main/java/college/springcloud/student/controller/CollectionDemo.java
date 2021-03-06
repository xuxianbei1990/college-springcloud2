package college.springcloud.student.controller;

import college.springcloud.student.dto.StudentDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * User: xuxianbei
 * Date: 2019/12/6
 * Time: 14:34
 * Version:V1.0
 */
public class CollectionDemo {

    //不支持多线程
    static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

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
        System.out.println(sonToSum(lists));
//        testDistinctList();

    }

    private static void testDistinctList() {
        //集合去重  按照指定属性
        List<StudentDto> studentDtos = new ArrayList<>();
        StudentDto studentDto = new StudentDto();
        studentDto.setName("xxb");
        studentDtos.add(studentDto);
        studentDto = new StudentDto();
        studentDto.setName("xxb1");
        studentDtos.add(studentDto);
        studentDto = new StudentDto();
        studentDto.setName("xxb2");
        studentDtos.add(studentDto);
        studentDto = new StudentDto();
        studentDto.setName("xxb3");
        studentDtos.add(studentDto);
        studentDto = new StudentDto();
        studentDto.setName("xxb");
        studentDtos.add(studentDto);
//        studentDtos = studentDtos.stream().filter(distinctByKey(t-> t.getName())).collect(Collectors.toList());
//        System.out.println(studentDtos);

        //去重 支持多线程
//        studentDtos = studentDtos.stream().collect(Collectors.toMap(t -> t.getName(), value -> value,
//                (existing, replacement) -> replacement)).entrySet().stream().map(t -> t.getValue()).collect(Collectors.toList());
        studentDtos = distinctList(studentDtos, t -> t.getName());
        System.out.println(studentDtos);
    }

    //集合合并
    public static <T> List<T> sonToSum(List<List<T>> lists) {
        return lists.stream().flatMap(t -> t.stream()).collect(Collectors.toList());
    }

    //去重
    static <T, K> List<T> distinctList(List<T> list, Function<? super T, ? extends K> keyMapper) {
        return list.stream().collect(Collectors.toMap(keyMapper, value -> value,
                (existing, replacement) -> replacement)).entrySet().stream().map(t -> t.getValue()).collect(Collectors.toList());
    }
    //集合拆分
    private static <T> List<List<T>> splitList(List<T> list, Integer send){
        int limit =(list.size() + send - 1) / send;;
        return Stream.iterate(0, n -> n + 1).limit(limit).parallel().map(a -> list.stream().skip(a * send)
                .limit(send).parallel().collect(Collectors.toList())).collect(Collectors.toList());
    }
}
