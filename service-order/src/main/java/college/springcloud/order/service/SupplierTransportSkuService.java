package college.springcloud.order.service;

import college.springcloud.common.service.IService;
import college.springcloud.order.po.SupplierTransportSku;

import java.util.List;

/**
 * @author: xuxianbei
 * Date: 2020/1/9
 * Time: 16:01
 * Version:V1.0
 */
public interface SupplierTransportSkuService extends IService<SupplierTransportSku> {

    List<SupplierTransportSku> criteriaIn();
}
