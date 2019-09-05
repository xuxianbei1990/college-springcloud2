package college.springcloud.helper.controller;


import college.springcloud.common.enums.ResultStatus;
import college.springcloud.common.utils.Result;
import college.springcloud.helper.po.Mail;
import college.springcloud.helper.service.SendEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/** 
* @ClassName: MailController 
* @Description: 发送邮件
* @author X.W
* @date 2018年12月20日 上午11:50:36 
*  
*/
@RestController
@RequestMapping(value = "/mail")
public class MailController {

	private static Logger logger = LoggerFactory.getLogger(MailController.class);

	@Autowired
	private SendEmail sendEmail;
	@Autowired
	private Mail mail;

	/** 
	* @Title: sendMail 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param recipient 收件人
	* @param @param code  验证码
	* @param @return  参数说明 
	* @return ResponseEntity<?>    返回类型 
	* @author X.W
	* @throws 
	*/
	@PostMapping(value = "/sendMail")
	public Result<Boolean> sendMail(String recipient, String code) {// 收件人  验证码
		String serverAddress = mail.getServerAddress();
		String mailAccount = mail.getMailAccount();
		String mailPassword = mail.getMailPassword();
		try {
			sendEmail.sendCode(serverAddress, mailAccount, mailPassword, recipient,code);
		} catch (Exception e) {
			logger.error("发送验证码失败",e);
			return Result.failure(ResultStatus.NOT_IMPLEMENTED);
		}
		return Result.success();
	}
	
	
	/** 
	* @Title: verifyEmail 
	* @Description: 认证信息提示邮件
	* @param @param fmobile
	* @param @param ftype
	* @param @return  参数说明 
	* @return ResponseEntity<?>    返回类型 
	* @author X.W
	* @throws 
	*/
	@PostMapping("/verifyEmail")
	public Result<Boolean> verifyEmail(String fmobile, Integer ftype){
		String serverAddress = mail.getServerAddress();
		String mailAccount = mail.getMailAccount();
		String mailPassword = mail.getMailPassword();
		String recipient = mail.getRecipient();
		try {
			sendEmail.sendVerifyMail(serverAddress, mailAccount, mailPassword, recipient, fmobile, ftype);
		} catch (Exception e) {
			logger.error("发送认证邮件失败");
			return Result.failure(ResultStatus.NOT_IMPLEMENTED);
		}
		return Result.success();
	}
	
	
	/** 
	* @Title: commonSendEmail 
	* @Description: 通用的发送邮件接口
	* @param @param content
	* @param @param recipient
	* @param @return  参数说明 
	* @return ResponseEntity<?>    返回类型 
	* @author X.W
	* @throws 
	*/
	@PostMapping("/commonSendEmail")
	public Result<Boolean> commonSendEmail(String content, String recipient) {
		logger.info("发送邮件, 收件人: " + recipient);
		try {
			sendEmail.commonSendEmail(content, recipient);
		} catch (Exception e) {
			logger.error("邮件发送失败，收件人：" + recipient);
			logger.error(e.getMessage());
			return Result.failure(ResultStatus.NOT_IMPLEMENTED);
		}
		return Result.success();
	}
	
	
	/** 
	* @Title: batchSendEmail 
	* @Description: 批量发送邮件
	* @param @param receiver
	* @param @param subject
	* @param @param content
	* @param @return  参数说明 
	* @return ResponseEntity<?>    返回类型 
	* @author X.W
	* @throws 
	*/
	@PostMapping("/batchSendEmail")
	public Result<Boolean> batchSendEmail(String receiver, String subject, String content) {
		logger.info("发送邮件, 收件人: " + receiver);
		try {
			sendEmail.sendMail(receiver, subject, content);
		} catch (Exception e) {
			logger.error("邮件发送失败，收件人：" + receiver);
			logger.error(e.getMessage());
			return Result.failure(ResultStatus.NOT_IMPLEMENTED);
		}
		return Result.success();
	}
}
