package college.springcloud.common.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * User: xuxianbei
 * Date: 2019/8/31
 * Time: 15:59
 * Version:V1.0
 */
@Data
public class AdminModuleVo implements Serializable {

    private static final long serialVersionUID = 3634612575205009420L;

    @ApiModelProperty(value = "中台模块id", example = "1")
    private Long fmoduleId;

    @ApiModelProperty(value = "中台模块名称", example = "运营")
    private String fmoduleName;

    @ApiModelProperty(value = "中台模块链接", example = "102185/512154/48")
    private String fhref;
}
