package com.programoo.corenews;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import object.News;
import object.SArrayList;

import org.apache.commons.lang3.StringEscapeUtils;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.XmlDom;
import com.programoo.snews.R;

public class FeedFragment extends Fragment implements OnItemClickListener,
		OnInitListener
{
	private String TAG = this.getClass().getSimpleName();
	private View newsFragment;
	FeedListViewAdapter ardap;
	private ListView lv;
	private AQuery aq;
	private SArrayList tempList;
	private ArrayList<String> imgUrlList1;
	private int imgLocationIdx;
	
	String blognone = "http://www.blognone.com/atom.xml";
	String thairath = "http://www.thairath.co.th/rss/news.xml";
	String dailynews = "http://www.dailynews.co.th/rss.xml";
	int fileSize = 0;
	
	private TextToSpeech mTts;
	private News currentShowOnDialogNew;
	private SocialAuthAdapter adapter;
	private MainActivity mCtx;
	
	private HashMap<String, Integer> imgIdxHM;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		aq = new AQuery(getActivity());
		mCtx = (MainActivity) getActivity();
		imgIdxHM = new HashMap<String, Integer>();
		
		imgLocationIdx = 0;
		// share button settings
		adapter = new SocialAuthAdapter(new ResponseListener());
		// Add providers
		adapter.addProvider(Provider.FACEBOOK, R.drawable.facebook);
		adapter.addProvider(Provider.TWITTER, R.drawable.twitter);
		// Providers require setting user call Back url
		adapter.addCallBack(Provider.TWITTER,
				"http://socialauth.in/socialauthdemo/socialAuthSuccessAction.do");
		
		// initialize predefind provider
		/*
		 * String[] mTestArray = getResources().getStringArray(
		 * R.array.rssProviderArray); for (int i = 0; i < 5; i++) {
		 * Info.getInstance().uniqueAddProvider( new
		 * Feeder(mTestArray[i].split(",")[0], mTestArray[i] .split(",")[1],
		 * false)); //(TAG, "Test array: " + mTestArray[i]); }
		 */
		
	}
	
	private final class ResponseListener implements DialogListener
	{
		@Override
		public void onComplete(Bundle values)
		{
			final String providerName = values
					.getString(SocialAuthAdapter.PROVIDER);
			Toast.makeText(getActivity(), providerName + " connected",
					Toast.LENGTH_LONG).show();
			
			final Dialog postDialog = new Dialog(getActivity());
			postDialog.getWindow();
			postDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			postDialog.setContentView(R.layout.insert_comment);
			postDialog.setCancelable(true);
			postDialog.show();
			//
			Button postBtn = (Button) postDialog
					.findViewById(R.id.postCommentBtn);
			
			final EditText postEdt = (EditText) postDialog
					.findViewById(R.id.postCommentEdt);
			
			postBtn.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					postDialog.dismiss();
					new uploadImgBgTask().execute(postEdt.getText().toString());
					
				}
			});
			
		}
		
		@Override
		public void onError(SocialAuthError error)
		{
			// d("ShareButton", "Authentication Error: " + error.getMessage());
		}
		
		@Override
		public void onCancel()
		{
			// d("ShareButton", "Authentication Cancelled");
		}
		
		@Override
		public void onBack()
		{
			// d("Share-Button", "Dialog Closed by pressing Back Key");
		}
		
	}
	
	private class uploadImgBgTask extends AsyncTask<String, String, String>
	{
		
		@Override
		protected String doInBackground(String... params)
		{
			String userComment = params[0];
			String message = userComment
					+ "\n\n"
					+ currentShowOnDialogNew.title
					+ "\n"
					+ Info.html2text(currentShowOnDialogNew.description)
					+ "\n"
					+ getString(R.string.continue_read_text)
					+ ": "
					+ currentShowOnDialogNew.link
					+ "\n"
					+ getString(R.string.credit_text)
					+ ": https://play.google.com/store/apps/details?id=com.prakarn.loopwalker";
			try
			{
				Bitmap preset = aq
						.getCachedImage(currentShowOnDialogNew.imgUrl);
				adapter.uploadImage(message, currentShowOnDialogNew.imgUrl,
						preset, 100);
			} catch (Exception e)
			{
				// if can't find image post only text
				adapter.updateStatus(message, new MessageListener(), true);
				e.printStackTrace();
				return null;
			}
			return "ok";
		}
		
		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);
			// d(TAG, "image posted complete");
			Toast.makeText(getActivity(), "Message posted complete ",
					Toast.LENGTH_LONG).show();
		}
		
	}
	
	private final class MessageListener implements SocialAuthListener<Integer>
	{
		@Override
		public void onExecute(String provider, Integer t)
		{
			Integer status = t;
			if (status.intValue() == 200 || status.intValue() == 201
					|| status.intValue() == 204)
				Toast.makeText(getActivity(), "Message posted on " + provider,
						Toast.LENGTH_LONG).show();
			else
				Toast.makeText(getActivity(),
						"Message not posted on " + provider, Toast.LENGTH_LONG)
						.show();
		}
		
		@Override
		public void onError(SocialAuthError e)
		{
			
		}
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		this.newsFragment = inflater.inflate(R.layout.news_fragment, container,
				false);
		lv = (ListView) newsFragment.findViewById(R.id.list1Fragment);
		ardap = new FeedListViewAdapter(getActivity(), mCtx.newsList, aq);
		lv.setAdapter(ardap);
		// d(TAG, "onCreateView");
		lv.setOnItemClickListener(this);
		return newsFragment;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		
		asyncJson("http://www.blognone.com/atom.xml");
		asyncJson("http://www.thairath.co.th/rss/news.xml");
		asyncJson("http://www.dailynews.co.th/rss.xml");
		asyncJson("http://www.matichon.co.th/rss/news_article.xml");
		asyncJson("http://www.manager.co.th/RSS/Game/Game.xml");
		asyncJson("http://www.komchadluek.net/rss/scienceit.xml");
		
		/*
		 * for (int i = 0; i < mCtx.fList.size(); i++) { Feeder fdObj = (Feeder)
		 * mCtx.fList.get(i); if (true) new
		 * SynchronousHtmlFetch().execute(fdObj.url); }
		 */
	}
	
	public void picasaCb(String provider, XmlDom xml, AjaxStatus status)
	{
		
		List<XmlDom> entries;
		tempList = new SArrayList();
		try
		{
			entries = xml.tags("item");
		} catch (NullPointerException e)
		{
			e.printStackTrace();
			return;
		}
		
		for (XmlDom entry : entries)
		{
			
			String titleStr = entry.text("title");
			String link = entry.text("link");
			String description = entry.text("description");
			String pubDate = entry.text("pubDate");
			String dcCreator = entry.text("dc:creator");
			
			// (TAG, "all content: " + entry.toString());
			// for unicode link
			link = StringEscapeUtils.unescapeJava(link);
			description = StringEscapeUtils.unescapeJava(description);
			
			News n = new News(provider, link, titleStr, description, pubDate,
					dcCreator);
			n.imgUrl = descriptionParser(entry.toString());
			// (TAG, "image link: " + n.imgUrl);
			// time parser with my self
			DateTime dt;
			try
			{
				
				int year = Integer.parseInt(pubDate.split(" ")[3]);
				int month = monthTranslate(pubDate.split(" ")[2]);
				int day = Integer.parseInt(pubDate.split(" ")[1]);
				int hour = Integer
						.parseInt(pubDate.split(" ")[4].split(":")[0]);
				int min = Integer.parseInt(pubDate.split(" ")[4].split(":")[1]);
				int sec = Integer.parseInt(pubDate.split(" ")[4].split(":")[2]);
				dt = new DateTime(year, month, day, hour, min, sec, 0);
				
			} catch (Exception e)
			{
				dt = new DateTime();
				// dt = Info.parseRfc822DateString(pubDate);
				
				// String dateString = "2010-03-01T00:00:00-08:00";
				String dateString = pubDate;
				String pattern = "yyyy-MM-dd'T'HH:mm:ssZ";
				DateTimeFormatter dtf = DateTimeFormat.forPattern(pattern);
				DateTime dateTime = dtf.parseDateTime(dateString);
				
				dt = dateTime;
				
			}
			n.pubDate = getHumanLanguageTime(dt);
			n.unixTime = dt.getMillis();
			tempList.add(n);
			mCtx.fList.add(n);
			
		}
		
		// if null image found try to search for content
		// need 2 index for compare
		if (tempList.size() >= 5)
		{
			String urlTemp1 = ((News) tempList.get(tempList.size() / 2)).link;
			AjaxCallback<String> cb = new AjaxCallback<String>();
			cb.url(urlTemp1).type(String.class);
			aq.sync(cb);
			// i(TAG, "fetching TEMPLATE1: " + urlTemp1);
			String xmlResponseContent = cb.getResult();
			AjaxStatus statusContent = cb.getStatus();
			diffImageUrlFirst(urlTemp1, xmlResponseContent, statusContent);
			
		}
	}
	
	public int monthTranslate(String monthString)
	{
		// Jan Feb Mar Apr May Jun Jul Aug Sep Oct Nov Dec
		if (monthString.equalsIgnoreCase("jan"))
		{
			return 1;
		} else if (monthString.equalsIgnoreCase("Feb"))
		{
			return 2;
		} else if (monthString.equalsIgnoreCase("Mar"))
		{
			return 3;
		} else if (monthString.equalsIgnoreCase("Apr"))
		{
			return 4;
		} else if (monthString.equalsIgnoreCase("May"))
		{
			return 5;
		} else if (monthString.equalsIgnoreCase("Jun"))
		{
			return 6;
		} else if (monthString.equalsIgnoreCase("Jul"))
		{
			return 7;
		} else if (monthString.equalsIgnoreCase("Aug"))
		{
			return 8;
		} else if (monthString.equalsIgnoreCase("Sep"))
		{
			return 9;
		} else if (monthString.equalsIgnoreCase("Oct"))
		{
			return 10;
		} else if (monthString.equalsIgnoreCase("Nov"))
		{
			return 11;
		} else if (monthString.equalsIgnoreCase("Dec"))
		{
			return 12;
		}
		
		return new DateTime().getMonthOfYear();
	}
	
	public void reloadView()
	{
		FeedListViewAdapter ardap = new FeedListViewAdapter(getActivity(),
				mCtx.newsList, aq);
		lv.setAdapter(ardap);
	}
	
	public void diffImageUrlFirst(String url, String html, AjaxStatus status)
	{
		try
		{
			imgUrlList1 = new ArrayList<String>();
			String imgUrl1[] = html.split("<img");
			// i(TAG, "template image1: " + imgUrl1.length);
			
			for (int i = 0; i < imgUrl1.length; i++)
			{
				
				if (imgUrl1[i].split("src=\"").length >= 2)
				{
					String imgUrlReal = imgUrl1[i].split("src=\"")[1]
							.split("\"")[0];
					
					if (imgUrlReal.indexOf(".js") == -1
							&& imgUrlReal.indexOf("http:") != -1)
					{
						// i(TAG, "template image1" + imgUrlReal);
						imgUrlList1.add(imgUrlReal);
					}
				}
			}
			// compare with next item
			// sampling and middle news list link
			String urlTemp1 = ((News) tempList.get((tempList.size() / 2) + 1)).link;
			
			AjaxCallback<String> cb = new AjaxCallback<String>();
			cb.url(urlTemp1).type(String.class);
			aq.sync(cb);
			// i(TAG, "fetching TEMPLATE2: " + urlTemp1);
			String xmlResponseContent = cb.getResult();
			AjaxStatus statusContent = cb.getStatus();
			diffImageUrlSecond(urlTemp1, xmlResponseContent, statusContent);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void diffImageUrlSecond(String url, String html, AjaxStatus status)
	{
		ArrayList<String> imgUrlList2 = new ArrayList<String>();
		
		String imgUrl[] = html.split("<img");
		// i(TAG, "template image2" + imgUrl.length);
		
		for (int i = 0; i < imgUrl.length; i++)
		{
			
			if (imgUrl[i].split("src=\"").length >= 2)
			{
				String imgUrlReal = imgUrl[i].split("src=\"")[1].split("\"")[0];
				if (imgUrlReal.indexOf(".js") == -1
						&& imgUrlReal.indexOf("http:") != -1)
				{
					// i(TAG, "template image2" + imgUrlReal);
					imgUrlList2.add(imgUrlReal);
				}
			}
		}
		for (int i = 0; i < imgUrlList1.size(); i++)
		{
			if (i < imgUrlList2.size())
			{
				// FOUND !!
				if (!imgUrlList1.get(i).equals(imgUrlList2.get(i)))
				{
					imgLocationIdx = i;
					break;
				}
				
			} else
			{
				break;
			}
			
		}
	}
	
	public void callbackByLink(News newsObj, String html, AjaxStatus status)
	{
		// parser for that URL
		String imgUrl[] = html.split("<img");
		// i(TAG, "CALL BACK HTML SIZE: " + html.length());
		// i(TAG, "CALL BACK BY IMAGE: " + imgUrl.length);
		int indexFilter = 0;
		boolean isFound = false;
		for (int i = 0; i < imgUrl.length; i++)
		{
			if (imgUrl[i].split("src=\"").length >= 2)
			{
				
				String imgUrlReal = imgUrl[i].split("src=\"")[1].split("\"")[0];
				// i(TAG, "image: " + imgUrlReal);
				if (imgUrlReal.indexOf(".js") == -1
						&& imgUrlReal.indexOf("http:") != -1)
				{
					
					if (indexFilter == imgLocationIdx)
					{
						// i(TAG, "real SETTING THIS IMAGE: " + imgUrlReal);
						newsObj.imgUrl = imgUrlReal;
						isFound = true;
					}
					++indexFilter;
					
				}
			}
		}
		
		// if don't find any image for some url select image that have longest
		// name to show user
		// this process below can remove it only use with MATICHON case
		int maxLongest = 0;
		
		if (isFound == false)
			for (int i = 0; i < imgUrl.length; i++)
			{
				
				if (newsObj.imgUrl == null)
					if (imgUrl[i].split("src=\"").length >= 2)
					{
						
						String imgUrlReal = imgUrl[i].split("src=\"")[1]
								.split("\"")[0];
						// i(TAG, "image: " + imgUrlReal);
						if (imgUrlReal.indexOf(".js") == -1
								&& imgUrlReal.indexOf("http:") != -1)
						{
							if (imgUrlReal.length() > maxLongest)
							{
								maxLongest = imgUrlReal.length();
								newsObj.imgUrl = imgUrlReal;
							}
							
						}
					}
			}
		
	}
	
	public String descriptionParser(String description)
	{
		
		String result = null;
		if (description.indexOf(".jpg\"") != -1)
		{
			String imgUrl = description.split(".jpg\"")[0];
			int lastIdx = imgUrl.lastIndexOf("\"") + 1;
			result = imgUrl.substring(lastIdx) + ".jpg";
			
			return result;
		}
		
		return result;
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		
		final Dialog dialog = new Dialog(getActivity());
		dialog.getWindow();
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.webview_dialog);
		dialog.setCancelable(true);
		dialog.show();
		// imgview.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		final int arg2Ja = arg2;
		final ImageButton iv = (ImageButton) dialog
				.findViewById(R.id.imageDialog);
		
		Button shareBtn = (Button) dialog.findViewById(R.id.shareImgBtn);
		// Enable Provider
		adapter.enable(shareBtn);
		
		ImageButton closeImgBtn = (ImageButton) dialog
				.findViewById(R.id.closeImgBtnDialog);
		closeImgBtn.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				dialog.dismiss();
			}
		});
		
		// adapter.e
		iv.setOnClickListener(new OnClickListener()
		{
			boolean scaleUp = false;
			News n = (News) lv.getItemAtPosition(arg2Ja);
			
			@Override
			public void onClick(View v)
			{
				if (!scaleUp)
				{
					aq.id(iv).image(n.imgUrl, true, true);
					
					iv.getLayoutParams().height = LayoutParams.MATCH_PARENT;
					iv.getLayoutParams().width = LayoutParams.MATCH_PARENT;
					iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
					
					scaleUp = true;
				} else if (scaleUp)
				{
					aq.id(iv).image(n.imgUrl, true, true, 200, 0);
					iv.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
					iv.getLayoutParams().width = LayoutParams.WRAP_CONTENT;
					iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
					scaleUp = false;
				}
			}
		});
		
		News n = (News) this.lv.getItemAtPosition(arg2);
		aq.id(iv).image(n.imgUrl, true, true, 200, 0);
		currentShowOnDialogNew = n;// for share later
		
		// mark as read
		ImageView isReadIv = (ImageView) arg1.findViewById(R.id.isReadIv);
		isReadIv.setVisibility(View.GONE);
		n.isRead = true;
		// update unReadText
		Info.getInstance().unReadCountTv.setText("0");
		// hiding if it down to zero
		
		// WEBVIEW settings
		String html = "" + "<html>" + "<head>"
				+ "<style type='text/css'>a:link {color:#ff8c00;}" + "body {"
				+ "font-family:serif;" + "color: #aaaaaa;"
				+ "background-color: #222222 }" + "</style>" + "</head>"
				+ "<body><h3>" + n.title + "</h3>\n" + n.description + "\n"
				+ "<a href=\"" + n.link + "\">"
				+ getString(R.string.continue_read_text) + "</body></html>";
		
		WebView myWebView = (WebView) dialog.findViewById(R.id.webview);
		WebSettings webSettings = myWebView.getSettings();
		webSettings.setDefaultFontSize(12);
		myWebView.loadDataWithBaseURL("file:///assets/", html, "text/html",
				"utf-8", null);
		
	}
	
	public String getHumanLanguageTime(DateTime newsTime)
	{
		String alreadyPassTime = "undefined";
		// joda time convert
		try
		{
			DateTime currentTime = new DateTime();
			Duration dur = new Duration(newsTime, currentTime);
			
			if (dur.getStandardDays() > 0)
			{
				alreadyPassTime = dur.getStandardDays() + " "
						+ getString(R.string.pass_day_text);
			} else if (dur.getStandardHours() > 0)
			{
				alreadyPassTime = dur.getStandardHours() + " "
						+ getString(R.string.pass_hour_text);
			} else if (dur.getStandardMinutes() > 0)
			{
				alreadyPassTime = dur.getStandardMinutes() + " "
						+ getString(R.string.pass_minute_text);
			} else
			{
				alreadyPassTime = getString(R.string.pass_second_text);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return alreadyPassTime;
	}
	
	public void asyncJson(String url)
	{
		Log.i(TAG, "request: " + url);
		aq.ajax(url, XmlDom.class, this, "xmlCallback");
	}
	
	public void xmlCallback(String url, XmlDom xml, AjaxStatus status)
	{
		if (xml != null)
		{
			Log.i(TAG, "response: " + url);
			// successful ajax call
			
			List<XmlDom> entries;
			tempList = new SArrayList();
			try
			{
				entries = xml.tags("item");
			} catch (NullPointerException e)
			{
				e.printStackTrace();
				return;
			}
			
			int index = 0;
			String firstUrl = "NA";
			String secondUrl = "NA";
			for (XmlDom entry : entries)
			{
				
				String titleStr = entry.text("title");
				String link = entry.text("link");
				String description = entry.text("description");
				String pubDate = entry.text("pubDate");
				String dcCreator = entry.text("dc:creator");
				
				link = StringEscapeUtils.unescapeJava(link);
				description = StringEscapeUtils.unescapeJava(description);
				
				News n = new News(url, link, titleStr, description, pubDate,
						dcCreator);
				n.imgUrl = descriptionParser(entry.toString());
				
				DateTime dt;
				try
				{
					
					int year = Integer.parseInt(pubDate.split(" ")[3]);
					int month = monthTranslate(pubDate.split(" ")[2]);
					int day = Integer.parseInt(pubDate.split(" ")[1]);
					int hour = Integer.parseInt(pubDate.split(" ")[4]
							.split(":")[0]);
					int min = Integer
							.parseInt(pubDate.split(" ")[4].split(":")[1]);
					int sec = Integer
							.parseInt(pubDate.split(" ")[4].split(":")[2]);
					dt = new DateTime(year, month, day, hour, min, sec, 0);
					
				} catch (Exception e)
				{
					dt = new DateTime();
					String dateString = pubDate;
					String pattern = "yyyy-MM-dd'T'HH:mm:ssZ";
					DateTimeFormatter dtf = DateTimeFormat.forPattern(pattern);
					DateTime dateTime = dtf.parseDateTime(dateString);
					
					dt = dateTime;
					
				}
				n.pubDate = getHumanLanguageTime(dt);
				n.unixTime = dt.getMillis();
				mCtx.newsList.add(n);
				
				index++;
				if (index == 1)
					firstUrl = n.link;
				if (index == 2)
					secondUrl = n.link;
			}// end reading loop
			
			new AsyncHtmlFetch().execute(firstUrl + "," + secondUrl);
			// finding image idx
			// aq.ajax(url, XmlDom.class, this, "xmlCallback");
			
		} else
		{
			// ajax error
			Log.e(TAG, "request timeout: " + url);
		}
	}
	
	private class AsyncHtmlFetch extends AsyncTask<String, Integer, String>
	{
		private String TAG = getClass().getSimpleName();
		private String url;
		
		@Override
		protected String doInBackground(String... uri)
		{
			Log.i(TAG, "start fetching image");
			int diffIdx = 0;
			String firstUrl = uri[0].split(",")[0];
			String secondUrl = uri[0].split(",")[1];
			
			String feederName = "NA";
			try
			{
				ArrayList<String> aList = new ArrayList<String>();
				ArrayList<String> bList = new ArrayList<String>();
				
				AjaxCallback<String> cbA = new AjaxCallback<String>();
				cbA.url(firstUrl).type(String.class);
				aq.sync(cbA);
				String resultA = cbA.getResult();
				AjaxStatus status1A = cbA.getStatus();
				
				resultA = resultA.replaceAll(".jpg\"", ".jpg.kak");
				resultA = resultA.replaceAll(".png\"", ".png.kak");
				resultA = resultA.replaceAll(".jpeg\"", ".jpeg.kak");
				resultA = resultA.replaceAll(".JPEG\"", ".JPEG.kak");
				
				
				String[] aSplit = resultA.split(".kak");
				for (int i = 0; i < aSplit.length; i++)
				{
					String imgUrl = aSplit[i];
					int lastIdx = imgUrl.lastIndexOf("\"") + 1;
					
					String imgUrlA = imgUrl.substring(lastIdx);
					
					// must have http:
					if (imgUrlA.indexOf("http:") != -1)
					{
						Log.i(TAG,"Image1: "+imgUrlA);
						aList.add(imgUrlA);
					}
				}
				
				// second url
				AjaxCallback<String> cbB = new AjaxCallback<String>();
				cbB.url(secondUrl).type(String.class);
				aq.sync(cbB);
				String resultB = cbB.getResult();
				AjaxStatus status1B = cbB.getStatus();
				
				resultB = resultB.replaceAll(".jpg\"", ".jpg.kak");
				resultB = resultB.replaceAll(".png\"", ".png.kak");
				resultB = resultB.replaceAll(".jpeg\"", ".jpeg.kak");
				resultB = resultB.replaceAll(".JPEG\"", ".JPEG.kak");
				
				String[] bSplit = resultB.split(".kak");
				for (int i = 0; i < bSplit.length; i++)
				{
					String imgUrl = bSplit[i];
					int lastIdx = imgUrl.lastIndexOf("\"") + 1;
					
					String imgUrlB = imgUrl.substring(lastIdx);
					
					// must have http:
					if (imgUrlB.indexOf("http:") != -1)
					{
						Log.i(TAG,"Image2: "+imgUrlB);
						bList.add(imgUrlB);
					}
				}
				
				for (int i = 0; i < aList.size(); i++)
				{
					if (i < bList.size())
					{
						if (!aList.get(i).equalsIgnoreCase(bList.get(i)))
						{
							diffIdx = i;
							break;
						}
					}
				}
				
				feederName = firstUrl.split("[.]")[1];
				imgIdxHM.put(feederName, diffIdx);
				
				// if it not have image find it //select only current feeder
				for (int i = 0; i < mCtx.newsList.size(); i++)
				{
					News newsObjNoImg = (News) mCtx.newsList.get(i);
					//if (
					//		newsObjNoImg.link.split("[.]")[1] == feederName)
					//{ 
						String whoFeed = newsObjNoImg.link.split("[.]")[1];
					
						if(newsObjNoImg.imgUrl == null && whoFeed.equalsIgnoreCase(feederName))
						setImgUrl(newsObjNoImg.link, newsObjNoImg);
					//}
				}
				
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			
			return feederName + "," + diffIdx + "";
		}
		
		public void setImgUrl(String url,News newsObj){
			ArrayList<String> aList = new ArrayList<String>();
			
			String feederName = url.split("[.]")[1];
			int imgIdxAt = imgIdxHM.get(feederName);
			
			
			Log.i(TAG,"IMGIDX IS :"+imgIdxAt);
			AjaxCallback<String> cbA = new AjaxCallback<String>();
			cbA.url(url).type(String.class);
			aq.sync(cbA);
			String resultA = cbA.getResult();//.toLowerCase(Locale.getDefault());
			AjaxStatus status1A = cbA.getStatus();
			//tricky for split without remove s
			resultA = resultA.replaceAll(".jpg\"", ".jpg.kak");
			resultA = resultA.replaceAll(".png\"", ".png.kak");
			resultA = resultA.replaceAll(".jpeg\"", ".jpeg.kak");
			resultA = resultA.replaceAll(".JPEG\"", ".JPEG.kak");
			
			String[] aSplit = resultA.split(".kak",-1);
			for (int i = 0; i < aSplit.length; i++)
			{
				String imgUrl = aSplit[i];
				int lastIdx = imgUrl.lastIndexOf("\"") + 1;
				
				String imgUrlA = imgUrl.substring(lastIdx);
				
				// must have http:
				if (imgUrlA.indexOf("http:") != -1)
				{
					Log.i(TAG,"ImageXXx: "+imgUrlA);
					aList.add(imgUrlA);
				}
			}
			if(aList.size()>imgIdxAt){
				newsObj.imgUrl = aList.get(imgIdxAt);
			}
			else{
				newsObj.imgUrl = aList.get(0);
			}
			Log.i(TAG,"Settings image: "+aList.size()+","+newsObj.imgUrl);

		}
		
		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);
			Log.i(TAG, "img index: " + result);
			reloadView();
		}
		
		
		
	}
	
	private class SynchronousHtmlFetch extends
			AsyncTask<String, Integer, String>
	{
		private String TAG = getClass().getSimpleName();
		private String url;
		
		@Override
		protected String doInBackground(String... uri)
		{
			// (TAG, "requesting: " + uri);
			this.url = uri[0];
			AjaxCallback<XmlDom> cb1 = new AjaxCallback<XmlDom>();
			cb1.url(this.url).type(XmlDom.class);
			// i(TAG, "Starting fetching");
			aq.sync(cb1);
			XmlDom xmlResponse1 = cb1.getResult();
			AjaxStatus status1 = cb1.getStatus();
			
			picasaCb(this.url, xmlResponse1, status1);
			if (getActivity() != null)
				getActivity().runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						reloadView();
					}
				});
			// this.fetchContentSynchronous();
			
			return "ok";
		}
		
		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);
			reloadView();
			
		}
		
		public void fetchContentSynchronous()
		{
			for (int i = 0; i < tempList.size(); i++)
			{
				News newsObj = (News) tempList.get(i);
				if (newsObj.imgUrl == null)
				{
					
					AjaxCallback<String> cb = new AjaxCallback<String>();
					cb.url(newsObj.link).type(String.class);
					aq.sync(cb);
					// i(TAG, "fetching content: " + tempList.get(i).link);
					String xmlResponseContent = cb.getResult();
					AjaxStatus statusContent = cb.getStatus();
					
					try
					{
						fileSize += xmlResponseContent.length();
						callbackByLink(newsObj, xmlResponseContent,
								statusContent);
					} catch (NullPointerException e)
					{
						e.printStackTrace();
					}
				}
			}
			
		}
		
	}
	
	@Override
	public void onInit(int status)
	{
	}
	
}