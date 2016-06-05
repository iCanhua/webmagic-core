package com.scut.fan.javamail;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 * ����Ϊ�ʼ�����ʹ�ù��ߣ���װ��Ҫ���ʼ��������̣�ȫ����̬���룬���������ýӿ�Properable�����ݽӿ�EmailFormter
 * ���ģʽΪ����ģʽ��prop��ʵ�ֿ����ö��ֲ���
 * @author FAN
 *
 */
public class EmailUtils {
	/**
	 * propΪ�ʼ������������Խӿڣ�ֻ��ʵ�ָ�Э��������ܱ��ʼ���ȷʹ��
	 * ���ģʽΪ����ģʽ��prop��ʵ�ֿ����ö��ֲ���
	 */
	static EmailPropFormat prop;

	public static void setProp(EmailPropFormat propinstance) {
		prop=propinstance;
	}
	
	private static EmailPropFormat getProp() throws PropNullException {
		if(prop==null){
			throw new PropNullException();
		}else{
			return prop;
		}
	}
	
	/**
	 * ��ȡ�Ự��Ϣ���÷�������������porp����������
	 * @return
	 */
	private static Session getSession() {
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", prop.getMailTransportProtocol());
		props.setProperty("mail.smtp.host", prop.getMailSmtpHost());
		props.setProperty("mail.smtp.port", prop.getMailSmtpPort());
		props.setProperty("mail.smtp.auth", "true");
		
		Session session = Session.getInstance(props, new Authenticator() {
			@Override //内部类获取配置文件的密码
			protected PasswordAuthentication getPasswordAuthentication() {
				String from=null;
				try {
					from = EmailUtils.getProp().getSenderEmail();
				} catch (PropNullException e) {
					e.printStackTrace();
				}
				String password=null;
				try {
					password = EmailUtils.getProp().getSenderEmailPwd();
				} catch (PropNullException e) {
					e.printStackTrace();
				}
				
			return new PasswordAuthentication(from, password);
			}
			
		});
		return session;
	}
	
	/**
	 * EmialUtils�����ṩ�ӿڣ���װ�����ʼ�����
	 * @param emailprop �ʼ��ķ�����������
	 * @param eamilinfo �ʼ�����������Ϣ
	 * @return
	 * @throws Exception 
	 */
	public static boolean sendEmail(EmailInfoFormat emailinfo) throws Exception {
		if(prop==null){
			throw new PropNullException();
		}
		Session session = getSession();
		MimeMessage message = new MimeMessage(session);
		try {
			message.setSubject(emailinfo.getSubject());
			message.setSentDate(emailinfo.getSendDate());
			message.setFrom(new InternetAddress(emailinfo.getFrom()));
			message.setRecipient(RecipientType.TO, new InternetAddress(emailinfo.getTo()));
			message.setContent(emailinfo.getContent(),"text/html;charset=utf-8");
			Transport.send(message);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}


}
