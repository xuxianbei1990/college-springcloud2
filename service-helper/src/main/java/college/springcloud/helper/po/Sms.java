package college.springcloud.helper.po;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sms")
public class Sms {
    /**
     * 短信平台url
     */
    private String url;
    /**
     * 短信平台接收的用户名参数名称
     */
    private String usernameKey;
    /**
     * 短信平台的登陆用户名
     */
    private String usernameValue;
    /**
     * 短信平台接收的密码参数名称
     */
    private String passwordKey;
    /**
     * 短信平台的登陆密码
     */
    private String passwordValue;
    /**
     * 短信平台接收的手机号参数名称
     */
    private String mobileKey;
    /**
     * 短信平台接收的内容参数名称
     */
    private String contentKey;
    /**
     * 短信平台接收的发送时间参数名称
     */
    private String sendTimeKey;
    /**
     * 是否需要状态报告（默认false），选填
     */
    private String reportKey;
    /**
     * 返回状态参数名称，部分平台用到
     */
    private String needstatusKey;
    /**
     * 扩展参数的名称,部分平台就算用不到也要传这个参数过去
     */
    private String extendkey;


    public String getReportKey() {
        return reportKey;
    }
    public void setReportKey(String reportKey) {
        this.reportKey = reportKey;
    }
    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }
    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }
    /**
     * @return the usernameKey
     */
    public String getUsernameKey() {
        return usernameKey;
    }
    /**
     * @param usernameKey the usernameKey to set
     */
    public void setUsernameKey(String usernameKey) {
        this.usernameKey = usernameKey;
    }
    /**
     * @return the usernameValue
     */
    public String getUsernameValue() {
        return usernameValue;
    }
    /**
     * @param usernameValue the usernameValue to set
     */
    public void setUsernameValue(String usernameValue) {
        this.usernameValue = usernameValue;
    }
    /**
     * @return the passwordKey
     */
    public String getPasswordKey() {
        return passwordKey;
    }
    /**
     * @param passwordKey the passwordKey to set
     */
    public void setPasswordKey(String passwordKey) {
        this.passwordKey = passwordKey;
    }
    /**
     * @return the passwordValue
     */
    public String getPasswordValue() {
        return passwordValue;
    }
    /**
     * @param passwordValue the passwordValue to set
     */
    public void setPasswordValue(String passwordValue) {
        this.passwordValue = passwordValue;
    }
    /**
     * @return the mobileKey
     */
    public String getMobileKey() {
        return mobileKey;
    }
    /**
     * @param mobileKey the mobileKey to set
     */
    public void setMobileKey(String mobileKey) {
        this.mobileKey = mobileKey;
    }
    /**
     * @return the contentKey
     */
    public String getContentKey() {
        return contentKey;
    }
    /**
     * @param contentKey the contentKey to set
     */
    public void setContentKey(String contentKey) {
        this.contentKey = contentKey;
    }
    /**
     * @return the sendTimeKey
     */
    public String getSendTimeKey() {
        return sendTimeKey;
    }
    /**
     * @param sendTimeKey the sendTimeKey to set
     */
    public void setSendTimeKey(String sendTimeKey) {
        this.sendTimeKey = sendTimeKey;
    }
    /**
     * @return the extendkey
     */
    public String getExtendkey() {
        return extendkey;
    }
    /**
     * @param extendkey the extendkey to set
     */
    public void setExtendkey(String extendkey) {
        this.extendkey = extendkey;
    }
    /**
     * @return the needstatusKey
     */
    public String getNeedstatusKey() {
        return needstatusKey;
    }
    /**
     * @param needstatusKey the needstatusKey to set
     */
    public void setNeedstatusKey(String needstatusKey) {
        this.needstatusKey = needstatusKey;
    }
}
