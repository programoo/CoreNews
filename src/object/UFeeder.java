package object;

public class UFeeder extends SObject
{
	public String name;
	public boolean isSelected;
	
	public UFeeder(String name,boolean isSelected)
	{
		this.id = name;
		this.name = name;
		this.isSelected = isSelected;
	}
}
