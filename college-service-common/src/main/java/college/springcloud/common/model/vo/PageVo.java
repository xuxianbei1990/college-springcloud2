package college.springcloud.common.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * User: xuxianbei
 * Date: 2019/8/30
 * Time: 11:46
 * Version:V1.0
 */
@Data
public class PageVo<T> implements Serializable {

    private static final long serialVersionUID = -6641453923168236929L;

    /**
     * 总记录数
     */
    @ApiModelProperty(value = "总记录数")
    private Integer totalCount;

    /**
     * 页码
     */
    @ApiModelProperty(value = "页码")
    private Integer currentPage;

    /**
     * 页容量
     */
    @ApiModelProperty(value = "页容量")
    private Integer pageSize;

    /**
     * 分页数据
     */
    @ApiModelProperty(value = "分页数据")
    private List<T> list;


    public PageVo(Integer totalCount, Integer currentPage, Integer pageSize, List<T> list) {
        this.totalCount = totalCount;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.list = list;
    }
}
