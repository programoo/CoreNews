package com.programoo.corenews;

import object.Feeder;
import object.SArrayList;
import object.UFeeder;
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

public class FeederListViewAdapter extends BaseAdapter implements
		OnClickListener, OnCheckedChangeListener
{
	private String TAG = getClass().getSimpleName();
	private MainActivity mCtx;
	private SArrayList fList;
	private LayoutInflater inflater = null;
	
	public FeederListViewAdapter(Context context, SArrayList fList)
	{
		super();
		this.mCtx = (MainActivity) context;
		this.fList = fList;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			this.inflater = (LayoutInflater) mCtx
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			convertView = inflater.inflate(R.layout.provider_listview, parent,
					false);
			
		}
		
		UFeeder fdObj = (UFeeder) this.fList.get(position) ;
		CheckBox cb = (CheckBox) convertView.findViewById(R.id.providerCb);
		cb.setText(fdObj.name);
		cb.setTag(fdObj);
		cb.setChecked(fdObj.isSelected);
		cb.setOnCheckedChangeListener(this);
		
		return convertView;
	}
	
	@Override
	public int getCount()
	{
		return this.fList.size();
	}
	
	@Override
	public Feeder getItem(int position)
	{
		return (Feeder) this.fList.get(position);
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
		UFeeder fdObj = (UFeeder) buttonView.getTag();
		fdObj.isSelected = isChecked;
	}
	
}