package com.programoo.corenews;


public class News {
	String provider;
	String id;
	String showtime;
	String urlContent;
	String description;

	// update later
	String imgUrl = "undefined";
	String title = "undefined";
	long unixTime = 0;
	long stampTime = 0;

	public News(String provider, String id, String time, String urlContent,
			String description) {
		this.provider = provider;
		this.id = id;
		this.showtime = time;
		this.urlContent = urlContent;
		this.description = description;
	}
	
	@Override
	public String toString() {
		return id + "," + showtime + "," + description;
	}
}
