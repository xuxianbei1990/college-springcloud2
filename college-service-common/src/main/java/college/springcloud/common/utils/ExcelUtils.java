package college.springcloud.common.utils;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.export.ExcelExportService;
import cn.afterturn.easypoi.excel.export.template.ExcelExportOfTemplateUtil;
import cn.afterturn.easypoi.handler.inter.IExcelDataHandler;
import cn.afterturn.easypoi.handler.inter.IExcelExportServer;
import college.springcloud.common.dto.PageDto;
import college.springcloud.common.exception.BizException;
import college.springcloud.common.exception.CollegeExceptionCode;
import college.springcloud.common.utils.excel.ExcelDataHandlerDateImpl;
import college.springcloud.common.utils.excel.ExcelExportCustomService;
import college.springcloud.common.utils.excel.ExcelExportStatisticStyler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

import static cn.afterturn.easypoi.excel.ExcelExportUtil.USE_SXSSF_LIMIT;

@Slf4j
public class ExcelUtils {
    private final static Integer EASYPOI_EXPORT_EXCEL_APPEND_OFFSET = 1000;

    // 导出表的列名
    private String[] head;

    private Map<String, List<Object[]>> dataMap = new HashMap<String, List<Object[]>>();

    private String fileName;

    private List<String> sheetName = new ArrayList<String>();

    private HttpServletResponse response;

    // 构造方法，传入要导出的数据
    public ExcelUtils(String[] head, Map<String, List<Object[]>> dataMap, String fileName, List<String> sheetName, HttpServletResponse response) {
        this.dataMap = dataMap;
        this.head = head;
        this.fileName = fileName;
        this.sheetName = sheetName;
        this.response = response;
    }

    // 构造方法，传入要导出的数据
    public ExcelUtils(String[] head, Map<String, List<Object[]>> dataMap, List<String> sheetName, String fileName) {
        this.dataMap = dataMap;
        this.head = head;
        this.sheetName = sheetName;
        this.fileName = fileName;
    }

