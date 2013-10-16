package com.programoo.corenews;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import object.SArrayList;
import object.SObject;

import org.jsoup.Jsoup;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

public class Info
{
	public ArrayList<String> typeList;
	public ArrayList<String> isReadList;
	public TextView unReadCountTv;
	public static int SPEAK_AVAILABLE = 555;
	public Gson gson;
	// singleton pattern
	
	private static Info instance = null;
	
	private Info()
	{
		typeList = new ArrayList<String>();
		isReadList = new ArrayList<String>();
		unReadCountTv = null;
		gson = new Gson();
		// add provider
	}
	
	public void cleanCloseInfo()
	{
		Info.instance = null;
	}
	
	public static Info getInstance()
	{
		if (instance == null)
		{
			instance = new Info();
		}
		return instance;
	}
	
	public static void longInfo(String tag, String str)
	{
		if (str.length() > 4000)
		{
			Log.i(tag, str.substring(0, 4000));
			longInfo(tag, str.substring(4000));
		} else
			Log.i(tag, str);
	}
	
	public static String html2text(String html)
	{
		return Jsoup.parse(html).text();
	}
	
	public void writeFiles(Context ctx, String fileName, String data)
	{
		BufferedWriter bufferedWriter;
		try
		{
			bufferedWriter = new BufferedWriter(new FileWriter(new File(
					ctx.getFilesDir() + File.separator + fileName)));
			bufferedWriter.write(data);
			bufferedWriter.close();
			
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> readFile(Context ctx, String fileName)
	{
		ArrayList<String> fileToAl = new ArrayList<String>();
		// read file line by line
		BufferedReader bufferedReader;
		try
		{
			bufferedReader = new BufferedReader(new FileReader(new File(
					ctx.getFilesDir() + File.separator + fileName)));
			String temp = "undefined";
			while ((temp = bufferedReader.readLine()) != null)
			{
				fileToAl.add(temp);
			}
			bufferedReader.close();
			
		} catch (Exception e)
		{
			e.printStackTrace();
			return fileToAl;
			
		}
		return fileToAl;
		
	}
	
	public static String serializeFromArrayList(Context ctx, String fileName,
			SArrayList al, Class<?> classType)
	{
		String serializeData = "";
		for (int i = 0; i < al.size(); i++)
		{
			serializeData += al.get(i).toString() + "\n";
		}
		// write serializeData
		BufferedWriter bufferedWriter;
		try
		{
			bufferedWriter = new BufferedWriter(new FileWriter(new File(
					ctx.getFilesDir() + File.separator + fileName)));
			bufferedWriter.write(serializeData);
			bufferedWriter.close();
			
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return serializeData;
	}
	
	public static boolean deserializeToGenericArrayList(Context ctx,
			String fileName, SArrayList al, Class<?> classType)
	{
		
		BufferedReader bufferedReader;
		try
		{
			bufferedReader = new BufferedReader(new FileReader(new File(
					ctx.getFilesDir() + File.separator + fileName)));
			
			String temp = "";
			Gson gson = new Gson();
			while ((temp = bufferedReader.readLine()) != null)
			{
				SObject genericObj = (SObject) gson.fromJson(temp, classType);
				// unique add prevent some bug in future
				al.add(genericObj);
			}
			bufferedReader.close();
			return true;
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
}
