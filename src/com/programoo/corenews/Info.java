package com.programoo.corenews;

import java.util.ArrayList;
import android.util.Log;

public class Info {
	public static ArrayList<News> newsList = new ArrayList<News>();
	
	public static void uniqueAdd(News n){
		for(int i=0;i<Info.newsList.size();i++){
			if(Info.newsList.get(i).id.equalsIgnoreCase(n.id)) return;
		}
		Info.newsList.add(n);
	}
	
	public static void longInfo(String tag,String str) {
	    if(str.length() > 4000) {
	        Log.i(tag,str.substring(0, 4000));
	        longInfo(tag,str.substring(4000));
	    } else
	        Log.i(tag,str);
	}
	
	public static void sortNewsList(){
		for(int i=0;i<Info.newsList.size();i++){
			for(int j=i;j<Info.newsList.size()-1;j++){
				News jA = Info.newsList.get(j);
				News jB = Info.newsList.get(j+1);
				if(jB.unixTime > jA.unixTime){					
					Info.newsList.set(j+1,jA);
					Info.newsList.set(j,jB);
				}
			}
		}
	}
}
