package college.springcloud.student.service;

import cn.afterturn.easypoi.entity.ImageEntity;
import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import cn.afterturn.easypoi.handler.inter.IExcelExportServer;
import college.springcloud.common.utils.ExcelUtils;
import college.springcloud.common.utils.Test;
import college.springcloud.common.utils.pageinfo.PageInfoUtil;
import college.springcloud.student.dto.*;
import college.springcloud.student.service.template.PayReportVendorItemVo;
import college.springcloud.student.service.template.PayReportVendorSonTotalVo;
import college.springcloud.student.service.template.PayReportVendorTotalVo;
import college.springcloud.student.service.template.PayReportVendorVo;
import college.springcloud.student.service.verify.ExportVerifyHandler;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Supplier;

/**
 * User: xuxianbei
 * Date: 2019/11/7
 * Time: 16:42
 * Version:V1.0
 */
@Service
public class StudentServiceImpl implements IExcelExportServer {
    @Override
    public List<Object> selectListForExcelExport(Object queryParams, int page) {
        //这里只是简单的是示例
        StudentDto studentDto = (StudentDto) queryParams;
        //本来是这里设置页码数量的
        studentDto.setCurrentPage(page);
        List<Object> list = new ArrayList<>();
        if (page >= 2) return list;
        Supplier<ExportVo> supplier = ExportVo::new;
        ExportVo exportVo = supplier.get();
        exportVo.setFsupplierOrderId(studentDto.getName());
        exportVo.setFaftersaleStatus(1);
        exportVo.setFbatchId(studentDto.getName());
        exportVo.setFcreateTime(new Date());
        list.add(exportVo);
        return list;
    }

    private void createExplicitList(Workbook workbook) {
        String[] dataArray = new String[]{"xx1", "xx2", "xx3"};
        Sheet hidden = workbook.createSheet("hidden");
        for (int i = 0; i < dataArray.length; i++) {
            hidden.createRow(i).createCell(0).setCellValue(dataArray[i]);
        }

        Name namedCell = workbook.createName();
        namedCell.setNameName("hidden");
        namedCell.setRefersToFormula("hidden!$A$1:$A$" + dataArray.length);

        //将第二个sheet设置为隐藏
        workbook.setSheetHidden(1, true);
        workbook.getSheetAt(0).addValidationData(new HSSFDataValidation(new CellRangeAddressList(1, 300, 5, 5),
                DVConstraint.createFormulaListConstraint("hidden")));
    }

    @SneakyThrows
    public void downloadTemplate(HttpServletResponse response) {
        ExportParams exportParams = new ExportParams();
        exportParams.setType(ExcelType.HSSF);
        exportParams.setSheetName("发货单导入模板");
        try (Workbook workbook = ExcelExportUtil.exportExcel(exportParams, ExportVo.class, Lists.newArrayList())) {
            //设置筛选框
            createExplicitList(workbook);
            ExcelUtils.exportEmptyContentExcel(response, workbook, "发货单导入模板");
        }
    }

    @SneakyThrows
    public List<Map<String, Object>> importDataDynamic(MultipartFile file, HttpServletResponse response) {
        try (InputStream inputStream = new ByteArrayInputStream(file.getBytes())) {
            ImportParams importParams = new ImportParams();
//            importParams.setHeadRows(1);
            List<Map<String, Object>> transportImportVos = ExcelImportUtil.importExcel(inputStream, Map.class, importParams);
            transportImportVos.stream().forEach(key -> key.put("测试001", "测试001"));

            //失败无法动态导出
//            ExcelUtils.exportExcel(transportImportVos, Map.class, "采购单", response);
            return transportImportVos;
        }
    }


    @SneakyThrows
    public List<ExportVo> importData(MultipartFile file) {
        try (InputStream inputStream = new ByteArrayInputStream(file.getBytes())) {
            ImportParams importParams = new ImportParams();
            importParams.setHeadRows(1);
            List<ExportVo> transportImportVos = ExcelImportUtil.importExcel(file.getInputStream(), ExportVo.class, importParams);
            return transportImportVos;
        }
    }

    public static void main(String[] args) {
        ImportParams importParams = new ImportParams();
//        importParams.setNeedSave(true);
        importParams.setNeedSave(false);
        List<ExportImageVo> list = ExcelImportUtil.importExcel(new File("C:\\Users\\2250\\Desktop\\test2.xlsx"), ExportImageVo.class, importParams);
        File file = new File(list.get(0).getPicture());
        System.out.println(file.toPath());
    }

