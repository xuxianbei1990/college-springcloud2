package college.springcloud.student.controller;

import college.springcloud.student.po.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**  java8 特性
 *   Supplier,
 * User: xuxianbei
 * Date: 2019/10/8
 * Time: 9:54
 * Version:V1.0
 */
public class Java8Function<T> {

    public List<T> getStudents(Supplier<T> supplier, BiConsumer<T, String> biConsumer, String name) {
//        Supplier<Student> studentSupplier = Student::new;
//        BiConsumer<Student, String> biConsumer = Student::setName;
        T t = supplier.get();
        biConsumer.accept(t, name);
        List<T> listResult = queryList(t);
        return listResult;
    }

    public static void main(String[] args) {
        Student student = new Student();
        student.setKey(1L);
        Map<Student, String> map =new HashMap<>();
        map.put(student, "2");
        Student students = new Student();
        students.setKey(1L);
        System.out.println(map.get(students));
    }

    //模拟数据库查询
    private List<T> queryList(T student){
        List list = new ArrayList<>();
        list.add(student);
        return list;
    }
}
