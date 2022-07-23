package college.springcloud.student.controller.pdf;

import lombok.Data;

import java.util.List;

/**
 * @author: xuxianbei
 * Date: 2021/9/16
 * Time: 10:47
 * Version:V1.0
 */
@Data
public class UploadDatasDto {

    /**
     * 批量合同
     */
   private List<UploadDataDto> uploadDataDtoList;
}
