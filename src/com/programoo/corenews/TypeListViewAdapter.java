package com.programoo.corenews;

import object.Kind;
import object.SArrayList;
import object.SObject;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.programoo.snews.R;

public class TypeListViewAdapter extends BaseAdapter implements OnClickListener, OnCheckedChangeListener
{
	private String TAG = getClass().getSimpleName();
	private Context context;
	private SArrayList typeList;
	private LayoutInflater inflater = null;
	
	public TypeListViewAdapter(Context context, SArrayList typeList)
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
		Kind typeObj = (Kind) this.typeList.get(position);
		
		CheckBox cb = (CheckBox) convertView.findViewById(R.id.providerCb);
		cb.setText(typeObj.type);
		cb.setTag(typeObj);
		cb.setChecked(typeObj.isSelected);
		cb.setOnCheckedChangeListener(this);
		
		return convertView;
		
	}
	
	@Override
	public int getCount()
	{
		return this.typeList.size();
	}
	
	@Override
	public SObject getItem(int position)
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

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	{
		Kind kindObj = (Kind) buttonView.getTag();
		kindObj.isSelected = isChecked;
	}
	
}