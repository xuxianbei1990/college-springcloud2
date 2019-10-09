/**
 * 供应商采购单实现设计
 * User: xuxianbei
 * Date: 2019/9/30
 * Time: 17:12
 * Version:V1.0
 *
 * 杭州代码Code Review开会总结：
 *
 * 1、所有的类注释、方法注释、需要补齐完善
 * 2、代码格式化
 * 3、集合、字符串判断全部用工具类校验
 * 4、远程接口调用，严禁直接获取结果，必须先校验
 * 5、所有工具类构造私有化，类、方法注释补全
 * 6、删除业务层没有引用的成员属性，及无用的导包
 * 7、日期获取请用工具类
 * 8、金额计算请使用BigDecimal
 * 9、不要全部写一坨，代码注意空行
 * 10、业务层引用对象，请加上private，建议使用@Resource
 * 11、所有魔法字符抽取到成员位置private static final String
 * 12、单表增删改必须加上 @GlobalTransactional
 * 13、分页查询，总条数必须校验，如果count < 1 直接return
 * 14、灵活运用java8提供的stream精简代码，提高性能及可读性
 *
 * 严格按照阿里巴巴开发手册进行开发。
 * 比较好的案例参考：卢彭大佬 GoodsController GoodsService GoodsServiceImpl
 */
public class SupplierPurchaseOrderImpl {
    //或者说一个查询应该有哪些动作
    //一般有以下几个动作list/query  detail export
    /**
     * list 一般会有 init  setCondition  setCriteria executeCriteria structurePageVo  setData
     */

}
