package college.springcloud.student.controller;

import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import college.springcloud.common.utils.*;
import college.springcloud.student.controller.pdf.UploadDatasDto;
import college.springcloud.student.utils.HtmlPdfUtils;
import college.springcloud.student.utils.UploadDataEnum;
import college.springcloud.student.utils.WordToPdfUtil;
import college.springcloud.student.annotation.TeacherRole;
import college.springcloud.student.api.StudentApi;
import college.springcloud.student.controller.pdf.UploadDataDto;
import college.springcloud.student.dto.ExportCustomVo;
import college.springcloud.student.dto.ExportMultyMergeVo;
import college.springcloud.student.dto.ExportVo;
import college.springcloud.student.dto.StudentDto;
import college.springcloud.student.po.Student;
import college.springcloud.student.po.StudentCopy;
import college.springcloud.student.po.StudentSerialize;
import college.springcloud.student.service.AsyncThreadTest;
import college.springcloud.student.service.StudentServiceImpl;
import com.alibaba.fastjson.JSONObject;
import feign.Request;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.util.Strings;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private WordToPdfUtil wordToPdfUtil;

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

    @ApiOperation("导出 大数据")
    @GetMapping("/export/big")
    public void export(@Validated StudentDto studentDto, HttpServletResponse response) {
        ExcelUtils.exportExcelByEasyPoi("采购单", studentDto, ExportVo.class, studentService, response);
    }

    @ApiOperation("导出 合并单元格")
    @GetMapping("/export/cellMerge")
    public void exportCellMerge(@Validated StudentDto studentDto, HttpServletResponse response) {

        List<ExportMultyMergeVo> exportVos = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            ExportMultyMergeVo exportVo = factoryNewExcport();
            exportVos.add(exportVo);
        }
        exportVos.sort(Comparator.comparing(ExportMultyMergeVo::getCode));
        exportVos.sort(Comparator.comparing(ExportMultyMergeVo::getThirdClass));
        exportVos.sort(Comparator.comparing(ExportMultyMergeVo::getSecondClass));
        exportVos.sort(Comparator.comparing(ExportMultyMergeVo::getFirstClass));
        ExcelUtils.exportExcel(exportVos, ExportMultyMergeVo.class, "采购单", response);
    }


    @ApiOperation("导出定制")
    @GetMapping("/export/custom")
    public void exportCellMerge(HttpServletResponse response) {
        Date date = new Date();
        LocalDateTime localDateTime = LocalDateTime.now();
        List<ExportCustomVo> exportVos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ExportCustomVo exportVo = new ExportCustomVo();
            exportVo.setFirstClass("random(firsts)");
            exportVo.setSecondClass("random(seconds)");
            exportVo.setThirdClass("random(thirds)");
            exportVo.setCode("random(codes)");
            exportVo.setPrice(BigDecimal.ZERO);
            exportVo.setCreateTime(DateUtils.addDays(date, i));
            exportVo.setCreateTime2(localDateTime.plusDays(i));
            exportVos.add(exportVo);
        }

        ExcelUtils.exportExcel(exportVos, ExportCustomVo.class, "采购单", response);
    }

    @ApiOperation("导出定制模板")
    @GetMapping("/export/template")
    public void exportCellMergeTemplate(HttpServletResponse response) {
        Date date = new Date();
        LocalDateTime localDateTime = LocalDateTime.now();
        List<Map<String, Object>> exportVos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ExportCustomVo exportVo = new ExportCustomVo();
            exportVo.setFirstClass("random(firsts)");
            exportVo.setSecondClass("random(seconds)");
            exportVo.setThirdClass("random(thirds)");
            exportVo.setCode("random(codes)");
            exportVo.setPrice(BigDecimal.ZERO);
            exportVo.setCreateTime(DateUtils.addDays(date, i));
            exportVo.setCreateTime2(localDateTime.plusDays(i));
            exportVos.add(PageInfoUtil.beanToMap(exportVo));
        }
        Map<String, Object> map = new HashMap();
        map.put("maplist", exportVos);
        map.put("data", "ddd");

        TemplateExportParams params = new TemplateExportParams(this.getClass().getClassLoader().getResource("template.xls").getPath());
        ExcelUtils.exportExcel("采购单", params, map, response);
    }

    /**
     * html 转换 pdf
     */
    @GetMapping("pdf/exchange/html")
    public void pdfExchange() {
        HtmlPdfUtils.htmlToPdf();
    }

    /**
     * word 转换 pdf
     *
     * @return
     */
    @GetMapping("word/to/pdf")
    public String wordToPdf() {
        String jsonStr = getJsonStr();
        UploadDatasDto uploadDataDtos = JSONObject.parseObject(jsonStr, UploadDatasDto.class);
        UploadDataDto uploadDataDto = uploadDataDtos.getUploadDataDtoList().get(0);
        String filePath = Strings.EMPTY;
        Object object = null;
        if (uploadDataDto.getTemplateId() == UploadDataEnum.ONE_FOR_DISTRIBUTION_CONTRACT.getCode()) {
            filePath = UploadDataEnum.ONE_FOR_DISTRIBUTION_CONTRACT.getFilePath();
            object = uploadDataDto.getOneForDistributionContractDto();
        } else if (uploadDataDto.getTemplateId() == UploadDataEnum.FIRST_GIVE_CONTRACT.getCode()) {
            filePath = UploadDataEnum.FIRST_GIVE_CONTRACT.getFilePath();
            object = uploadDataDto.getFirstGiveContract();
        } else if (uploadDataDto.getTemplateId() == UploadDataEnum.LIVE_COMMON_CONTRACT.getCode()) {
            filePath = UploadDataEnum.LIVE_COMMON_CONTRACT.getFilePath();
            object = uploadDataDto.getLiveCommonContract();
        } else if (uploadDataDto.getTemplateId() == UploadDataEnum.LIVE_PREPARE_CONTRACT.getCode()) {
            filePath = UploadDataEnum.LIVE_PREPARE_CONTRACT.getFilePath();
            object = uploadDataDto.getLivePrepareContract();
        } else if (uploadDataDto.getTemplateId() == UploadDataEnum.MATERIAL_CONTRACT.getCode()) {
            filePath = UploadDataEnum.MATERIAL_CONTRACT.getFilePath();
            object = uploadDataDto.getMaterialContract();
        } else if (uploadDataDto.getTemplateId() == UploadDataEnum.XUE_LI_LIVE_OUT_CONTRACT.getCode()) {
            filePath = UploadDataEnum.XUE_LI_LIVE_OUT_CONTRACT.getFilePath();
            object = uploadDataDto.getXueLiLiveOutContract();
        } else if (uploadDataDto.getTemplateId() == UploadDataEnum.XUE_LI_LIVE_CONTRACT.getCode()) {
            filePath = UploadDataEnum.XUE_LI_LIVE_CONTRACT.getFilePath();
            object = uploadDataDto.getXueLiLiveContract();
        }
        if (!StringUtils.isEmpty(filePath) && object != null) {
            wordToPdfUtil.execute(filePath, object);
            return "success";
        } else {
            throw new RuntimeException("PurchaseExceptionState.WORD_PDF_UNDEFINE_ERROR");
        }
    }

    private String getJsonStr() {
        return "{\"uploadDataDtoList\":[{\"templateId\":6,\"poId\":1000027212,\"xueLiLiveOutContract\":" +
                "{\"financialBody\":\"衢州宸帆电子商务有限责任公司\",\"brandName\":\"XUELI\",\"poCode\":\"CG2101140001\"," +
                "\"vendorName\":\"壹毫不苟（杭州）品牌管理有限公司\",\"tableData\":[{\"accessoryRequisitionsInfoId\":null," +
                "\"appvouchDetailId\":null,\"arrivalQty\":211,\"color\":\"珍珠粉\",\"conEndDate\":\"2021-01-16 00:00:00\"" +
                ",\"conStartDate\":\"2021-01-14 00:00:00\",\"detailStatus\":3,\"exchName\":\"\",\"freeTaxMoney\":65940.6," +
                "\"gsp\":null,\"includedTaxMoney\":66600,\"inventoryCode\":\"CH21010010ZZFS\",\"inventoryId\":1003018," +
                "\"inventoryName\":\"[钱夫人1]210106-小李测试PDA扫描\",\"isDelete\":null,\"poCode\":\"CG2101140001\",\"poDetailId\"" +
                ":1000083045,\"poId\":1000027212,\"preOrderDetailId\":null,\"productCode\":\"CH21010010\",\"purchaseQuantity\":200," +
                "\"quantity\":200,\"quantityTop\":null,\"rejectionQty\":3,\"remark\":\"\",\"rrdQuantity\":0,\"salesType\":2,\"size\"" +
                ":\"S\",\"sort\":3,\"specName\":\"珍珠粉S\",\"specificationsRemark\":\"\",\"taxMoney\":659.4,\"taxRate\":1,\"taxUnitPrice\":" +
                "333,\"unitPrice\":329.703,\"updateDate\":null},{\"accessoryRequisitionsInfoId\":null,\"appvouchDetailId\":null,\"arrival" +
                "Qty\":0,\"color\":\"珍珠粉\",\"conEndDate\":\"2021-01-16 00:00:00\",\"conStartDate\":\"2021-01-14 00:00:00\",\"detailStat" +
                "us\":3,\"exchName\":\"\",\"freeTaxMoney\":65940.6,\"gsp\":null,\"includedTaxMoney\":66600,\"inventoryCode\":\"CH21010010Z" +
                "ZFM\",\"inventoryId\":1003019,\"inventoryName\":\"[钱夫人1]210106-小李测试PDA扫描\",\"isDelete\":null,\"poCode\":\"CG21011400" +
                "01\",\"poDetailId\":1000083046,\"poId\":1000027212,\"preOrderDetailId\":null,\"productCode\":\"CH21010010\",\"purchaseQua" +
                "ntity\":200,\"quantity\":200,\"quantityTop\":null,\"rejectionQty\":0,\"remark\":\"\",\"rrdQuantity\":0,\"salesType\":2,\"s" +
                "ize\":\"M\",\"sort\":4,\"specName\":\"珍珠粉M\",\"specificationsRemark\":\"\",\"taxMoney\":659.4,\"taxRate\":1,\"taxUnitPr" +
                "ice\":333,\"unitPrice\":329.703,\"updateDate\":null},{\"accessoryRequisitionsInfoId\":null,\"appvouchDetailId\":null,\"arri" +
                "valQty\":0,\"color\":\"珍珠粉\",\"conEndDate\":\"2021-01-16 00:00:00\",\"conStartDate\":\"2021-01-14 00:00:00\",\"detailSt" +
                "atus\":3,\"exchName\":\"\",\"freeTaxMoney\":65940.6,\"gsp\":null,\"includedTaxMoney\":66600,\"inventoryCode\":\"CH2101001" +
                "0ZZFL\",\"inventoryId\":1003020,\"inventoryName\":\"[钱夫人1]210106-小李测试PDA扫描\",\"isDelete\":null,\"poCode\":\"CG21011" +
                "40001\",\"poDetailId\":1000083047,\"poId\":1000027212,\"preOrderDetailId\":null,\"productCode\":\"CH21010010\",\"purchas" +
                "eQuantity\":200,\"quantity\":200,\"quantityTop\":null,\"rejectionQty\":0,\"remark\":\"\",\"rrdQuantity\":0,\"salesType" +
                "\":2,\"size\":\"L\",\"sort\":5,\"specName\":\"珍珠粉L\",\"specificationsRemark\":\"\",\"taxMoney\":659.4,\"taxRate\":1,\"t" +
                "axUnitPrice\":333,\"unitPrice\":329.703,\"updateDate\":null},{\"accessoryRequisitionsInfoId\":null,\"appvouchDetailId\":n" +
                "ull,\"arrivalQty\":0,\"color\":\"花灰\",\"conEndDate\":\"2021-01-16 00:00:00\",\"conStartDate\":\"2021-01-14 00:00:00\",\"de" +
                "tailStatus\":3,\"exchName\":\"\",\"freeTaxMoney\":65940.6,\"gsp\":null,\"includedTaxMoney\":66600,\"inventoryCode\":\"CH210" +
                "10010HVS\",\"inventoryId\":1003021,\"inventoryName\":\"[钱夫人1]210106-小李测试PDA扫描\",\"isDelete\":null,\"poCode\":\"CG210" +
                "1140001\",\"poDetailId\":1000083048,\"poId\":1000027212,\"preOrderDetailId\":null,\"productCode\":\"CH21010010\",\"purchas" +
                "eQuantity\":200,\"quantity\":200,\"quantityTop\":null,\"rejectionQty\":0,\"remark\":\"\",\"rrdQuantity\":0,\"salesType\":" +
                "2,\"size\":\"S\",\"sort\":3,\"specName\":\"花灰S\",\"specificationsRemark\":\"\",\"taxMoney\":659.4,\"taxRate\":1,\"taxUnit" +
                "Price\":333,\"unitPrice\":329.703,\"updateDate\":null},{\"accessoryRequisitionsInfoId\":null,\"appvouchDetailId\":null,\"ar" +
                "rivalQty\":0,\"color\":\"花灰\",\"conEndDate\":\"2021-01-16 00:00:00\",\"conStartDate\":\"2021-01-14 00:00:00\",\"detailSt" +
                "atus\":3,\"exchName\":\"\",\"freeTaxMoney\":65940.6,\"gsp\":null,\"includedTaxMoney\":66600,\"inventoryCode\":\"CH21010010HV" +
                "M\",\"inventoryId\":1003022,\"inventoryName\":\"[钱夫人1]210106-小李测试PDA扫描\",\"isDelete\":null,\"poCode\":\"CG2101140001\"," +
                "\"poDetailId\":1000083049,\"poId\":1000027212,\"preOrderDetailId\":null,\"productCode\":\"CH21010010\",\"purchaseQuantity\"" +
                ":200,\"quantity\":200,\"quantityTop\":null,\"rejectionQty\":0,\"remark\":\"\",\"rrdQuantity\":0,\"salesType\":2,\"size\":\"" +
                "M\",\"sort\":4,\"specName\":\"花灰M\",\"specificationsRemark\":\"\",\"taxMoney\":659.4,\"taxRate\":1,\"taxUnitPrice\":333,\"" +
                "unitPrice\":329.703,\"updateDate\":null},{\"accessoryRequisitionsInfoId\":null,\"appvouchDetailId\":null,\"arrivalQty\":0,\"" +
                "color\":\"花灰\",\"conEndDate\":\"2021-01-16 00:00:00\",\"conStartDate\":\"2021-01-14 00:00:00\",\"detailStatus\":3,\"exchNa" +
                "me\":\"\",\"freeTaxMoney\":65940.6,\"gsp\":null,\"includedTaxMoney\":66600,\"inventoryCode\":\"CH21010010HVL\",\"inventoryI" +
                "d\":1003023,\"inventoryName\":\"[钱夫人1]210106-小李测试PDA扫描\",\"isDelete\":null,\"poCode\":\"CG2101140001\",\"poDetailId\"" +
                ":1000083050,\"poId\":1000027212,\"preOrderDetailId\":null,\"productCode\":\"CH21010010\",\"purchaseQuantity\":200,\"quanti" +
                "ty\":200,\"quantityTop\":null,\"rejectionQty\":0,\"remark\":\"\",\"rrdQuantity\":0,\"salesType\":2,\"size\":\"L\",\"sort\":" +
                "5,\"specName\":\"花灰L\",\"specificationsRemark\":\"\",\"taxMoney\":659.4,\"taxRate\":1,\"taxUnitPrice\":333,\"unitPrice\":3" +
                "29.703,\"updateDate\":null},{\"accessoryRequisitionsInfoId\":null,\"appvouchDetailId\":null,\"arrivalQty\":0,\"color\":\"草绿" +
                "色\",\"conEndDate\":\"2021-01-16 00:00:00\",\"conStartDate\":\"2021-01-14 00:00:00\",\"detailStatus\":3,\"exchName\":\"\",\"" +
                "freeTaxMoney\":65940.6,\"gsp\":null,\"includedTaxMoney\":66600,\"inventoryCode\":\"CH21010010CLSS\",\"inventoryId\":1003024," +
                "\"inventoryName\":\"[钱夫人1]210106-小李测试PDA扫描\",\"isDelete\":null,\"poCode\":\"CG2101140001\",\"poDetailId\":1000083051,\"poI" +
                "d\":1000027212,\"preOrderDetailId\":null,\"productCode\":\"CH21010010\",\"purchaseQuantity\":200,\"quantity\":200,\"quantityTop\":n" +
                "ull,\"rejectionQty\":0,\"remark\":\"\",\"rrdQuantity\":0,\"salesType\":2,\"size\":\"S\",\"sort\":3,\"specName\":\"草绿色S\",\"specifi" +
                "cationsRemark\":\"\",\"taxMoney\":659.4,\"taxRate\":1,\"taxUnitPrice\":333,\"unitPrice\":329.703,\"updateDate\":null}],\"totalQuanti" +
                "ty\":1400,\"totalMoney\":466200,\"chineseTypeMoney\":\"肆拾陆万陆仟贰佰元整\",\"taxRate\":1,\"parameTer\":\"1\",\"parameTer1\":\"30\",\"" +
                "parameTer2\":\"\",\"parameTer3\":\"\",\"parameTer4\":\"\",\"chinaTer4\":\"\",\"parameTer5\":\"\",\"parameTer6\":\"\",\"chinaTer6\":\"\"" +
                ",\"parameTer7\":\"\",\"parameTer8\":\"\",\"settlementDay\":\"\",\"receivingAddress\":\"浙江省杭州市富阳区东洲街道明星路8号-到货组-（雪梨店）\",\"" +
                "contactPerson\":\"沈强\",\"contactPhone\":\"18656622567 \",\"moreLess1\":10,\"moreLess2\":3,\"moreLess3\":2,\"accessories\":\"1470.00\"," +
                "\"totalSum\":\"7350\",\"poNum10\":\"5\"}}],\"poToContracts\":[{\"poId\":1000027212,\"contract\":\"\",\"firstParty\":\"衢州宸帆电子商务有限责" +
                "任公司\",\"secondParty\":\"壹毫不苟\",\"vendorId\":695,\"isVendor\":0}]}";
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
        return strings[(int) (Math.random() * (strings.length))];
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
