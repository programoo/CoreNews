package com.programoo.corenews;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.VideoView;

import com.programoo.snews.R;

public class WebViewActivity extends Activity
{
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_webview);
		// String url = getIntent().getStringExtra("url");
		String description = getIntent().getStringExtra("description");
		
		String pureHtml = "<html><body>"+description+"</body></html>";
		String mime = "text/html";
		String encoding = "utf-8";
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
		
		;
		
		WebView myWebView = (WebView) findViewById(R.id.webview);
		WebSettings webSettings = myWebView.getSettings();
		webSettings.setDefaultFontSize(12);

		String head = "<head><style>@font-face {font-family: 'APARAJ';src: url('fonts/DroidSerif-Regular.ttf');}body {font-family: 'APARAJ';}</style></head>";
		String HtmlString = "<html>" + head + "<body>" + description
				+ "</body></html>";
		
		myWebView.loadDataWithBaseURL("file:///assets/", html,
				"text/html", "utf-8", null);
		
		// myWebView
		// .loadData(HtmlString, "contentType=text/html", "charset=UTF-8");
		
		// WebSettings webSettings = myWebView.getSettings();
		// webSettings.setJavaScriptEnabled(true);
		// myWebView.getSettings().setPluginsEnabled(true);
		// myWebView.loadDataWithBaseURL(null, html, mime, encoding, null);
		
		// myWebView.loadUrl(url);
	}
}
