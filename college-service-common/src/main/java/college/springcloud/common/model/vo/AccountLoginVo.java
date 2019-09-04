package college.springcloud.common.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * User: xuxianbei
 * Date: 2019/8/30
 * Time: 20:29
 * Version:V1.0
 */
@Data
public class AccountLoginVo implements Serializable {

    private static final long serialVersionUID = -7811446107750715704L;

    @ApiModelProperty(value = "用户id", example = "1")
    private Long fadminId;

    @ApiModelProperty(value = "token", example = "123!@324")
    private String token;

    @ApiModelProperty(value = "管理员名称", example = "xxb")
    private String fname;

    @ApiModelProperty(value = "登录ip", example = "192.168.0.2")
    private String flastloginIp;

    @ApiModelProperty(value = "管理员姓名", example = "xxb")
    private String frealName;

    @ApiModelProperty(value = "手机号", example = "11245682156")
    private String fmobile;

    @ApiModelProperty(value = "管理员编号", example = "XSDXZ")
    private String facode;

    @ApiModelProperty(value = "管理员备注", example = "XSDXZ")
    private String fremark;

    @ApiModelProperty(value = "角色ID", example = "1")
    private Long froleId;

    @ApiModelProperty(value = "是否启用(0不启用,1启用", example = "1")
    private Integer fstatus;

    @ApiModelProperty(value = "登录时间", example = "2019/8/31 21:21:21")
    private Date floginTime;

    @ApiModelProperty(value = "权限列表")
    private List<AdminModuleVo> adminModuleVos;

}
