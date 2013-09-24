package com.programoo.corenews;

public class News
{
	String provider;
	String link;
	String title;
	String description;
	String pubDate;
	String creator;
	// update later
	String imgUrl = "undefined";
	long unixTime = 0;
	
	public News(String provider, String link, String title, String description,
			String pubDate, String creator)
	{
		this.provider = provider;
		this.link = link;
		this.title = title;
		this.description = description;
		this.pubDate = pubDate;
		this.creator = creator;
	}
	
	public News clone(){
		News n = new News(this.provider,this.link,this.title,this.description,this.pubDate,this.creator);
		n.imgUrl = this.imgUrl;
		n.unixTime = this.unixTime;
		return n;
	}
}
