package college.springcloud.student.controller;

import college.springcloud.common.utils.ExcelUtils;
import college.springcloud.common.utils.Reflection;
import college.springcloud.common.utils.Result;
import college.springcloud.common.utils.ResultUtils;
import college.springcloud.student.annotation.TeacherRole;
import college.springcloud.student.api.StudentApi;
import college.springcloud.student.dto.ExportMultyMergeVo;
import college.springcloud.student.dto.ExportVo;
import college.springcloud.student.dto.StudentDto;
import college.springcloud.student.po.Student;
import college.springcloud.student.po.StudentCopy;
import college.springcloud.student.po.StudentSerialize;
import college.springcloud.student.service.AsyncThreadTest;
import college.springcloud.student.service.StudentServiceImpl;
import feign.Request;
import io.swagger.annotations.ApiOperation;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * User: EDZ
 * Date: 2019/8/27
 * Time: 14:16
 * Version:V1.0
 */

@RestController
@RequestMapping("/student")
public class StudentController<T> implements StudentApi {

    @TeacherRole("鬼神降世")
    @TeacherRole("百八式")
    @TeacherRole("必杀")
    private String role;

    @Autowired
    private StudentServiceImpl studentService;

    @Autowired
    private AsyncThreadTest asyncThreadTest;

    //    @Resource
    private AsyncTaskExecutor asyncTaskExecutor;

    @Override
    @GetMapping("/get")
    public Result get() {
        return Result.success(new Student());
    }

    @Override
    public Result insert(String param, Request.Options options) {
        return Result.success(param);
    }

    @Override
    public Result getStudent(Student student) {
        return Result.success(student);
    }

    @Override
    public Result getStudentQueryMap(Student student) {
        return Result.success(student);
    }

