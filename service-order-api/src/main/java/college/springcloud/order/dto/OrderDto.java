package college.springcloud.order.dto;

import college.springcloud.common.dto.PageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * User: xuxianbei
 * Date: 2019/8/30
 * Time: 10:04
 * Version:V1.0
 */
@Data
public class OrderDto extends PageDto {
    private static final long serialVersionUID = 6296204765747936654L;

    @ApiModelProperty(value = "订单号")
    private String forderId;
}