    public List<ExportImageVo> importImage(MultipartFile file) {
        InputStream inputStream = null;
        try {
            inputStream = new ByteArrayInputStream(file.getBytes());
            ImportParams importParams = new ImportParams();
            importParams.setNeedSave(false);
            //file.getInputStream()
            List<ExportImageVo> list = ExcelImportUtil.importExcel(inputStream, ExportImageVo.class, importParams);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SneakyThrows
    public List<ExportVertifyVo> importVertify(MultipartFile file, HttpServletResponse response) {
        try (InputStream inputStream = new ByteArrayInputStream(file.getBytes())) {
            ImportParams importParams = new ImportParams();
//            importParams.setHeadRows(1);
            importParams.setNeedVerify(true);
            ExportVerifyHandler exportVerifyHandler = new ExportVerifyHandler();
            importParams.setVerifyHandler(exportVerifyHandler);
            ExcelImportResult<ExportVertifyVo> result = ExcelImportUtil.importExcelMore(inputStream, ExportVertifyVo.class, importParams);
            ExcelUtils.downLoadExcel("错误excle.xlsx", response, result.getFailWorkbook());
            return result.getList();
        }

    }

    public void exportTemplate(HttpServletResponse servletResponse) {
        TemplateExportParams templateExportParams = new TemplateExportParams("templates/付款申请统计模板.xlsx");
        PayReportVendorVo payReportVendorVo = new PayReportVendorVo();
        PageInfo<PayReportVendorItemVo> pageInfo = new PageInfo<>();
        List<PayReportVendorItemVo> list = new ArrayList<>();
        PayReportVendorItemVo payReportVendorItemVo = new PayReportVendorItemVo();
        PayReportVendorTotalVo payReportVendorTotalVo = new PayReportVendorTotalVo();
        PayReportVendorSonTotalVo payReportVendorSonTotalVo = new PayReportVendorSonTotalVo();
        Test.randomFillProperty(payReportVendorItemVo, PayReportVendorItemVo.class);
        Test.randomFillProperty(payReportVendorTotalVo, PayReportVendorTotalVo.class);
        Test.randomFillProperty(payReportVendorSonTotalVo, PayReportVendorSonTotalVo.class);
        list.add(payReportVendorItemVo);
        pageInfo.setList(list);
        payReportVendorVo.setPayReportVendorItemVos(pageInfo);
        payReportVendorVo.setPayReportVendorTotalVo(payReportVendorTotalVo);
        payReportVendorVo.setPayReportVendorSonTotalVo(payReportVendorSonTotalVo);
        Map<String, Object> map = new HashMap();
        map.putAll(PageInfoUtil.beanToMap(payReportVendorTotalVo));
        map.putAll(PageInfoUtil.beanToMap(payReportVendorSonTotalVo));
        Map<String, Object> mapItem = PageInfoUtil.beanToMap(payReportVendorItemVo);
        List<Map<String, Object>> listMap = new ArrayList<>();
        listMap.add(mapItem);
        map.put("payReportVendorItemVos", listMap);
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setWidth(200);
        imageEntity.setHeight(400);
        //仅支持路径以图片格式结尾的网址。支持图片文件。
        imageEntity.setUrl("http://10.228.81.222:19305/chenfan_filestore/file/view/fc68357569dd423db11fd2f2cb54d016.jpg");
        map.put("mainMap0", imageEntity);
        ImageEntity imageEntity1 = new ImageEntity();
        imageEntity1.setWidth(200);
        imageEntity1.setHeight(400);
        imageEntity1.setUrl("http://10.228.81.222:19305/chenfan_filestore/file/view/fc68357569dd423db11fd2f2cb54d016.jpg");
        map.put("mainMap1", imageEntity1);
        ImageEntity imageEntity2 = new ImageEntity();
        imageEntity2.setWidth(200);
        imageEntity2.setHeight(400);
        imageEntity2.setUrl("http://10.228.81.222:19305/chenfan_filestore/file/view/fc68357569dd423db11fd2f2cb54d016.jpg");
        map.put("mainMap2", imageEntity2);
        Workbook workbook = ExcelExportUtil.exportExcel(templateExportParams, map);
        try {
            ExcelUtils.downLoadExcel("模板打印.xlsx", servletResponse, workbook);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
