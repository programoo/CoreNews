package com.programoo.corenews;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.programoo.snews.R;

public class WebViewActivity extends Activity
{
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.webview_dialog);
		String description = getIntent().getStringExtra("description");
		// set font
		String html = ""
				+ "<html>"
				+ "<head>"
				+ "<style type='text/css'>"
				+ "body {"
				+ "font-family:serif;"
				+ "color: #aaaaaa;"
				+ "background-color: #222222 }"
				+ "</style>"
				+ "</head>" + "<body>" + description + "</body></html>";
		
		WebView myWebView = (WebView) findViewById(R.id.webview);
		WebSettings webSettings = myWebView.getSettings();
		webSettings.setDefaultFontSize(12);
		myWebView.loadDataWithBaseURL("file:///assets/", html,
				"text/html", "utf-8", null);
	}
}
