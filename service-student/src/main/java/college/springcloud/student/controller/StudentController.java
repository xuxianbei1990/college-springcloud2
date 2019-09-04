package college.springcloud.student.controller;

import college.springcloud.common.utils.Reflection;
import college.springcloud.common.utils.Result;
import college.springcloud.student.api.StudentApi;
import college.springcloud.student.po.Student;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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
        //这个功能小优化吧
        list.forEach(Student::run);
        System.out.println("============");
        //下面的代码也是偏向设计的
        Consumer<Student> consumer = Student::run;
        Consumer<Student> consumer2 = Student::run2;
        consumer2.andThen(consumer).accept(student);
        System.out.println("============");
        Function<Student, Student> studentFunction = student1 -> student1.fun(student1);
        Function<Student, Student> studentFunction2 = student1 -> student1.fun2(student1);
        System.out.println(studentFunction2.getClass());
        System.out.println(studentFunction.andThen(studentFunction2).apply(student));

       String str =  Reflection.fnToFieldName(Student::getAge);
       System.out.println(str);
    }
}
