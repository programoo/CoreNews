package com.programoo.corenews;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.programoo.snews.R;

public class SettingFragment extends Fragment implements OnItemSelectedListener
{
	private String TAG = this.getClass().getSimpleName();
	private View layout;
	FeedListViewAdapter ardap;
	private ListView providerLv;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//predefinde unique add provider
		//http://www.komchadluek.net/rss/news_widget.xml
		
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		this.layout = inflater.inflate(R.layout.settings_fragment,
				container, false);
		providerLv = (ListView) this.layout.findViewById(R.id.providerLv);
		ProviderListViewAdapter pAdapter = new ProviderListViewAdapter(getActivity(), Info.getInstance().pList);
		providerLv.setAdapter(pAdapter);
		
		
		Spinner spinner = (Spinner) this.layout
				.findViewById(R.id.typeSelectSpn);
		//spinner.setAdapter(new SettingsFragmentSpinnerAdapter(getActivity(),
		//		R.layout.settings_spinerview_fragment, a));
		//spinner.setOnItemSelectedListener(this);

		return layout;
	}
	
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id)
	{
		TextView tv = (TextView) view.findViewById(R.id.dataSpinnerTv);
		String itemSelect = (String) parent.getItemAtPosition(pos);
		Log.i(TAG,"You select: "+itemSelect);
		// An item was selected. You can retrieve the selected item using
		// parent.getItemAtPosition(pos)
	}
	
	public void onNothingSelected(AdapterView<?> parent)
	{
		// Another interface callback
	}
}
