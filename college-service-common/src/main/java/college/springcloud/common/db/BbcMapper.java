package college.springcloud.common.db;


import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;

/**
 * User: xuxianbei
 * Date: 2019/8/27
 * Time: 20:02
 * Version:V1.0
 */
public interface BbcMapper<T> extends Mapper<T>, MySqlMapper<T> {

    //下面两个代码不知道干嘛用的
    @Select({"${sql}"})
    List<T> selectBySql(@Param("sql") String var1);

    @Select({"${sql}"})
    Integer countBySql(@Param("sql") String var1);
}
