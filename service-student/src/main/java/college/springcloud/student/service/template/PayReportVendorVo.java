package college.springcloud.student.service.template;

import com.github.pagehelper.PageInfo;
import lombok.Data;

/**
 * @author: xuxianbei
 * Date: 2021/12/21
 * Time: 20:18
 * Version:V1.0
 */
@Data
public class PayReportVendorVo {
    /**
     * 列表
     */
    private PageInfo<PayReportVendorItemVo> payReportVendorItemVos;

    /**
     * 小计
     */
    private PayReportVendorSonTotalVo payReportVendorSonTotalVo;

    /**
     * 总计
     */
    private PayReportVendorTotalVo payReportVendorTotalVo;
}
