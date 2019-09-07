package college.springcloud.student.controller;

import college.springcloud.common.utils.Reflection;
import college.springcloud.common.utils.Result;
import college.springcloud.student.api.StudentApi;
import college.springcloud.student.po.Student;
import com.google.common.collect.Lists;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * User: EDZ
 * Date: 2019/8/27
 * Time: 14:16
 * Version:V1.0
 */

@RequestMapping("/student")
public class StudentController implements StudentApi {

    @GetMapping("/get")
    public Result get() {
        return Result.success(new Student());
    }

    @Override
    public Result insert(String param) {
        return Result.success(param);
    }

    private static void timeTest(){
        //时间戳
        Instant instant =  Instant.now();

        //格式化
        DateTimeFormatter dateTimeFormatter =  DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        //系统时间转换日本时间
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.of("Asia/Tokyo"));
        System.out.println(localDateTime.format(dateTimeFormatter));

        Set<String> set = ZoneId.getAvailableZoneIds();
        System.out.println(set);

        //东京
        localDateTime = LocalDateTime.now(ZoneId.of("Asia/Tokyo"));

        System.out.println(localDateTime);
//        2019-09-07T10:45:17.853

        //东京
        localDateTime = LocalDateTime.now();
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Tokyo"));
        //仅仅代表和标准时间差9小时
        System.out.println(zonedDateTime);
//        2019-09-07T09:45:17.857+09:00[Asia/Tokyo]
    }

    public static void main(String[] args) {
        //怎么说呢，延后创建吧。属于设计范畴。我觉得可以大量使用
        //我觉得这个是好东西啊
        Supplier<Student> supplier = Student::new;
        supplier.get();
        List<Student> list = new ArrayList<>();
        Student student = supplier.get();
        student.setName("x");
        list.add(student);
        student = supplier.get();
        student.setName("y");
        list.add(student);
        list.forEach(student1 -> student1.run());
        //就是代码太长看不过来的
        testJava8Consumer(list, student);

//        Map<String, Student> map = new HashMap<>();
//        list.stream().collect(Collectors.toMap(Student::getName, student1 -> student1));
        list = null;
        Map<String, Student> countryNameMap = Optional.ofNullable(list).orElse(Lists.newArrayList()).
                stream().collect(Collectors.toMap(Student::getName, student1 -> student1));
        System.out.println(countryNameMap);

        timeTest();
    }

    private static void testJava8Consumer(List<Student> list, Student student) {
        //这个功能小优化吧
        list.forEach(Student::run);
        System.out.println("Supplier============");
        //下面的代码也是偏向设计的
        Consumer<Student> consumer = Student::run;
        Consumer<Student> consumer2 = Student::run2;
        //优先执行consumer2再执行consumer
        consumer2.andThen(consumer).accept(student);
        System.out.println("Consumer============");
        Function<Student, Student> studentFunction = student1 -> student1.fun(student1);
        Function<Student, Student> studentFunction2 = student1 -> student1.fun2(student1);
        System.out.println(studentFunction2.getClass());
        System.out.println(studentFunction.andThen(studentFunction2).apply(student));
        String str = Reflection.fnToFieldName(Student::getAge);
        System.out.println(str);
        System.out.println("Predicate============");
        Predicate<Student> predicate = student1 ->  {
            if (student1.getAge().equals(1)) {
                student1.setName("xxb");
            }
            return student1.getName().equals("xxb");
        };
        student.setAge(1);
        System.out.println(predicate.test(student));
    }
}
