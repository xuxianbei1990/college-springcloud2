package college.springcloud.student.po;

import college.springcloud.common.serialize.InNumberMultiply100JS;
import college.springcloud.common.serialize.NumberJsonSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author: xuxianbei
 * Date: 2020/4/17
 * Time: 17:42
 * Version:V1.0
 */
@Data
public class StudentSerialize implements Serializable {

    private static final long serialVersionUID = 2410580602885738179L;
    @JsonDeserialize(using = InNumberMultiply100JS.class)
    private Integer cash;

    @JsonSerialize(using = NumberJsonSerializer.class)
    private Integer cashJs;

    private Integer nullTest;

    private List<Integer> listNull;

    @JsonFormat(pattern = "YYYY-MM-dd")
    private Date modifyDate;
}
