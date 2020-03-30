package college.springcloud.io.seata.core.protocol.transaction;

/**
 * @author: xuxianbei
 * Date: 2020/3/27
 * Time: 15:27
 * Version:V1.0
 */
public class UndoLogDeleteRequest {
    public static final short DEFAULT_SAVE_DAYS = 7;

    private String resourceId;

    private short saveDays = DEFAULT_SAVE_DAYS;

    public String getResourceId() {
        return resourceId;
    }

    public short getSaveDays() {
        return saveDays;
    }
}
