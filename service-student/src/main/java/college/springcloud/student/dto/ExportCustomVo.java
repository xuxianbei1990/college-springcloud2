package college.springcloud.student.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author: xuxianbei
 * Date: 2021/7/26
 * Time: 15:28
 * Version:V1.0
 */
@Data
public class ExportCustomVo {


    /**
     * 一级类
     */
    @Excel(name = "品类")
    private String firstClass;

    /**
     * 二级类
     */
    @Excel(name = "大类")
    private String secondClass;

    /**
     * 三级类
     */
    @Excel(name = "中类")
    private String thirdClass;

    /**
     * 价格
     */
    @Excel(name = "价格")
    private BigDecimal price;

    /**
     * 编码
     */
    @Excel(name = "编码")
    private String code;

    /**
     * 时间
     */
    @Excel(name = "时间Date", format ="yyyy/MM/dd HH:mm")
    private Date createTime;


    /**
     * 时间LocalDateTime
     */
    @Excel(name = "时间LocalDateTime", format ="yyyy/MM/dd HH:mm")
    private LocalDateTime createTime2;
}
