package college.springcloud.order.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * <p>
 * 
 * </p>
 *
 * @author xxb
 * @since 2022-04-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ad_items")
public class AdItems implements Serializable {

    private static final long serialVersionUID = 1L;

//    @TableId(value = "id", type = IdType.AUTO)
    @Id
    private Integer id;

    @Column(name = "name")
    private String name;

    /**
     * 分类：1，首页推广，2列表推广
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 展示商品（对应的skuId）
     */
    @Column(name = "sku_id")
    private String skuId;

    /**
     * 排序
     */
    @Column(name = "sort")
    private Integer sort;


}
