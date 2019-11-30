package college.springcloud.common.db;


import tk.mybatis.mapper.version.NextVersion;
import tk.mybatis.mapper.version.VersionException;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author pengaoluo
 * @version 1.0.0
 */
public class XyNextVersion implements NextVersion {

    @Override
    public Object nextVersion(Object current) throws VersionException {
        if (current == null) {
            throw new VersionException("当前版本号为空!");
        }
        if (current instanceof Integer) {
            return (Integer) current + 1;
        } else if (current instanceof Long) {
            return (Long) current + 1L;
        } else if (current instanceof Timestamp) {
            return new Timestamp(System.currentTimeMillis());
        } else if (current instanceof Date) {
            return new Date();
        } else {
            throw new VersionException("XyNextVersion 只支持 Integer, Long, java.sql.Timestamp,java.util.Date 类型的版本号，如果有需要请自行扩展!");
        }
    }

}
