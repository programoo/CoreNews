package com.programoo.corenews;


public class News {
	String provider;
	String id;
	String pubDate;
	String urlContent;
	String description;
	
	//String title;
	String link;
	//String description;

	// update later
	String imgUrl = "undefined";
	String title = "undefined";
	long unixTime = 0;
	long stampTime = 0;

	public News(String provider,String title,String link,String description,String time,String reporter){
		this.provider = provider;
		this.title = title;
		this.link = link;
		this.id = link;//re-create later
		this.description = description;
	}
	
	public News(String provider, String id, String pubDate, String urlContent,
			String description) {
		this.provider = provider;
		this.id = id;
		this.pubDate = pubDate;
		this.urlContent = urlContent;
		this.description = description;
	}
	
	@Override
	public String toString() {
		return id + "," + pubDate + "," + description;
	}
}
