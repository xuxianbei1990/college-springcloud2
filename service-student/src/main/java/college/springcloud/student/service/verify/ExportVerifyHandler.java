package college.springcloud.student.service.verify;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import college.springcloud.student.dto.ExportVertifyVo;

/**
 * @author: xuxianbei
 * Date: 2021/10/14
 * Time: 17:46
 * Version:V1.0
 */
public class ExportVerifyHandler implements IExcelVerifyHandler<ExportVertifyVo> {


    @Override
    public ExcelVerifyHandlerResult verifyHandler(ExportVertifyVo obj) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult();
        result.setSuccess(true);
        if (!obj.getFsupplierOrderId().equals("ddd")) {
            result.setSuccess(false);
            result.setMsg("自定义错误");
        }
        return result;
    }
}
