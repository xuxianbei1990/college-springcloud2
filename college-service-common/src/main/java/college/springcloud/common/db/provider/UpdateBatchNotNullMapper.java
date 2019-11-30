package college.springcloud.common.db.provider;

import org.apache.ibatis.annotations.UpdateProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;

/**
 * User: xuxianbei
 * Date: 2019/10/9
 * Time: 20:49
 * Version:V1.0
 */
@RegisterMapper
public interface UpdateBatchNotNullMapper<T> {

    /**
     * 批量更新通过单个主键更新
     *
     * @param recordList
     * @return
     */
    @UpdateProvider(type = BatchUpdateProvider.class, method = "dynamicSQL")
    int updateBatchNotNullBySinglePrimaryKey(List<? extends T> recordList);

    /**
     * 批量更新通过单个主键更新
     * 原因：上面的效率大约是这个5倍。测试数据10000订单
     * @param recordList
     * @return
     */
    @Deprecated
    @UpdateProvider(type = BatchUpdateProvider.class, method = "dynamicSQL")
    int batchUpdate(List<? extends T> recordList);
}
