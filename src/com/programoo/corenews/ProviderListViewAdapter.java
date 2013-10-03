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

public class ProviderListViewAdapter extends BaseAdapter implements OnClickListener
{
	private String TAG = getClass().getSimpleName();
	private Context context;
	private ArrayList<Feeder> pList;
	private LayoutInflater inflater = null;
	//private Typeface tf = null;
	
	public ProviderListViewAdapter(Context context, ArrayList<Feeder> pList)
	{
		super();
		Log.i(TAG,"ProviderListViewAdapter");
		this.context = context;
		this.pList = pList;
		/*this.tf = Typeface.createFromAsset(this.context.getAssets(),
				"fonts/DroidSerif-Regular.ttf");
		Log.d(TAG, "FeedListViewAdapter");
		*/
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			this.inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			convertView = inflater.inflate(R.layout.provider_listview,
					parent, false);
			
		}
		
		CheckBox cb = (CheckBox) convertView.findViewById(R.id.providerCb);
		cb.setText(this.pList.get(position).name);
		if(this.pList.get(position).isSelected){
			cb.setChecked(true);
		}
		
		
		return convertView;
		
	}
	
	@Override
	public int getCount()
	{
		return this.pList.size();
	}
	
	@Override
	public Feeder getItem(int position)
	{
		return this.pList.get(position);
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