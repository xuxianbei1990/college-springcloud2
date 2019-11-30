package college.springcloud.common.db.provider;

import org.apache.ibatis.mapping.MappedStatement;
import org.springframework.util.Assert;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.Set;

/**
 * 批量更新
 * 性能上比foreach好
 *
 *   // mysql 5.7.27 i7-9700  3.0GHZ 16G内存
 *     //case when 66860 10000 记录
 * //    foreach  372784
 * User: xuxianbei
 * Date: 2019/10/9
 * Time: 20:51
 * Version:V1.0
 */
public class BatchUpdateProvider extends MapperTemplate {
    public BatchUpdateProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    //    使用是case when 方式更新的。
    public String updateBatchNotNullBySinglePrimaryKey(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);

        StringBuilder sql = new StringBuilder();
        sql.append("<bind name=\"listNotEmptyCheck\" value=\"@tk.mybatis.mapper.util.OGNL@notEmptyCollectionCheck(list, '" + ms.getId() + " 方法参数为空')\"/>");
        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));
        sql.append("<trim prefix=\"set\" suffixOverrides=\",\">");

        Set<EntityColumn> keyColumns = EntityHelper.getPKColumns(entityClass);
        Assert.isTrue(keyColumns.size() == 1, "批量更新主键目前只支持单主键更新");
        EntityColumn entityColumn = keyColumns.iterator().next();
        String primaryDBKey = entityColumn.getColumn();
        String entityPk = entityColumn.getEntityField().getName();
        //获取全部列
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        for (EntityColumn column : columnList) {
            if (!column.isId() && column.isUpdatable()) {
                sql.append("  <trim prefix=\"" + column.getColumn() + " =case\" suffix=\"end,\">");
                sql.append("    <foreach collection=\"list\" item=\"record\" index=\"index\">");
                sql.append("      <if test=\"record." + column.getEntityField().getName() + "!=null\">");
                sql.append("         when " + primaryDBKey + "= #{record." + entityPk + "} then #{record." + column.getEntityField().getName() + "}");
                sql.append("      </if>");
                sql.append("    </foreach>");
                sql.append("  </trim>");
            }
        }

        sql.append("</trim>");
        sql.append("WHERE");
        sql.append(" " + primaryDBKey + " IN ");
        sql.append("<trim prefix=\"(\" suffix=\")\">");
        sql.append("<foreach collection=\"list\" separator=\", \" item=\"record\" index=\"index\" >");
        sql.append("#{record." + entityPk + "}");
        sql.append("</foreach>");
        sql.append("</trim>");

        return sql.toString();
    }

    /**
     * 批量更新 foreach 实现
     *
     * @param statement
     * @return
     */
    @Deprecated
    public String batchUpdate(MappedStatement statement) {
        //1.创建StringBuilder用于拼接SQL语句的各个组成部分
        StringBuilder builder = new StringBuilder();
        //2.拼接foreach标签
        builder.append("<foreach collection=\"list\" item=\"record\" separator=\";\" >");
        //3.获取实体类对应的Class对象
        Class<?> entityClass = super.getEntityClass(statement);
        //4.获取实体类在数据库中对应的表名
        String tableName = super.tableName(entityClass);
        //5.生成update子句
        String updateClause = SqlHelper.updateTable(entityClass, tableName);
        builder.append(updateClause);
        builder.append("<set>");
        //6.获取所有字段信息
        Set<EntityColumn> columns = EntityHelper.getColumns(entityClass);
        String idColumn = null;
        String idHolder = null;
        for (EntityColumn entityColumn : columns) {
            boolean isPrimaryKey = entityColumn.isId();
            //7.判断当前字段是否为主键
            if (isPrimaryKey) {
                //8.缓存主键的字段名和字段值
                idColumn = entityColumn.getColumn();
                //※返回格式如:#{record.age,jdbcType=NUMERIC,typeHandler=MyTypeHandler}
                idHolder = entityColumn.getColumnHolder("record");
            } else {
                //9.使用非主键字段拼接SET子句
                String column = entityColumn.getColumn();
                String columnHolder = entityColumn.getColumnHolder("record");
                builder.append("      <if test=\"record." + entityColumn.getEntityField().getName() + "!=null\">");
                builder.append(column).append("=").append(columnHolder).append(",");
                builder.append("      </if>");
            }
        }
        builder.append("</set>");
        //10.使用前面缓存的主键名、主键值拼接where子句
        builder.append("where ").append(idColumn).append("=").append(idHolder);
        builder.append("</foreach>");
        //11.将拼接好的字符串返回
        String sql = builder.toString();
        return sql;
    }
}
