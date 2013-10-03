package com.programoo.corenews;

public class Feeder
{
	public String id;
	public String type;
	public String url;
	public String name;
	boolean isSelected;
	public Feeder(String type,String url,boolean isSelected)
	{
		this.type = type;
		this.url = url;
		//extract 
		this.name = url.split("[.]")[1];
		this.id = url;
		this.isSelected=isSelected;
	}
	
	
	
}
