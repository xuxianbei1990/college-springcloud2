package college.springcloud.common.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * User: xuxianbei
 * Date: 2019/8/30
 * Time: 10:50
 * Version:V1.0
 */
public class PageDto extends BaseDto {
    private static final long serialVersionUID = 5926144719053691699L;

    /**
     * 页码
     */
    @ApiModelProperty(value = "页码")
    private Integer currentPage = 1;

    /**
     * 页容量
     */
    @ApiModelProperty(value = "页容量")
    private Integer pageSize = 20;

    public Integer getCurrentPage() {
        return this.currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        if (currentPage != null && currentPage.intValue() > 0) {
            this.currentPage = currentPage;
        }
    }

    public Integer getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(Integer pageSize) {
        if (pageSize == null || pageSize <= 0) {
            return;
        }
        this.pageSize = pageSize;
    }
}
