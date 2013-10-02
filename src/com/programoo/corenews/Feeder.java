package com.programoo.corenews;

public class Feeder
{
	public String id;
	public String type;
	public String url;
	public String name;
	public Feeder(String type,String url)
	{
		this.type = type;
		this.url = url;
		//extract 
		this.name = url.split("[.]")[1];
		this.id = url;
	}
	
	
	
}
