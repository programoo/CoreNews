package object;

public class Feeder extends SObject
{
	public String kind;
	public String url;
	public String name;
	public boolean isSelected;
	
	public Feeder(String kind, String url, boolean isSelected)
	{
		this.kind = kind; 
		this.url = url;
		// extract
		this.name = url.split("[.]")[1];
		this.id = url;
		this.value = name;
		this.isSelected = isSelected;
		this.value = this.url;
	}
}
