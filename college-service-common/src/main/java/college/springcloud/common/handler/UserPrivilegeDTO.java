package college.springcloud.common.handler;

import lombok.Data;

import java.util.List;

/**
 * @author: xuxianbei
 * Date: 2021/3/2
 * Time: 15:14
 * Version:V1.0
 */
@Data
public class UserPrivilegeDTO {
    private Long companyId;
    private List<Long> companyIds;
    private List<Long> departmentIds;
    private List<Long> userIds;
    private List<Integer> brandIds;
    private String dataPrivilegeCode;
    private Boolean disableStatus;
}
