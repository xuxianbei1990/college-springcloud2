package college.springcloud.order.dto;

import college.springcloud.order.vo.OrderVo;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;

/**
 * @author: xuxianbei
 * Date: 2020/4/3
 * Time: 14:39
 * Version:V1.0
 */
@Data
public class OrderVoDto {

    @Valid
    private List<OrderVo> list;
}
