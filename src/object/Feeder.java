package object;

public class Feeder extends SObject
{
	public String type;
	public String url;
	public String name;
	public boolean isSelected;
	
	public Feeder(String type, String url, boolean isSelected)
	{
		this.type = type;
		this.url = url;
		// extract
		this.name = url.split("[.]")[1];
		this.id = name;
		this.value = name;
		this.isSelected = isSelected;
	}
}
