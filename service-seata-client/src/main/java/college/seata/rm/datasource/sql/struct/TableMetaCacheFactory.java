/*
 *  Copyright 1999-2019 Seata.io Group.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package college.seata.rm.datasource.sql.struct;

import college.seata.rm.datasource.DataSourceProxy;
import college.seata.rm.datasource.sql.struct.cache.MysqlTableMetaCache;
import college.springcloud.io.seata.core.exception.NotSupportYetException;
import com.alibaba.druid.util.JdbcConstants;

/**
 * @author guoyao
 */
public class TableMetaCacheFactory {

    private TableMetaCacheFactory() {}

    public static TableMetaCache getTableMetaCache(DataSourceProxy dataSourceProxy) {
        return getTableMetaCache(dataSourceProxy.getDbType());
    }

    public static TableMetaCache getTableMetaCache(String dbType) {
        if (dbType.equals(JdbcConstants.MYSQL)) {
            return MysqlTableMetaCache.getInstance();
        } else {
            throw new NotSupportYetException("not support dbType[" + dbType + "]");
        }
    }
}
