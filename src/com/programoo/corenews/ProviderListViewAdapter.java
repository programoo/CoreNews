package com.programoo.corenews;

import object.Feeder;
import object.SArrayList;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.programoo.snews.R;

public class ProviderListViewAdapter extends BaseAdapter implements
		OnClickListener, OnCheckedChangeListener
{
	private String TAG = getClass().getSimpleName();
	private Context context;
	private SArrayList fList;
	private LayoutInflater inflater = null;
	
	public ProviderListViewAdapter(Context context, SArrayList fList)
	{
		super();
		Log.i(TAG, "ProviderListViewAdapter");
		this.context = context;
		this.fList = fList;
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
		/*
		 * CheckBox cb = (CheckBox) convertView.findViewById(R.id.providerCb);
		 * //Feeder fdObj = (Feeder) this.fList.get(position); //
		 * /Log.i(TAG,"fdObj: "+position+"<>"+fdObj.toString());
		 * cb.setOnCheckedChangeListener(this); cb.setTag(fdObj);
		 * cb.setText(fdObj.value); if(fdObj.isSelected){ cb.setChecked(true); }
		 */
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
		Feeder fd = (Feeder) buttonView.getTag();
		fd.isSelected = isChecked;
		// Log.i(TAG,"change: "+isChecked+","+buttonView.getTag());
	}
	
}