package college.seata.rm.datasource;

import college.seata.rm.DefaultResourceManager;
import college.springcloud.io.seata.core.model.BranchType;
import college.springcloud.io.seata.core.model.Resource;
import com.alibaba.druid.util.JdbcUtils;
import lombok.Data;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据源代理;
 * 获取数据库连接
 *
 * @author: xuxianbei
 * Date: 2020/3/11
 * Time: 20:26
 * Version:V1.0
 */
@Data
public class DataSourceProxy extends AbstractDataSourceProxy implements Resource {

    private static final String DEFAULT_RESOURCE_GROUP_ID = "DEFAULT";
    private String jdbcUrl;
    private String dbType;
    private String resourceGroupId;
    private static boolean ENABLE_TABLE_META_CHECKER_ENABLE = false;

    /**
     * Instantiates a new Abstract data source proxy.
     *
     * @param targetDataSource the target data source
     */
    public DataSourceProxy(DataSource targetDataSource) {
        this(targetDataSource, DEFAULT_RESOURCE_GROUP_ID);
    }

    public DataSourceProxy(DataSource targetDataSource, String resourceGroupId) {
        super(targetDataSource);
        init(targetDataSource, resourceGroupId);
    }

    private void init(DataSource dataSource, String resourceGroupId) {
        this.resourceGroupId = resourceGroupId;
        try (Connection connection = dataSource.getConnection()) {
            jdbcUrl = connection.getMetaData().getURL();
            dbType = JdbcUtils.getDbType(jdbcUrl, null);
        } catch (SQLException e) {
            throw new IllegalStateException("can not init dataSource", e);
        }
        DefaultResourceManager.get().registerResource(this);
        if (ENABLE_TABLE_META_CHECKER_ENABLE) {
//            tableMetaExcutor.scheduleAtFixedRate(() -> {
//                try (Connection connection = dataSource.getConnection()) {
//                    TableMetaCacheFactory.getTableMetaCache(DataSourceProxy.this.getDbType())
//                            .refresh(connection, DataSourceProxy.this.getResourceId());
//                } catch (Exception e) {
//                }
//            }, 0, TABLE_META_CHECKER_INTERVAL, TimeUnit.MILLISECONDS);
        }
    }

    public Connection getPlainConnection() throws SQLException {
        return targetDataSource.getConnection();
    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection targetConnection = targetDataSource.getConnection();
        return new ConnectionProxy(this, targetConnection);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        Connection targetConnection = targetDataSource.getConnection(username, password);
        return new ConnectionProxy(this, targetConnection);
    }

    @Override
    public String getResourceId() {
        if (jdbcUrl.contains("?")) {
            return jdbcUrl.substring(0, jdbcUrl.indexOf("?"));
        } else {
            return jdbcUrl;
        }
    }

    @Override
    public BranchType getBranchType() {
        return BranchType.AT;
    }
}
