package college.springcloud.order.service.impl;

import college.springcloud.common.query.Criteria;
import college.springcloud.common.service.BaseService;
import college.springcloud.order.po.SupplierTransportOrder;
import college.springcloud.order.po.SupplierTransportSku;
import college.springcloud.order.service.SupplierTransportSkuService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: xuxianbei
 * Date: 2020/1/9
 * Time: 16:01
 * Version:V1.0
 */
@Service
public class SupplierTransportSkuServiceImpl extends BaseService<SupplierTransportSku> implements SupplierTransportSkuService {

    @Override
    public List<SupplierTransportSku> criteriaIn() {
        List<SupplierTransportOrder> orderList = new ArrayList<>();
        SupplierTransportOrder transportOrder = new SupplierTransportOrder();
        transportOrder.setFtransportOrderId("F5755150811408755");
        orderList.add(transportOrder);
        List<String> orderIds = orderList.stream().map(SupplierTransportOrder::getFtransportOrderId).collect(Collectors.toList());
        Criteria<SupplierTransportSku, Object> criteria = Criteria.of(SupplierTransportSku.class);
        criteria.andIn(SupplierTransportSku::getFtransportOrderId, orderIds);
        return queryByCriteria(criteria);
    }
}
