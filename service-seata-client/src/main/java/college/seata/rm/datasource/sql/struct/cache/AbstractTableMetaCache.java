package college.seata.rm.datasource.sql.struct.cache;

import college.seata.rm.datasource.sql.struct.TableMeta;
import college.seata.rm.datasource.sql.struct.TableMetaCache;
import college.springcloud.io.seata.core.context.RootContext;
import college.springcloud.io.seata.core.exception.ShouldNeverHappenException;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

/**
 * @author: xuxianbei
 * Date: 2020/3/20
 * Time: 13:43
 * Version:V1.0
 */
@Slf4j
public abstract class AbstractTableMetaCache implements TableMetaCache {


    private static final long CACHE_SIZE = 100000;

    private static final long EXPIRE_TIME = 900 * 1000;

    private static final Cache<String, TableMeta> TABLE_META_CACHE = Caffeine.newBuilder().maximumSize(CACHE_SIZE)
            .expireAfterWrite(EXPIRE_TIME, TimeUnit.MILLISECONDS).softValues().build();


    @Override
    public TableMeta getTableMeta(final Connection connection, final String tableName, String resourceId) {
        if (StringUtils.isBlank(tableName)) {
            throw new IllegalArgumentException("TableMeta cannot be fetched without tableName");
        }

        TableMeta tmeta;
        final String key = getCacheKey(connection, tableName, resourceId);
        tmeta = TABLE_META_CACHE.get(key, mappingFunction -> {
            try {
                return fetchSchema(connection, tableName);
            } catch (SQLException e) {
                log.error("get cache error:{}", e.getMessage(), e);
                return null;
            }
        });

        if (tmeta == null) {
            try {
                tmeta = fetchSchema(connection, tableName);
            } catch (SQLException e) {
                log.error("get table meta error:{}", e.getMessage(), e);
            }
        }

        if (tmeta == null) {
            throw new ShouldNeverHappenException(String.format("[xid:%s]get tablemeta failed", RootContext.getXID()));
        }
        return tmeta;
    }

    protected abstract String getCacheKey(Connection connection, String tableName, String resourceId);

    protected abstract TableMeta fetchSchema(Connection connection, String tableName) throws SQLException;
}
