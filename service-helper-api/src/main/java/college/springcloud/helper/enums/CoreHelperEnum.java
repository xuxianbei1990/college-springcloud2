/**
 *
 */
package college.springcloud.helper.enums;


import college.springcloud.common.enums.IResultStatus;

/**
 * @author bond
 *
 */
public enum CoreHelperEnum implements IResultStatus {

    RECORD_NOT_EXIST("606", "记录不存在"),
    HELPER_CONNECT_FAILED("607", "Helper服务连接失败"),
    UPLOAD_FILE_FAILED("608", "上传文件失败"),
    DELETE_FILE_FAILED("609", "删除文件失败"),
    DOWNLOAD_FILE_FAILED("610", "下载文件失败"),
    ;
    private String code;
    private String msg;

    CoreHelperEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

}
