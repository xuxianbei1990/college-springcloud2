package college.springcloud.helper.service;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.io.UnsupportedEncodingException;


public interface SendEmail {
	/**
	 * 邮箱发送
	 * 
	 * @param recipient
	 * @param code
	 * @throws Exception
	 */
	public void sendCode(String serverAddress, String mailAccount, String mailPassword, String recipient, String code) throws Exception ;

	/** 
	* @Title: sendVerifyMail 
	* @Description: 认证提示邮件
	* @param serverAddress
	* @param mailAccount
	* @param mailPassword
	* @author duyy
	 * @param recipient 
	*/
	public void sendVerifyMail(String serverAddress, String mailAccount, String mailPassword, String recipient, String fmobile, Integer ftype) throws Exception;
	
	
	void commonSendEmail(String content, String recipient) throws Exception;

	boolean sendMail(String receiver, String subject, String content) throws AddressException,MessagingException,
			UnsupportedEncodingException;
}
