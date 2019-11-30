package college.springcloud.student.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * User: xuxianbei
 * Date: 2019/9/20
 * Time: 13:42
 * Version:V1.0
 */
@Data
public class ExportVo {
    @Excel(name = "采购订单号", width = 20, mergeVertical = true)
    private String fsupplierOrderId;

    @Excel(name = "订单状态", width = 20, mergeRely = {0}, mergeVertical = true)
    private String fstatusCode;

    //根据第一列垂直合并
    @Excel(name = "售后状态", width = 20, mergeRely = {0}, mergeVertical = true)
    private String faftersaleStatusCode;

    @Excel(name = "应发供货总额", width = 20, mergeRely = {0}, mergeVertical = true)
    private BigDecimal fshouldAmount = BigDecimal.valueOf(1);

    private Integer fstatus;

    @Excel(name = "推送时间", width = 20, mergeRely = {0}, mergeVertical = true, format = "yyyy/MM/dd HH:mm:ss")
    private Date fcreateTime;

    @Excel(name = "商品名称", width = 20)
    private String fskuName;

    @Excel(name = "规格", width = 20)
    private String fbatchPackageVal = "xxb";

    @Excel(name = "商品货号", width = 20)
    private String fgoodsStoreCode = "xxb";

    @Excel(name = "批次号", width = 20)
    private String fbatchId;

    @Excel(name = "下单数量", width = 20)
    private Integer fskuNum = 0;

    @Excel(name = "供货单价", width = 20)
    private BigDecimal fskuPrice;

    /**
     * 售后状态1待客服审核 2待采购审核 3待仓库审核 4待财务审核 5已拒绝 6待退货 7待退款 8已成功 9已撤销
     */
    @ApiModelProperty(value = "售后状态1待客服审核 2待采购审核 3待仓库审核 4待财务审核 5已拒绝 6待退货 7待退款 8已成功 9已撤销", example = "售后状态1待客服审核 2待采购审核 3待仓库审核 4待财务审核 5已拒绝 6待退货 7待退款 8已成功 9已撤销")
    private Integer faftersaleStatus;

    public void setFstatus(Integer fstatus) {
        this.fstatus = fstatus;
    }

    public void setFaftersaleStatus(Integer faftersaleStatus) {
        this.faftersaleStatus = faftersaleStatus;
    }
}
