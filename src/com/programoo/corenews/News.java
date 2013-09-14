package com.programoo.corenews;

import android.graphics.Bitmap;


public class News {
	String provider;
	String id;
	String time;
	String urlContent;
	String description;
	
	//update later
	String imgUrl="undefined";
	String title="undefined";
	Bitmap bm=null;
	public News(String provider,String id, String time,String urlContent,String description) {
		this.provider = provider;
		this.id = id;
		this.time = time;
		this.urlContent = urlContent;
		this.description = description;
	}

	@Override
	public String toString() {
		return id+","+time+","+description;
	}
}
