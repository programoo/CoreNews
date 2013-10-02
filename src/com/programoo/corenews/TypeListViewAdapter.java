package com.programoo.corenews;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.programoo.snews.R;

public class TypeListViewAdapter extends BaseAdapter implements OnClickListener
{
	private String TAG = getClass().getSimpleName();
	private Context context;
	private ArrayList<String> typeList;
	private LayoutInflater inflater = null;
	
	public TypeListViewAdapter(Context context, ArrayList<String> typeList)
	{
		super();
		Log.i(TAG, "ProviderListViewAdapter");
		this.context = context;
		this.typeList = typeList;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			this.inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			convertView = inflater.inflate(R.layout.provider_listview, parent,
					false);
			
		}
		
		CheckBox cb = (CheckBox) convertView.findViewById(R.id.providerCb);
		cb.setText(this.typeList.get(position));
		
		return convertView;
		
	}
	
	@Override
	public int getCount()
	{
		return this.typeList.size();
	}
	
	@Override
	public String getItem(int position)
	{
		return this.typeList.get(position);
	}
	
	@Override
	public long getItemId(int position)
	{
		return 0;
	}
	
	@Override
	public void onClick(View v)
	{
	}
	
}