package college.springcloud.order.vo;

import college.springcloud.common.serialize.InNumberMultiply100JS;
import college.springcloud.common.serialize.NumberJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author: xuxianbei
 * Date: 2020/4/3
 * Time: 14:13
 * Version:V1.0
 */
@Data
public class OrderVo {

    @JsonSerialize(using = NumberJsonSerializer.class)
    private Long forderAmount;

    @JsonSerialize(using = NumberJsonSerializer.class)
    private BigDecimal forderAmountBig;

//    @JsonDeserialize(using = InNumberMultiply100JS.class)
    @NotNull
    private BigDecimal skuPrice;


    /**
     * 结论JsonDeserialize 会修改原数据，导致校验失败
     */
    @JsonDeserialize(using = InNumberMultiply100JS.class)
    @Min(1)
    @Max(100)
    private Integer fcommissionPercent;
}
