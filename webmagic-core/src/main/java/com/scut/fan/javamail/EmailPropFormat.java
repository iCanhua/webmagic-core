package com.scut.fan.javamail;

public interface EmailPropFormat {
	
	public String getMailTransportProtocol();

	public String getMailSmtpHost(); 
	
	public String getMailSmtpPort();
	
	public String getSenderEmailPwd();
	
	public String getSenderEmail();
		
	public String isAuth();
	
}
