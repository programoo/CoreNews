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
import android.widget.Spinner;
import android.widget.TextView;

import com.programoo.snews.R;

public class SettingFragment extends Fragment implements OnItemSelectedListener
{
	private String TAG = this.getClass().getSimpleName();
	private View newsFragment;
	FeedListViewAdapter ardap;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		this.newsFragment = inflater.inflate(R.layout.settings_fragment,
				container, false);
		Log.d(TAG, "onCreateView");
		
		Spinner spinner = (Spinner) this.newsFragment
				.findViewById(R.id.typeSelectSpn);
		ArrayList<String> a = new ArrayList<String>();
		a.add("first");
		a.add("เพิ่มประเภทข่าว");
		spinner.setAdapter(new SettingsFragmentSpinnerAdapter(getActivity(),
				R.layout.settings_spinerview_fragment, a));
		spinner.setOnItemSelectedListener(this);

		return newsFragment;
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
