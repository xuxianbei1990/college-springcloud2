package college.seata.rm.datasource.sql.struct.cache;

import college.seata.rm.datasource.sql.struct.*;
import college.springcloud.io.seata.core.exception.ShouldNeverHappenException;

import java.sql.*;

/**
 * @author: xuxianbei
 * Date: 2020/3/20
 * Time: 13:42
 * Version:V1.0
 */
public class MysqlTableMetaCache extends AbstractTableMetaCache {

    private static volatile TableMetaCache tableMetaCache = null;

    public static TableMetaCache getInstance() {
        if (tableMetaCache == null) {
            synchronized (MysqlTableMetaCache.class) {
                if (tableMetaCache == null) {
                    tableMetaCache = new MysqlTableMetaCache();
                }
            }
        }
        return tableMetaCache;
    }

    @Override
    protected String getCacheKey(Connection connection, String tableName, String resourceId) {
        return null;
    }

    @Override
    protected TableMeta fetchSchema(Connection connection, String tableName) throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement();
            StringBuilder builder = new StringBuilder("SELECT * FROM ");
            //难道原始的sql只认识 `forder_logistics_no` ？
            builder.append(tableName);
            builder.append(" LIMIT 1");
            rs = stmt.executeQuery(builder.toString());
            ResultSetMetaData rsmd = rs.getMetaData();
            DatabaseMetaData dbmd = connection.getMetaData();

            return resultSetMetaToSchema(rsmd, dbmd);
        } catch (Exception e) {
            if (e instanceof SQLException) {
                throw e;
            }
            throw new SQLException("Failed to fetch schema of " + tableName, e);

        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    private TableMeta resultSetMetaToSchema(ResultSetMetaData rsmd, DatabaseMetaData dbmd)
            throws SQLException {
        //always "" for mysql
        String schemaName = rsmd.getSchemaName(1);
        String catalogName = rsmd.getCatalogName(1);
        /*
         * use ResultSetMetaData to get the pure table name
         * can avoid the problem below
         *
         * select * from account_tbl
         * select * from account_TBL
         * select * from `account_tbl`
         * select * from account.account_tbl
         */
        String tableName = rsmd.getTableName(1);

        TableMeta tm = new TableMeta();
        tm.setTableName(tableName);

        /*
         * here has two different type to get the data
         * make sure the table name was right
         * 1. show full columns from xxx from xxx(normal)
         * 2. select xxx from xxx where catalog_name like ? and table_name like ?(informationSchema=true)
         */
        ResultSet rsColumns = dbmd.getColumns(catalogName, schemaName, tableName, "%");
        ResultSet rsIndex = dbmd.getIndexInfo(catalogName, schemaName, tableName, false, true);

        try {
            while (rsColumns.next()) {
                ColumnMeta col = new ColumnMeta();
                col.setTableCat(rsColumns.getString("TABLE_CAT"));
                col.setTableSchemaName(rsColumns.getString("TABLE_SCHEM"));
                col.setTableName(rsColumns.getString("TABLE_NAME"));
                col.setColumnName(rsColumns.getString("COLUMN_NAME"));
                col.setDataType(rsColumns.getInt("DATA_TYPE"));
                col.setDataTypeName(rsColumns.getString("TYPE_NAME"));
                col.setColumnSize(rsColumns.getInt("COLUMN_SIZE"));
                col.setDecimalDigits(rsColumns.getInt("DECIMAL_DIGITS"));
                col.setNumPrecRadix(rsColumns.getInt("NUM_PREC_RADIX"));
                col.setNullAble(rsColumns.getInt("NULLABLE"));
                col.setRemarks(rsColumns.getString("REMARKS"));
                col.setColumnDef(rsColumns.getString("COLUMN_DEF"));
                col.setSqlDataType(rsColumns.getInt("SQL_DATA_TYPE"));
                col.setSqlDatetimeSub(rsColumns.getInt("SQL_DATETIME_SUB"));
                col.setCharOctetLength(rsColumns.getInt("CHAR_OCTET_LENGTH"));
                col.setOrdinalPosition(rsColumns.getInt("ORDINAL_POSITION"));
                col.setIsNullAble(rsColumns.getString("IS_NULLABLE"));
                col.setIsAutoincrement(rsColumns.getString("IS_AUTOINCREMENT"));

                tm.getAllColumns().put(col.getColumnName(), col);
            }

            while (rsIndex.next()) {
                String indexName = rsIndex.getString("INDEX_NAME");
                String colName = rsIndex.getString("COLUMN_NAME");
                ColumnMeta col = tm.getAllColumns().get(colName);

                if (tm.getAllIndexes().containsKey(indexName)) {
                    IndexMeta index = tm.getAllIndexes().get(indexName);
                    index.getValues().add(col);
                } else {
                    IndexMeta index = new IndexMeta();
                    index.setIndexName(indexName);
                    index.setNonUnique(rsIndex.getBoolean("NON_UNIQUE"));
                    index.setIndexQualifier(rsIndex.getString("INDEX_QUALIFIER"));
                    index.setIndexName(rsIndex.getString("INDEX_NAME"));
                    index.setType(rsIndex.getShort("TYPE"));
                    index.setOrdinalPosition(rsIndex.getShort("ORDINAL_POSITION"));
                    index.setAscOrDesc(rsIndex.getString("ASC_OR_DESC"));
                    index.setCardinality(rsIndex.getInt("CARDINALITY"));
                    index.getValues().add(col);
                    if ("PRIMARY".equalsIgnoreCase(indexName)) {
                        index.setIndextype(IndexType.PRIMARY);
                    } else if (!index.isNonUnique()) {
                        index.setIndextype(IndexType.Unique);
                    } else {
                        index.setIndextype(IndexType.Normal);
                    }
                    tm.getAllIndexes().put(indexName, index);

                }
            }
            if (tm.getAllIndexes().isEmpty()) {
                throw new ShouldNeverHappenException("Could not found any index in the table: " + tableName);
            }
        } finally {
            if (rsColumns != null) {
                rsColumns.close();
            }
            if (rsIndex != null) {
                rsIndex.close();
            }
        }
        return tm;
    }

    @Override
    public void refresh(Connection connection, String resourceId) {

    }
}
