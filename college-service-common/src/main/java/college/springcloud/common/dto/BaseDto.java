package college.springcloud.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * User: xuxianbei
 * Date: 2019/8/30
 * Time: 10:26
 * Version:V1.0
 */
@Data
public class BaseDto implements Serializable {
    private static final long serialVersionUID = -5000573466808670741L;
    /**
     * 查询类型
     */
    @ApiModelProperty(value = "查询类型")
    private Integer searchType;

    /**
     * 查询关键字
     */
    @ApiModelProperty(value = "查询关键字")
    private String keyword;
}
