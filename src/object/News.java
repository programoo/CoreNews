package object;

public class News extends SObject
{
	public String provider;
	public String link;
	public String title;
	public String description;
	public String pubDate;
	public String creator;
	// update later
	public String imgUrl = "undefined";
	public long unixTime = 0;
	public boolean isRead;
	
	public News(String provider, String link, String title, String description,
			String pubDate, String creator)
	{
		this.provider = provider;
		this.link = link;
		this.title = title;
		this.description = description;
		this.pubDate = pubDate;
		this.creator = creator;
		this.id = link;
		isRead = false;
	}
	
}
