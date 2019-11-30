package college.springcloud.notify.mybatis;

import college.springcloud.common.utils.Reflection;
import college.springcloud.notify.mybatis.model.OrderAftersale;
import college.springcloud.notify.mybatis.model.UserAccount;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;

import java.sql.Statement;
import java.util.Objects;
import java.util.Properties;

/**
 * User: xuxianbei
 * Date: 2019/11/29
 * Time: 16:48
 * Version:V1.0
 */
@Intercepts({@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})})
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
        ParameterHandler parameterHandler = Reflection.getFieldValue(setHandler, "parameterHandler");
        BoundSql boundSql = Reflection.getFieldValue(setHandler, "boundSql");
        String sql = boundSql.getSql();
        if (sql.contains(user_account)) {
            OrderAftersale orderAftersale = (OrderAftersale) parameterHandler.getParameterObject();
            if (Objects.nonNull(orderAftersale)) {
                System.out.println(orderAftersale);
            }
        } else if (sql.contains(order_aftersale)) {
            UserAccount userAccount = (UserAccount) parameterHandler.getParameterObject();
            if (Objects.nonNull(userAccount)) {
                System.out.println(userAccount);
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

}
