package college.springcloud.common.utils.excel;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.excel.export.ExcelExportService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

import java.text.ParseException;
import java.util.Date;

/**
 * 重写时间格式
 *
 * @author: xuxianbei
 * Date: 2021/8/17
 * Time: 16:08
 * Version:V1.0
 */
public class ExcelExportCustomService extends ExcelExportService {

    @Override
    public void createStringCell(Row row, int index, String text, CellStyle style, ExcelExportEntity entity) {
        super.createStringCell(row, index, text, style, entity);
        if (StringUtils.isNotBlank(entity.getFormat())) {
            Cell cell = row.getCell(index);
            try {
                Date date = DateUtils.parseDate(text, entity.getFormat());
                style.setDataFormat(row.getSheet().getWorkbook().getCreationHelper().createDataFormat().getFormat(entity.getFormat()));
                cell.setCellStyle(style);
                cell.setCellValue(date);
            } catch (ParseException e) {
            }
        }
    }
}
