package college.springcloud.student.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * @author: xuxianbei
 * Date: 2021/10/14
 * Time: 15:02
 * Version:V1.0
 */
@Data
public class ExportVertifyVo implements IExcelModel {

    @Excel(name = "采购订单号", width = 20)
    @Pattern(regexp = "^[-\\+]?[\\d]*$", message = "不是数字")
    private String fsupplierOrderId;


    @Override
    public String getErrorMsg() {
        return null;
    }

    @Override
    public void setErrorMsg(String errorMsg) {

    }
}
