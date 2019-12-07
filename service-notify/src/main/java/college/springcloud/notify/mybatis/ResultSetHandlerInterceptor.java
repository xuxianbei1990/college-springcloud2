package college.springcloud.notify.mybatis;

import college.springcloud.common.utils.Reflection;
import college.springcloud.notify.mybatis.model.OrderAftersale;
import college.springcloud.notify.mybatis.model.UserAccount;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * User: xuxianbei
 * Date: 2019/11/29
 * Time: 16:48
 * Version:V1.0
 */
@Intercepts({@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})})
@Component
public class ResultSetHandlerInterceptor implements Interceptor {
    /**
     * 这个可以改为yml方式实现
     */
    private static String user_account = "t_bbc_user_account";
    private static String order_aftersale = "t_bbc_order_aftersale";


    /**
     * 拦截某些表的结果集，如果这些表某些字段发生变动则发送消息
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        ResultSetHandler setHandler = (ResultSetHandler) invocation.getTarget();
        MappedStatement mappedStatement = Reflection.getFieldValue(setHandler, "mappedStatement");
        BoundSql boundSql = Reflection.getFieldValue(setHandler, "boundSql");
        String sql = boundSql.getSql();
        if (sql.contains(order_aftersale)) {
            if (!CollectionUtils.isEmpty(mappedStatement.getResultMaps())) {
                Class<?> resultType = mappedStatement.getResultMaps().get(0).getType();
                if (resultType.isAssignableFrom(OrderAftersale.class)) {
                    //                getResult(invocation);
                    List<OrderAftersale> orderAftersaleList = (List<OrderAftersale>) invocation.proceed();
                    if (!CollectionUtils.isEmpty(orderAftersaleList)) {
                        //发送消息

                    }
                    return orderAftersaleList;
                }
            }
        } else if (sql.contains(user_account)) {
            if (!CollectionUtils.isEmpty(mappedStatement.getResultMaps())) {
                Class<?> resultType = mappedStatement.getResultMaps().get(0).getType();
                if (resultType.isAssignableFrom(OrderAftersale.class)) {
//                    getResult(invocation);
                    List<UserAccount> userAccounts = (List<UserAccount>) invocation.proceed();
                    if (!CollectionUtils.isEmpty(userAccounts)) {
                        //发送消息
                    }
                    return userAccounts;
                }
            }
        }
        return invocation.proceed();
    }

    /**
     * 这种写法会拦截原来的数据
     *
     * @param invocation
     * @throws SQLException
     */
    private void getResult(Invocation invocation) throws SQLException {
        Statement statement = (Statement) invocation.getArgs()[0];
        ResultSet resultSet = statement.getResultSet();
        if (resultSet != null) {
            ResultSetMetaData rsmd = resultSet.getMetaData();
            List<String> columnList = new ArrayList<>();
            for (int i = 1; i < rsmd.getColumnCount(); i++) {
                columnList.add(rsmd.getColumnName(i));
            }

            List<OrderAftersale> results = new ArrayList();
            //关键这行代码
            while (resultSet.next()) {
                JSONObject jsonObject = new JSONObject();
                for (String colName : columnList) {
                    jsonObject.put(colName, resultSet.getObject(colName));
                }
                OrderAftersale orderAftersale = jsonObject.toJavaObject(OrderAftersale.class);
                results.add(orderAftersale);
            }
            resultSet.first();
            System.out.println(results);
            //发送该消息
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

}
