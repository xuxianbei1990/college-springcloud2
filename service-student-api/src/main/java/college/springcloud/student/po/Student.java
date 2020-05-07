package college.springcloud.student.po;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * User: EDZ
 * Date: 2019/8/27
 * Time: 14:21
 * Version:V1.0
 */
@Data
public class Student implements Serializable {
    private static final long serialVersionUID = -2874944596724304275L;
    private String name;
    private Integer age;
    private Long key;
    private Integer money;
    private byte[] bytes = new byte[1024];

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(key, student.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public void run(){
        this.name = "run";
        System.out.println(name);
    }

    public void run2(){
        this.name = "run2";
        System.out.println(name);
    }

    public Student fun(Student student) {
        student.setName("fun");
        System.out.println("fun");
        return student;
    }

    public Student fun2(Student student) {
        student.setName("fun2");
        System.out.println("fun2");
        return student;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
