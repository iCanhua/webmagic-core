package com.scut.fan.javamail;

import java.util.Date;

public interface EmailInfoFormat {
	public String getSubject();
	
	public Date getSendDate();
	
	public String getFrom();
	
	public String getTo() ;
	
	public String getContent();
	

}
