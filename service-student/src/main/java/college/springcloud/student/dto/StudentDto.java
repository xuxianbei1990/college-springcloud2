package college.springcloud.student.dto;

import college.springcloud.common.dto.PageDto;
import lombok.Data;

/**
 * User: xuxianbei
 * Date: 2019/11/7
 * Time: 16:30
 * Version:V1.0
 */
@Data
public class StudentDto extends PageDto {
    private String name;
}
