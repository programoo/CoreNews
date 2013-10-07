package object;
import com.google.gson.Gson;

public class SObject
{
	public String id;
	public String value;
	public int priority;
	public SObject()
	{
		this.id = "0";
		this.value="";
		this.priority = 0;
	}
	
	public String toString()
	{
		Gson gson = new Gson();
		String json = gson.toJson(this);
		return json;
	}
	@Override
	public int hashCode()
	{
		return this.id.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof SObject && ((SObject) obj).id.equals(this.id) ;
	}
	
}
