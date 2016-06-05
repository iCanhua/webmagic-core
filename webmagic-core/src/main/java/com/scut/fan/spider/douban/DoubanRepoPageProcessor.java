package com.scut.fan.spider.douban;
import java.security.Security;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;


public class DoubanRepoPageProcessor implements PageProcessor {
	private Site site = Site.me().setRetryTimes(3).setSleepTime(1500);
	@Override
	public void process(Page page) {
		
		//处理逻辑1，抽取所有市桥的帖子链接
		page.addTargetRequests(page.getHtml().links().regex("(https://www\\.douban\\.com/group/panyuzufang/discussion\\?start=\\d{1,2})").all());
		List<Selectable> records= page.getHtml().xpath("//div[@class='article']//table[@class='olt']/tbody/tr").nodes();
		int count=0;
		for(Selectable record: records){
			if(record.xpath("//tr/td[@class='title']/a/text()").regex("\\S*市桥\\S*").match()){
				page.addTargetRequests(record.links().regex("https://www\\.douban\\.com/group/topic/\\d+/").all());
				System.out.println("该页添加了一个正文链接进去:"+(++count));
				System.out.println(record.links().regex("https://www\\.douban\\.com/group/topic/\\d+/").all());
				//page.putField("该网站包含市桥的帖子"+records.indexOf(record), record.xpath("//td[@class='title']/a/text()").regex("\\S*市桥\\S*"));
			}
		}
		
		//处理逻辑2，
		boolean isUse=false;
		String time=page.getHtml().xpath("//div[@class='topic-content clearfix']/div[@class='topic-doc']/h3/span[@class='color-green']/text()").toString();
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		if(time!=null&&!"".equals(time)){
			try {
				Date date=sdf.parse(time);
				isUse=date.after(sdf.parse("2016-6-4 00:00:00"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(isUse){
			System.out.println("发现一个有用的链接");
			page.putField("topic", page.getHtml().xpath("//div[@id='wrapper']/div[@id='content']/h1/text()").toString());
			page.putField("time", time);
			page.putField("link", "<a href='" +page.getUrl()+"'>点击我可以查看具体信息哦</a>");
			page.putField("topic-content",page.getHtml().xpath("//div[@id='link-report']/div[@class='topic-content']/p/outerHtml()"));
		}
		if (isUse==false){
            //skip this page
            page.setSkip(true);
        }
	}

	@Override
	public Site getSite() {
		return site;
	}
	
	
	public static void main(String[] args) {
		System.out.println("Hello World！");
		Security.addProvider(new BouncyCastleProvider());
		EmailPipeline emailpipeline=new EmailPipeline();
		Spider.create(new DoubanRepoPageProcessor()).addUrl("https://www.douban.com/group/panyuzufang/discussion?start=0").addPipeline(emailpipeline).thread(1).run();
	}
}
