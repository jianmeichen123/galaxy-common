package com.galaxyinternet.framework.core.utils.mail;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.galaxyinternet.framework.core.constants.Constants;
import com.galaxyinternet.framework.core.utils.PropertiesUtils;

/**
 * 发送邮件需要使用的基本信息 此处需要修改
 */
public class MailSenderInfo {
	
	 // 发送邮件的服务器的IP和端口
    private String mailServerHost = "mail.galaxyinternet.com"; // 默认服务器
    private String mailServerPort = "587";    
    // 邮件发送者的地址    
    // 邮件发送者的地址
    private String fromAddress = "fx-admin@galaxyinternet.com";
    // 邮件接收者的地址
    private String toAddress;
    // 登陆邮件发送服务器的用户名和密码
    private String userName = "fx-admin";
    private String password = "oN1oAyt4";
    // 是否需要身份验证
    private boolean validate = true;
	// 邮件主题
	private String subject;
	// 邮件的文本内容
	private String content;
	// 邮件附件的文件名
	private String[] attachFileNames;
	
	/**附件添加的组件**/
	private Multipart mp ;
	/**存放附件文件**/
	private List<FileDataSource> files = new LinkedList<FileDataSource>();

	/**
	 * 获得邮件会话属性
	 */
	public MailSenderInfo() {
	    Properties property = PropertiesUtils.getProperties(Constants.MAIL_CONFIG_FILE);
		mailServerHost = property.getProperty(Constants.MAIL_HOST_KEY);
		mailServerPort = property.getProperty(Constants.MAIL_SMTP_PORT_KEY);
		fromAddress = property.getProperty(Constants.MAIL_ADDRESS_KEY);
		userName = property.getProperty(Constants.MAIL_USERNAME_KEY);
	//	password = property.getProperty(Constants.MAIL_PASSWORD_KEY);
		password = "oN1oAyt4";
		validate = property.getProperty(Constants.MAIL_SMTP_AUTH_KEY).equals("true");
		mp = new MimeMultipart();
	}

	public Properties getProperties() {
		
		Properties p = new Properties();
		p.put("mail.transport.protocol", "smtp");//协议 
		p.put("mail.smtp.host", this.mailServerHost);
		p.put("mail.smtp.port", this.mailServerPort);
		p.put("mail.smtp.auth", validate ? "true" : "false");
		//以下配置支持tls
		p.put("mail.smtp.starttls.enable", "true");
		p.put("mail.smtp.ssl.checkserveridentity", "false"); 
		p.put("mail.smtp.ssl.trust", "mail.galaxyinternet.com");  

		return p;
	}

	
	/**
	 * 增加发送附件
	 * @param filename 邮件附件的地址，只能是本机地址而不能是网络地址，否则抛出异常
	 * @return
	 */
	public boolean addFileAffix(String filename) {
		try {
			BodyPart bp = new MimeBodyPart();
			FileDataSource fileds = new FileDataSource(filename);
			bp.setDataHandler(new DataHandler(fileds));
			/**解决附件名称乱码**/
			bp.setFileName(MimeUtility.encodeText(fileds.getName(), "UTF-8",null)); 
			/**添加附件**/
			mp.addBodyPart(bp);
			files.add(fileds);
			return true;
		} catch (Exception e) {
			System.err.println("增加邮件附件<" + filename + ">时发生错误：" + e);
			return false;
		}
		
	}
	public String getMailServerHost() {
		return mailServerHost;
	}

	public void setMailServerHost(String mailServerHost) {
		this.mailServerHost = mailServerHost;
	}

	public String getMailServerPort() {
		return mailServerPort;
	}

	public void setMailServerPort(String mailServerPort) {
		this.mailServerPort = mailServerPort;
	}

	public boolean isValidate() {
		return validate;
	}

	public void setValidate(boolean validate) {
		this.validate = validate;
	}

	public String[] getAttachFileNames() {
		return attachFileNames;
	}

	public void setAttachFileNames(String[] fileNames) {
		this.attachFileNames = fileNames;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String textContent) {
		this.content = textContent;
	}

}
