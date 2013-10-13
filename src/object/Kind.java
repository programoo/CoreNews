package object;

public class Kind extends SObject
{
	public String type;
	public boolean isSelected;
	
	public Kind(String type,boolean isSelected)
	{
		this.id = type;
		
		this.type = type;
		this.isSelected = isSelected;
	}
}
