package college.springcloud.common.utils.eexcel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 垂直导入excel
 *
 * @author: xuxianbei
 * Date: 2021/10/25
 * Time: 11:20
 * Version:V1.0
 */
public class MyVerticalExcelImportUtil {

    private static final int MAX_ROW_NUM = 1000;

    private int getActiveRow(Sheet sheet) {
        int activeRow = 0;
        int emptyCount = 0;
        for (int i = 0; i < MAX_ROW_NUM; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                return i;
            }
            Cell cell = row.getCell(0);
            if (StringUtils.isEmpty(cell.getStringCellValue())) {
                if (emptyCount == 0) {
                    activeRow = i;
                }
                emptyCount++;
                if (emptyCount == 3) {
                    return activeRow;
                }
            } else {
                emptyCount = 0;
            }
        }
        return activeRow;
    }

    /**
     * excel 转换 实体
     *
     * @param inputStream
     * @return
     */
    public List<Student> excelToEntity(InputStream inputStream) {
        List<Map<String, List<String>>> orign = parseExcel(inputStream, "sheet1", 6);
        Class<Student> studentClass = Student.class;
        //获取目标数据
        Map<String, Field> nameFieldMap = getTargetFields(studentClass);
        List<Student> students = new ArrayList<>();

        for (Map<String, List<String>> map : orign) {
            for (int index = 1; index < 6; index++) {
                Student student = new Student();
                for (Map.Entry<String, Field> entry : nameFieldMap.entrySet()) {
                    List<String> values = map.get(entry.getKey());
                    if (index < values.size()) {
                        String value = values.get(index);
                        Field field = entry.getValue();
                        setValue(student, value, field);
                    } else {
                        break;
                    }
                }
                students.add(student);
            }
        }

        return students;
    }

    private void setValue(Student student, String value, Field field) {
        try {
            field.setAccessible(true);
            field.set(student, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Field> getTargetFields(Class<Student> studentClass) {
        Map<String, Field> nameFieldMap = new HashMap<>();
        for (Field declaredField : studentClass.getDeclaredFields()) {
            if (declaredField.isAnnotationPresent(Excel.class)) {
                Excel excel = declaredField.getAnnotation(Excel.class);
                String name = excel.name();
                nameFieldMap.put(name, declaredField);
            }
        }
        return nameFieldMap;
    }


    /**
     * 解析excel 得到原始数据
     *
     * @param inputStream
     * @param sheetName
     * @param activeColumn
     * @return
     */
    public List<Map<String, List<String>>> parseExcel(InputStream inputStream, String sheetName, int activeColumn) {
        Sheet sheet = getSheet(inputStream, sheetName);
        int activeRow = getActiveRow(sheet);
        List<Map<String, List<String>>> orign = createOriginValues(activeColumn, sheet, activeRow);
        return orign;
    }


    /**
     * 默认第一列是列名
     *
     * @param activeColumn
     * @param sheet
     * @param activeRow
     * @return
     */
    private List<Map<String, List<String>>> createOriginValues(int activeColumn, Sheet sheet, int activeRow) {
        List<Map<String, List<String>>> orign = new ArrayList<>();
        Map<String, List<String>> rowMap = new HashMap<>();
        for (int i = 0; i < activeRow; i++) {
            List<String> rowList = new ArrayList();
            String columnName = "";
            for (int j = 0; j < activeColumn; j++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(j);
                String value = getCellValueByType(cell);
                if (j == 0) {
                    columnName = value;
                }
                rowList.add(value);
            }
            if (rowMap.get(columnName) == null) {
                rowMap.put(columnName, rowList);
            } else {
                i--;
                orign.add(rowMap);
                rowMap = new HashMap<>();
            }
        }
        orign.add(rowMap);
        return orign;
    }


    public String getCellValueByType(Cell cell) {
        String cellValue = "";
        if (cell.getCellType() == CellType.NUMERIC) {
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                cellValue = DateFormatUtils.format(cell.getDateCellValue(), "yyyy-MM-dd");
            } else {
                NumberFormat nf = NumberFormat.getInstance();
                cellValue = String.valueOf(nf.format(cell.getNumericCellValue())).replace(",", "");
            }

        } else if (cell.getCellType() == CellType.STRING) {
            cellValue = String.valueOf(cell.getStringCellValue());
        } else if (cell.getCellType() == CellType.BOOLEAN) {
            cellValue = String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == CellType.ERROR) {
            cellValue = "错误类型";
            throw new RuntimeException("单元格: " + cellValue);
        }
        return cellValue;
    }


    private Sheet getSheet(InputStream inputStream, String sheetName) {
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Sheet sheet = null;
        if (StringUtils.isEmpty(sheetName)) {
            sheet = workbook.getSheetAt(0);
        } else {
            sheet = workbook.getSheet(sheetName);
        }
        return sheet;
    }

    public static void main(String[] args) {
        MyVerticalExcelImportUtil myVerticalExcelImportUtil = new MyVerticalExcelImportUtil();
        List<Student> list = myVerticalExcelImportUtil.excelToEntity(MyVerticalExcelImportUtil.class.getResourceAsStream("/student.xlsx"));
        System.out.println(list);
    }
}
