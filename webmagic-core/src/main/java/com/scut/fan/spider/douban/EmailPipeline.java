package com.scut.fan.spider.douban;

import java.util.Date;
import java.util.Map;

import com.scut.fan.javamail.AbstractEmailTemplate;
import com.scut.fan.javamail.EmailInfoFormat;
import com.scut.fan.javamail.EmailPropFormat;
import com.scut.fan.javamail.EmailPropModel;

import us.codecraft.webmagic.pipeline.*;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
/**
 * write result in Email
 * @author FAN
 *
 */
public class EmailPipeline extends AbstractEmailTemplate implements Pipeline {
	
	RomeRentInfo info=new RomeRentInfo();
	EmailPropModel prop =new EmailPropModel();

	@Override
	public EmailPropFormat initEmailProp() {
		// TODO Auto-generated method stub
		return prop;
	}

	@Override
	public EmailInfoFormat initEmailInfo() {
		return info;
	}

	@Override
	public void process(ResultItems resultItems, Task task) {
		// TODO Auto-generated method stub
		info.setSubject("给Soso的租房订阅");
		info.setFrom("fancanhuade@163.com");
		info.setTo("982898072@qq.com");
		info.setSendDate(new Date());
		String content="亲爱的Soso:<br><br> 我出生便将为你服务，我将会从该系统设计者收到指令后，为你监控所有豆瓣租房信息！需求请联系：525921307@qq.com"+"<br>"+"祝你每日开心愉快！"+"<br><br>";
		System.out.println("get page: " + resultItems.getRequest().getUrl());
	    for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
	        System.out.println(entry.getKey() + ":\t" + entry.getValue());
	        content=content+"<br>"+entry.getKey() + ":\t" + entry.getValue();
	        
	    }
		info.setContent(content);
	
		try {
			this.sendEmail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
