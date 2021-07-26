package college.springcloud.student.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: xuxianbei
 * Date: 2021/7/26
 * Time: 15:28
 * Version:V1.0
 */
@Data
public class ExportMultyMergeVo {


    /**
     * 一级类
     */
    @Excel(name = "品类", width = 20, mergeVertical = true)
    private String firstClass;

    /**
     * 二级类
     */
    @Excel(name = "大类", width = 20, mergeRely = {0}, mergeVertical = true)
    private String secondClass;

    /**
     * 三级类
     */
    @Excel(name = "中类", width = 20, mergeRely = {0, 1}, mergeVertical = true)
    private String thirdClass;

    /**
     * 价格
     */
    @Excel(name = "价格", width = 20)
    private BigDecimal price;

    /**
     * 编码
     */
    @Excel(name = "编码", width = 20)
    private String code;
}
