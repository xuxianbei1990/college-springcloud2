package college.springcloud.common.utils.excel;

import cn.afterturn.easypoi.handler.impl.ExcelDataHandlerDefaultImpl;

/**
 * 时间处理
 * @author: xuxianbei
 * Date: 2021/8/17
 * Time: 15:39
 * Version:V1.0
 */
public class ExcelDataHandlerDateImpl extends ExcelDataHandlerDefaultImpl {


    public ExcelDataHandlerDateImpl() {
        setNeedHandlerFields(new String[]{"时间Date"});
    }

    @Override
    public Object exportHandler(Object obj, String name, Object value) {
         return value;
    }
}