    /*
     * 导出数据
     */
    public void export() throws Exception {
        HSSFWorkbook wb = new HSSFWorkbook();
        for (int j = 0, len = sheetName.size(); j < len; j++) {
            HSSFSheet sheet = wb.createSheet(sheetName.get(j));
            sheet.setDefaultRowHeight((short) (20 * 20));
            sheet.setDefaultColumnWidth(20);

            HSSFRow row = sheet.createRow(0);

            // 设置表头
            for (int i = 0; i < head.length; i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(head[i]);
                cell.setCellStyle(getColumnTopStyle(wb));
            }

            List<Object[]> list = dataMap.get(sheetName.get(j));
            for (int i = 0; i < list.size(); i++) {
                row = sheet.createRow(i + 1);
                for (int k = 0, length = list.get(i).length; k < length; k++) {
                    HSSFCell cell = row.createCell(k);
                    cell.setCellValue(String.valueOf(list.get(i)[k]));
                    // cell.setCellStyle(getStyle(wb));
                }
            }
        }
        try {
            String headStr = "attachment; filename=\"" + fileName + "\"";
            response.setContentType("APPLICATION/OCTET-STREAM");
            response.setHeader("Content-Disposition", headStr);
            OutputStream out = response.getOutputStream();
            wb.write(out);
            // out.write(b);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 导出时第一行为示例，字体为红色
    public void exportWithRed() throws Exception {
        HSSFWorkbook wb = new HSSFWorkbook();
        for (int j = 0, len = sheetName.size(); j < len; j++) {
            HSSFSheet sheet = wb.createSheet(sheetName.get(j));
            sheet.setDefaultRowHeight((short) (20 * 20));
            sheet.setDefaultColumnWidth(20);
            sheet.setColumnWidth(18, 27 * 256);
            sheet.setColumnWidth(20, 27 * 256);
            sheet.setColumnWidth(25, 27 * 256);
            HSSFRow row = sheet.createRow(0);
            row.setHeight((short) (25 * 25));
            for (int i = 0; i < head.length; i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(head[i]);
                cell.setCellStyle(autoWrap(wb));
            }

            List<Object[]> list = dataMap.get(sheetName.get(j));
            for (int i = 0; i < list.size(); i++) {
                row = sheet.createRow(i + 1);
                for (int k = 0, length = list.get(i).length; k < length; k++) {
                    HSSFCell cell = row.createCell(k);
                    cell.setCellValue(String.valueOf(list.get(i)[k]));
                    cell.setCellStyle(textFormat(k, wb));
                    if (i == 0) {
                        cell.setCellStyle(firstRedStyle(wb));
                    }
                }
            }
        }
        try {
            String headStr = "attachment; filename=\"" + fileName + "\"";
            response.setContentType("APPLICATION/OCTET-STREAM");
            response.setHeader("Content-Disposition", headStr);
            OutputStream out = response.getOutputStream();
            wb.write(out);
            // out.write(b);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * 导出数据
     */
    public void exportToPath() throws Exception {
        HSSFWorkbook wb = new HSSFWorkbook();
        for (int j = 0, len = sheetName.size(); j < len; j++) {
            HSSFSheet sheet = wb.createSheet(sheetName.get(j));
            sheet.setDefaultRowHeight((short) (20 * 20));
            sheet.setDefaultColumnWidth(20);

            HSSFRow row = sheet.createRow(0);

            // 设置表头
            for (int i = 0; i < head.length; i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(head[i]);
                cell.setCellStyle(getColumnTopStyle(wb));
            }

            List<Object[]> list = dataMap.get(sheetName.get(j));
            for (int i = 0; i < list.size(); i++) {
                row = sheet.createRow(i + 1);
                for (int k = 0, length = list.get(i).length; k < length; k++) {
                    HSSFCell cell = row.createCell(k);
                    cell.setCellValue(String.valueOf(list.get(i)[k]));
                    // cell.setCellStyle(getStyle(wb));
                }
            }
        }
        try {
            OutputStream out = new FileOutputStream(fileName);
            wb.write(out);
            // out.write(b);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * 列头单元格样式
     */
    public HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 11);
        // 字体加粗
//		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setBold(true);
        // 设置字体名字
        font.setFontName("Courier New");
        // 设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        /*
         * //设置底边框; style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //设置底边框颜色;
         * style.setBottomBorderColor(HSSFColor.BLACK.index); //设置左边框;
         * style.setBorderLeft(HSSFCellStyle.BORDER_THIN); //设置左边框颜色;
         * style.setLeftBorderColor(HSSFColor.BLACK.index); //设置右边框;
         * style.setBorderRight(HSSFCellStyle.BORDER_THIN); //设置右边框颜色;
         * style.setRightBorderColor(HSSFColor.BLACK.index); //设置顶边框;
         * style.setBorderTop(HSSFCellStyle.BORDER_THIN); //设置顶边框颜色;
         * style.setTopBorderColor(HSSFColor.BLACK.index);
         */
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;

    }

    /*
     * 列数据信息单元格样式
     */
    public HSSFCellStyle getStyle(HSSFWorkbook workbook) {
        // 设置字体
        // HSSFFont font = workbook.createFont();
        // 设置字体大小
        // font.setFontHeightInPoints((short)10);
        // 字体加粗
        // font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 设置字体名字
        // font.setFontName("Courier New");
        // 设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        /*
         * //设置底边框; style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //设置底边框颜色;
         * style.setBottomBorderColor(HSSFColor.BLACK.index); //设置左边框;
         * style.setBorderLeft(HSSFCellStyle.BORDER_THIN); //设置左边框颜色;
         * style.setLeftBorderColor(HSSFColor.BLACK.index); //设置右边框;
         * style.setBorderRight(HSSFCellStyle.BORDER_THIN); //设置右边框颜色;
         * style.setRightBorderColor(HSSFColor.BLACK.index); //设置顶边框;
         * style.setBorderTop(HSSFCellStyle.BORDER_THIN); //设置顶边框颜色;
         * style.setTopBorderColor(HSSFColor.BLACK.index);
         */
        // 在样式用应用设置的字体;
        // style.setFont(font);
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;

    }

    // 设置字体为红色，格式为文本
    public HSSFCellStyle firstRedStyle(HSSFWorkbook workbook) {
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setColor(new Short("1"));
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
        style.setFont(font);
        return style;
    }

    // 设置单元格格式为文本格式
    public HSSFCellStyle textFormat(int i, HSSFWorkbook workbook) {
        HSSFCellStyle style = workbook.createCellStyle();
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
        return style;
    }

    public HSSFCellStyle autoWrap(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        // 字体加粗
        font.setBold(true);
        // 设置字体名字
        font.setFontName("Courier New");
        // 设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        style.setWrapText(true);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    public HSSFCellStyle green(HSSFWorkbook workbook) {
        HSSFCellStyle style = workbook.createCellStyle();
        style.setTopBorderColor(new Short("1"));
        style.setFillForegroundColor(new Short("1"));
        return style;
    }

    /**
     * 通过easypoi导出excel文件
     *
     * @param fileName
     * @param pageDto
     * @param voClass
     * @param excelExportServer
     * @param response
     */
    public static void exportExcelByEasyPoi(String fileName, PageDto pageDto, Class<?> voClass,
                                            IExcelExportServer excelExportServer, HttpServletResponse response) {
        try {
            try (Workbook workbook = ExcelExportUtil.exportBigExcel(new ExportParams(null, fileName), voClass, excelExportServer, pageDto)) {
                response.setHeader("content-Type", "application/vnd.ms-excel;charset=utf-8");
                response.setHeader("Content-Disposition", StringUtils.join("attachment;filename=",
                        new String(fileName.getBytes("utf-8"), "iso8859-1"), ".xls"));
                try (ServletOutputStream outputStream = response.getOutputStream()) {
                    workbook.write(outputStream);
                }
            }
        } catch (Throwable e) {
            log.error("导出{}出错", fileName, e);
            throw new RuntimeException("系统异常");
        }
    }

    /**
     * 导出一个空内容的Excel
     *
     * @param response
     * @param workbook excel对象
     * @param fileName 文件名入参时不包含后缀
     * @throws Exception
     */
    public static void exportEmptyContentExcel(HttpServletResponse response, Workbook workbook, String fileName) {
        try {
            //response重置之前需要判断是否已经committed，避免response throw new IllegalStateException("Committed")
            if (!response.isCommitted()) {
                response.reset();
            }
            response.setHeader("content-Type", "application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", StringUtils.join("attachment;filename=",
                    new String(fileName.getBytes("utf-8"), "iso8859-1"), ".xls"));
            try (ServletOutputStream outputStream = response.getOutputStream()) {
                workbook.write(outputStream);
            }
        } catch (Throwable e) {
            log.error("导出{}出错", fileName, e);
            throw new BizException(CollegeExceptionCode.SYSTEM_ERROR);
        }
    }

    /**
     * 导出excel 按照目标类型
     *
     * @param list
     * @param tClass
     * @param sheetName
     * @param response
     * @param <T>
     */
    public static <T> void exportExcel(List<?> list, Class<T> tClass, String sheetName, HttpServletResponse response) {
        try {
            defaultExport(list, tClass, sheetName + ".xls", response, new ExportParams(null, sheetName));
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * 模板导出
     *
     * @param params
     * @param map
     */
    public static void exportExcel(String fileName, TemplateExportParams params, Map<String, Object> map, HttpServletResponse response) {
        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        if (workbook != null) {
            try {
                downLoadExcel(fileName + ".xls", response, workbook);
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    private static void defaultExport(
            List<?> list,
            Class<?> pojoClass,
            String fileName,
            HttpServletResponse response,
            ExportParams exportParams)
            throws Exception {
        exportParams.setDataHandler(new ExcelDataHandlerDateImpl());
        exportParams.setStyle(ExcelExportStatisticStyler.class);
        Workbook workbook = exportExcel(exportParams, pojoClass, list);
        if (workbook != null) {
            downLoadExcel(fileName, response, workbook);
        }
    }

    public static Workbook exportExcel(ExportParams entity, Class<?> pojoClass,
                                       Collection<?> dataSet) {
        Workbook workbook = getWorkbook(entity.getType(), dataSet.size());
        new ExcelExportCustomService().createSheet(workbook, entity, pojoClass, dataSet);
        return workbook;
    }

    private static Workbook getWorkbook(ExcelType type, int size) {
        if (ExcelType.HSSF.equals(type)) {
            return new HSSFWorkbook();
        } else if (size < USE_SXSSF_LIMIT) {
            return new XSSFWorkbook();
        } else {
            return new SXSSFWorkbook();
        }
    }

    public static void downLoadExcel(
            String fileName, HttpServletResponse response, Workbook workbook) throws Exception {

        try (OutputStream out = response.getOutputStream()) {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            response.setHeader("Content-Length", String.valueOf(baos.size()));
            out.write(baos.toByteArray());
        } catch (IOException e) {
            try {
                throw new Exception(e.getMessage());
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

}
