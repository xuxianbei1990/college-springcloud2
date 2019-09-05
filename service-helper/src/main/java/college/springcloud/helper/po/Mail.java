package college.springcloud.helper.po;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mail")
public class Mail {
	/**
	 * 邮箱账号
	 */
	private String mailAccount;
	/**
	 * 邮箱服务器地址
	 */
	private String serverAddress;
	/**
	 * 邮箱密码
	 */
	private String mailPassword;
	/**
	 * 收件地址
	 */
	private String recipient;
	
	public String getMailAccount() {
		return mailAccount;
	}

	public void setMailAccount(String mailAccount) {
		this.mailAccount = mailAccount;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public String getMailPassword() {
		return mailPassword;
	}

	public void setMailPassword(String mailPassword) {
		this.mailPassword = mailPassword;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	
	
}
