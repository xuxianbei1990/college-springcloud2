package college.springcloud.helper.service.impl;


import college.springcloud.helper.po.Mail;
import college.springcloud.helper.service.SendEmail;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;


@Component(value="SendEmailImpl")
public class SendEmailImpl implements SendEmail {
	
	@Autowired
	private Mail mail;
	
	/**
	 * 邮箱发送
	 * 
	 * @param recipient
	 * @param code
	 * @throws Exception
	 */
	@Override
	public void sendCode(String serverAddress, String mailAccount, String mailPassword, String recipient, String code) throws Exception {

		Properties props = new Properties();
		// 不是接受邮件服务器需要验证
		props.setProperty("mail.smtp.auth", "true");
		// 添加传输协议
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.host", serverAddress);// 登录发送邮件服务器
		props.setProperty("mail.smtp.ssl.enable", "true");

		// 登录发送邮件服务器验证 每次都返回一个新的对象
		Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(mailAccount, mailPassword);
			}
		});
		session.setDebug(true);
		InternetAddress from = new InternetAddress(mailAccount);
		InternetAddress to = new InternetAddress(recipient);
		MimeMessage msg = new MimeMessage(session);
		// 邮件发件人
		msg.setFrom(from);
		msg.setSubject(MimeUtility.encodeText("行云全球汇"));
		// 邮件接收人
		msg.setRecipient(Message.RecipientType.TO, to);

		// 整个邮件的组成方式
		MimeMultipart msgMultipart = new MimeMultipart("mixed");// 邮件的组成方式
		msg.setContent(msgMultipart);

		MimeBodyPart content = new MimeBodyPart(); // 正文

		msgMultipart.addBodyPart(content);

		// 邮件正文 邮件正文之间的组成方式
		MimeMultipart bodyMultipart = new MimeMultipart("related");
		content.setContent(bodyMultipart);
		MimeBodyPart htmlPart = new MimeBodyPart();

		bodyMultipart.addBodyPart(htmlPart);
		
		
		htmlPart.setContent("<div>" + "<div >尊敬的用户：</div>" + "<p>您好!"
				+ "<p>您正在进行邮箱验证，请在验证码输入框中输入此次验证码：</p>"
				+ "<div><span style=\"color:#3c8bf5;padding:20px 0;font-size:18px;\">"
				+ code + "</span>  以完成验证。</div>"
				+ "<p>如非本人操作，请忽略此邮件，由此给您带来的不便请谅解！</p>"
				+ "<p>如有疑问请拔打：400-863-5878</p>"
				+ "<p>或登录行云全球汇官网（http://www.xyb2b.com）咨询客服</p>" + "<br/><br/>"
				+ "<p>行云全球汇</p>" + "<p>" + DateFormatUtils.format(new Date(), "yyyy-HH-dd hh:mm:ss") + "</p>"
				+ "</div>",
				"text/html;charset=utf-8");

		msg.saveChanges();

		Transport.send(msg);

	}

	@Override
	public void sendVerifyMail(String serverAddress, String mailAccount, String mailPassword, String recipient, String fmobile, Integer ftype) throws Exception {

		Properties props = new Properties();
		// 不是接受邮件服务器需要验证
		props.setProperty("mail.smtp.auth", "true");
		// 添加传输协议
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.host", serverAddress);// 登录发送邮件服务器
		props.setProperty("mail.smtp.ssl.enable", "true");

		// 登录发送邮件服务器验证 每次都返回一个新的对象
		Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(mailAccount, mailPassword);
			}
		});
		session.setDebug(true);
		InternetAddress from = new InternetAddress(mailAccount);
		InternetAddress to = new InternetAddress(recipient);
		MimeMessage msg = new MimeMessage(session);
		// 邮件发件人
		msg.setFrom(from);
		msg.setSubject(MimeUtility.encodeText("行云全球汇"));
		// 邮件接收人
		msg.setRecipient(Message.RecipientType.TO, to);

		// 整个邮件的组成方式
		MimeMultipart msgMultipart = new MimeMultipart("mixed");// 邮件的组成方式
		msg.setContent(msgMultipart);

		MimeBodyPart content = new MimeBodyPart(); // 正文

		msgMultipart.addBodyPart(content);

		// 邮件正文 邮件正文之间的组成方式
		MimeMultipart bodyMultipart = new MimeMultipart("related");
		content.setContent(bodyMultipart);
		MimeBodyPart htmlPart = new MimeBodyPart();

		bodyMultipart.addBodyPart(htmlPart);
		String context = "";
		if(ftype == 1){
			context = "<div style=\"width:460px;height:230px;\">" + "<div >客服消息提示：</div>"
					+ "<p>用户<div style=\"color:#3c8bf5;padding:20px 0;font-size:18px;\">" + fmobile +  "</div>" +"新提交了认证信息，请及时处理"
					 + "</div>";
		}else{
			context = "<div style=\"width:460px;height:230px;\">" + "<div >客服消息提示：</div>"
					+ "<p>用户<div style=\"color:#3c8bf5;padding:20px 0;font-size:18px;\">" + fmobile +  "</div>" +"修改了认证信息，请及时处理"
					 + "</div>";
		}
		
		
		htmlPart.setContent(context,"text/html;charset=utf-8");

		msg.saveChanges();

		Transport.send(msg);
		
	}
	
	@Override
	public void commonSendEmail(String content, String recipient) throws Exception {
		Properties props = new Properties();
		// 不是接受邮件服务器需要验证
		props.setProperty("mail.smtp.auth", "true");
		// 添加传输协议
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.host", mail.getServerAddress());// 登录发送邮件服务器
		props.setProperty("mail.smtp.ssl.enable", "true");

		// 登录发送邮件服务器验证 每次都返回一个新的对象
		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(mail.getMailAccount(), mail.getMailPassword());
			}
		});
		session.setDebug(true);
		InternetAddress from = new InternetAddress(mail.getMailAccount());
		InternetAddress to = new InternetAddress(recipient);
		MimeMessage msg = new MimeMessage(session);
		// 邮件发件人
		msg.setFrom(from);
		msg.setSubject(MimeUtility.encodeText("行云全球汇"));
		// 邮件接收人
		msg.setRecipient(Message.RecipientType.TO, to);

		// 整个邮件的组成方式
		MimeMultipart msgMultipart = new MimeMultipart("mixed");// 邮件的组成方式
		msg.setContent(msgMultipart);

		MimeBodyPart part = new MimeBodyPart(); // 正文

		msgMultipart.addBodyPart(part);

		// 邮件正文 邮件正文之间的组成方式
		MimeMultipart bodyMultipart = new MimeMultipart("related");
		part.setContent(bodyMultipart);
		MimeBodyPart htmlPart = new MimeBodyPart();

		bodyMultipart.addBodyPart(htmlPart);
		htmlPart.setContent(content, "text/html;charset=utf-8");

		msg.saveChanges();

		Transport.send(msg);
	}
	
    @Override
	public boolean sendMail(String receiver, String subject, String content) throws AddressException,MessagingException,
			UnsupportedEncodingException {
    	 // 参数修饰  
        if (content == null) {  
        	content = "";  
        }  
        if (subject == null) {  
            subject = "无主题";  
        }  
        // 创建Properties对象  
        Properties props = System.getProperties();  
        // 创建信件服务器  
        props.put("mail.smtp.host", mail.getServerAddress());  
        props.put("mail.smtp.auth", "true"); // 通过验证  
        // 得到默认的对话对象  
        Session session = Session.getDefaultInstance(props, null);  
        // 创建一个消息，并初始化该消息的各项元素  
        MimeMessage msg = new MimeMessage(session);  
        subject = MimeUtility.encodeText(subject);  
        msg.setFrom(new InternetAddress(subject + "<" + mail.getMailAccount() + ">"));  
        // 创建收件人列表  
        if (receiver != null && receiver.trim().length() > 0) {  
            String[] arr = receiver.split(",");  
            int receiverCount = arr.length;  
            if (receiverCount > 0) {  
                InternetAddress[] address = new InternetAddress[receiverCount];  
                for (int i = 0; i < receiverCount; i++) {  
                    address[i] = new InternetAddress(arr[i]);  
                }  
                msg.addRecipients(Message.RecipientType.TO, address);  
                msg.setSubject(subject);  
                // 后面的BodyPart将加入到此处创建的Multipart中  
                Multipart mp = new MimeMultipart();  
                // 设置邮件正文  
                //MimeMultipart类是一个容器类，包含MimeBodyPart类型的对象  
                Multipart mainPart = new MimeMultipart();  
                MimeBodyPart messageBodyPart = new MimeBodyPart();//创建一个包含HTML内容的MimeBodyPart  
                //设置HTML内容  
                messageBodyPart.setContent(content,"text/html; charset=utf-8");  
                mainPart.addBodyPart(messageBodyPart);
                //将MimeMultipart对象设置为邮件内容     
                msg.setContent(mainPart);  
                // 设置信件头的发送日期  
                msg.setSentDate(new Date());  
                msg.saveChanges();  
                // 发送信件  
                Transport transport = session.getTransport("smtp");  
                transport.connect(mail.getServerAddress(), mail.getMailAccount(), mail.getMailPassword());  
                transport.sendMessage(msg,  
                        msg.getRecipients(Message.RecipientType.TO));  
                transport.close();  
                return true;  
            } else {  
                System.out.println("None receiver!");  
                return false;  
            }  
        } else {  
            System.out.println("None receiver!");  
            return false;  
        }  
    }  
}
