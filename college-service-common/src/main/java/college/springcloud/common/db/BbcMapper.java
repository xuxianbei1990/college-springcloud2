package college.springcloud.common.db;


import college.springcloud.common.db.provider.UpdateBatchNotNullMapper;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.insert.InsertListProvider;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;

/**
 * User: xuxianbei
 * Date: 2019/8/27
 * Time: 20:02
 * Version:V1.0
 */
public interface BbcMapper<T> extends UpdateBatchNotNullMapper<T>, Mapper<T>, MySqlMapper<T> {

    /**
     * 配合mybatis 自动驼峰你  mybatis: configuration: map-underscore-to-camel-case: true
     *
     * @param var1
     * @return
     */
    @Select({"${sql}"})
    List<T> selectBySql(@Param("sql") String var1);

    @Select({"${sql}"})
    Integer countBySql(@Param("sql") String var1);

    /**
     * 批量插入，支持批量插入的数据库可以使用，例如MySQL,H2等
     * <p>
     * 不支持主键策略，插入前需要设置好主键的值
     * <p>
     * 特别注意：2018-04-22 后，该方法支持 @KeySql 注解的 genId 方式
     *
     * @param recordList
     * @return
     */
    @InsertProvider(type = InsertListProvider.class, method = "dynamicSQL")
    int insertListNotNeedId(List<? extends T> recordList);
}
