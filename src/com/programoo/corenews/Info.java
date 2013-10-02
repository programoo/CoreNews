package com.programoo.corenews;

import java.util.ArrayList;

import org.jsoup.Jsoup;

import android.R;
import android.util.Log;

public class Info
{
	public ArrayList<News> newsList;
	public ArrayList<Feeder> pList;
	public ArrayList<String> typeList;

	public static int SPEAK_AVAILABLE = 555;
	
	// singleton pattern
	private static Info instance = null;
	
	private Info()
	{
		newsList = new ArrayList<News>();
		pList = new ArrayList<Feeder>();
		typeList = new ArrayList<String>();
		
		//add provider
	
		uniqueAddProvider(new Feeder("technology", "http://www.blognone.com/atom.xml"));
		uniqueAddProvider(new Feeder("other", "http://www.thairath.co.th/rss/news.xml"));
		uniqueAddProvider(new Feeder("news", "http://www.komchadluek.net/rss/news_widget.xml"));
		uniqueAddProvider(new Feeder("kak", "http://www.matichon.co.th/rss/news_conspicuous.xml"));
		// http://www.krobkruakao.com/rss/News.rss
		// http://rssfeeds.sanook.com/rss/feeds/sanook/news.index.xml
		uniqueAddProvider(new Feeder("sanook", "http://rssfeeds.sanook.com/rss/feeds/sanook/news.index.xml"));
	}
	
	public static Info getInstance()
	{
		if (instance == null)
		{
			instance = new Info();
		}
		return instance;
	}
	
	public void uniqueAdd(News n)
	{
		for (int i = 0; i < this.newsList.size(); i++)
		{
			if (this.newsList.get(i).link.equalsIgnoreCase(n.link))
				return;
		}
		this.newsList.add(n);
	}
	
	public News getNewsByLink(String link)
	{
		for (int i = 0; i < this.newsList.size(); i++)
		{
			if (this.newsList.get(i).link.equals(link))
			{
				return this.newsList.get(i);
			}
		}
		return null;
	}
	
	public static void longInfo(String tag, String str)
	{
		if (str.length() > 4000)
		{
			Log.i(tag, str.substring(0, 4000));
			longInfo(tag, str.substring(4000));
		} else
			Log.i(tag, str);
	}
	
	public void sortNewsList()
	{
		for (int i = 0; i < this.newsList.size(); i++)
		{
			for (int j = i; j < this.newsList.size() - 1; j++)
			{
				News jA = this.newsList.get(j);
				News jB = this.newsList.get(j + 1);
				if (jB.unixTime > jA.unixTime)
				{
					this.newsList.set(j + 1, jA);
					this.newsList.set(j, jB);
				}
			}
		}
	}
	
	public static String html2text(String html)
	{
		return Jsoup.parse(html).text();
	}
	
	public void uniqueAddProvider(Feeder pv){
		
		//add to type list first
		boolean newType = true;
		for(int i=0;i<typeList.size();i++){
			if(typeList.get(i).equalsIgnoreCase(pv.type)){
				newType = false;
				break;
			}
		}
		
		if(newType){
			typeList.add(pv.type);
		}
		
		for(int i=0;i<pList.size();i++){
			if(pList.get(i).id.equalsIgnoreCase(pv.id)){
				return;
			}
		}
		pList.add(pv);
	}
		
}
