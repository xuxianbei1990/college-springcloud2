package college.springcloud.common.utils.eexcel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * @author: xuxianbei
 * Date: 2021/10/25
 * Time: 17:30
 * Version:V1.0
 */
@Data
public class Student {

    /**
     * 花名
     */
    @Excel(name = "花名")
    private String name;

    /**
     * 品名品类
     */
    @Excel(name = "品名品类")
    private String mingping;

    /**
     * 品名品类
     */
    @Excel(name = "款式来源")
    private String styleSource;

    /**
     * 颜色
     */
    @Excel(name = "颜色")
    private String color;

    /**
     * 下单数量
     */
    @Excel(name = "下单数量")
    private String orderNum;

    /**
     * 总数
     */
    @Excel(name = "总数")
    private String sum;

    /**
     * 是否拍照
     */
    @Excel(name = "是否拍照")
    private String photograph;
}
