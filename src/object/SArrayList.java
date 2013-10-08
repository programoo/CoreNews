package object;

import java.util.ArrayList;

public class SArrayList extends ArrayList<SObject>
{
	private static final long serialVersionUID = 1L;

	public SArrayList()
	{
		
	}
	
	public void sortByPriority()
	{

		for(int i=0;i<size();i++){
			for(int j=0;j<size()-1;j++){
				SObject objA = get(j);
				SObject objB = get(j+1);
				
				if (objB.priority > objA.priority)
				{
					set(j + 1, objA);
					set(j, objB);
				}
			}
		}
		
	}
	
	@Override
	public boolean add(SObject object)
	{
		//unique add
		if( !contains(object) ){
			super.add(object);
		}
		return true;
	}
	
	@Override
	public boolean contains(Object object)
	{
		for(int i=0;i<size();i++){
			if( ((SObject) object).equals( get(i) )  ){
				return true;
			}
		}
		return false;
	}
	
}
