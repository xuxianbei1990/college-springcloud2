package college.springcloud.student.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * @author: xuxianbei
 * Date: 2021/10/15
 * Time: 11:32
 * Version:V1.0
 */
@Data
public class ExportImageVo {

    @Excel(name = "图片", type = 2, width = 192, height = 192, imageType = 1)
    private String picture;

    @Excel(name = "花名")
    private String flower;

    @Excel(name = "品名品类")
    private String brandName;
}