    @Test
    public void timeTest() {
        //时间戳
        Instant instant = Instant.now();

        //格式化
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
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

        //上海时间
        localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(1567440000000L), ZoneId.of("Asia/Shanghai"));
        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println(dateTimeFormatter.format(localDateTime));
    }

    public static void main(String[] args) {
        //怎么说呢，延后创建吧。属于设计范畴。我觉得可以大量使用
        //我觉得这个是好东西啊
//        Supplier<Student> supplier = Student::new;
//        supplier.get();
//        List<Student> list = new ArrayList<>();
//        Student student = supplier.get();
//        student.setName("x");
//        list.add(student);
//        student = supplier.get();
//        student.setName("y");
//        list.add(student);
//        list.forEach(student1 -> student1.run());
//        //就是代码太长看不过来的
//        testJava8Consumer(list, student);
//
////        Map<String, Student> map = new HashMap<>();
//        list = null;
//        Map<String, Student> countryNameMap = Optional.ofNullable(list).orElse(Lists.newArrayList()).
//                stream().collect(Collectors.toMap(Student::getName, student1 -> student1, (key1, key2) -> key2));
//        System.out.println(countryNameMap);
        BeanCopy();
    }

    private static void BeanCopy() {
        Student student = new Student();
        student.setAge(1);
        student.setKey(2L);
        student.setName("lu 卡 si");
        student.setMoney(10);
        StudentCopy copy = new StudentCopy();
        ResultUtils.copyProperties(student, copy);
        ResultUtils.copyProperties(student, copy);
        System.out.println(copy);
    }


    @Test
    public void testGetStudents() {
        List<Student> students = getStudents("xx");
        System.out.println(Arrays.toString(students.toArray()));
        Java8Function<Student> java8Function = new Java8Function<>();
        students = java8Function.getStudents(Student::new, Student::setName, "xx");
        System.out.println(Arrays.toString(students.toArray()));
    }

    private List<Student> getStudents(String name) {
        //一般查询都会写这样的代码
        Student student = new Student();
        student.setName(name);
        List<Student> listResult = queryList(student);
        return listResult;
    }


    //模拟数据库查询
    private List<Student> queryList(Student student) {
        List list = new ArrayList<>();
        list.add(student);
        return list;
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
        System.out.println("studentFunction===" + studentFunction.apply(student));
        System.out.println(studentFunction2.getClass());
        //先执行studentFunction 再执行了 studentFunction2
        System.out.println(studentFunction.andThen(studentFunction2).apply(student));
        String str = Reflection.fnToFieldName(Student::getAge);
        System.out.println(str);
        System.out.println("Function============");
        Predicate<Student> predicate = student1 -> {
            if (student1.getAge().equals(1)) {
                student1.setName("xxb");
            }
            return student1.getName().equals("xxb");
        };
        student.setAge(1);
        System.out.println(predicate.test(student));
    }

    @ApiOperation("导出")
    @GetMapping("/export/big")
    public void export(@Validated StudentDto studentDto, HttpServletResponse response) {
        ExcelUtils.exportExcelByEasyPoi("采购单", studentDto, ExportVo.class, studentService, response);
    }

    @ApiOperation("导出")
    @GetMapping("/export/cellMerge")
    public void exportCellMerge(@Validated StudentDto studentDto, HttpServletResponse response) {

        List<ExportMultyMergeVo> exportVos = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            ExportMultyMergeVo exportVo = factoryNewExcport();
            exportVos.add(exportVo);
        }

        ExcelUtils.exportExcel(exportVos, ExportMultyMergeVo.class, "采购单", response);
    }

    private ExportMultyMergeVo factoryNewExcport() {
        String[] firsts = {"女装", "水装", "光装"};
        String[] seconds = {"上身", "连身", "下身", "变身", "超神"};
        String[] thirds = {"背心", "吊带", "风衣", "连体裤", "连衣裙", "半群", "背带裙", "超群"};
        String[] codes = {"代号", "型号", "星号"};
        BigDecimal[] prics = {BigDecimal.valueOf(9.9), BigDecimal.valueOf(99.9), BigDecimal.valueOf(999.9),
                BigDecimal.valueOf(11.9), BigDecimal.valueOf(22.9), BigDecimal.valueOf(33.9)};
        ExportMultyMergeVo exportVo = new ExportMultyMergeVo();
        exportVo.setFirstClass(random(firsts));
        exportVo.setSecondClass(random(seconds));
        exportVo.setThirdClass(random(thirds));
        exportVo.setCode(random(codes));
        exportVo.setPrice(random(prics));
        return exportVo;
    }

    private <T> T random(T[] strings) {
        return strings[(int) (Math.random() * (strings.length - 1))];
    }

    @PostMapping("/serialize")
    public StudentSerialize serialize(@RequestBody StudentSerialize studentSerialize) {
        studentSerialize.setCashJs(10);
        studentSerialize.setModifyDate(new Date());
        return studentSerialize;
    }


    @ApiOperation("导入模板下载")
    @GetMapping("/download/template")
    public void downloadTemplate(HttpServletResponse response) {
        studentService.downloadTemplate(response);
    }

    @ApiOperation("导入")
    @GetMapping("/import")
    public List<ExportVo> importData(@RequestBody MultipartFile file) {
        return studentService.importData(file);
    }


    @GetMapping("/async")
    public String async() {
        asyncThreadTest.asycTest("xxy", "a yi a yi e king", 1);
        return "";
    }

    /**
     * 异步再同步数据
     * 说明一个问题，在线程处理时候异步时候 future 是先被返回的。所以future无论放在哪里都是没有问题。
     *
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @GetMapping("/async/future")
    public String asyncFuture() throws ExecutionException, InterruptedException {
        Future<String> future = asyncThreadTest.asycTestFuture("xxy", "a yi a yi e king", 1);
        asyncTaskExecutor.execute(() -> {
            throw new RuntimeException("xxb");
        });
        System.out.println("ba la ba 一顿操作");
        return future.get();
    }

    private List<Student> list = new ArrayList<>();

    @GetMapping("/oom")
    public String outOfMemory(Integer count) {
        for (int i = 0; i < count; i++) {
            Student student = new Student();
            student.setName("xxb");
            student.setKey(1L);
            student.setAge(1);
            student.setMoney(4);
            list.add(student);
        }
        return "success";
    }

}
