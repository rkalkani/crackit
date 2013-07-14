package com.pvr.crackit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

public class Help_Screen extends Activity {
	
	Context context = Help_Screen.this;
	
	// Text View
	TextView window_heading = null; // display heading of title bar
	
	// to display help html in help activity
	private WebView mWebView = null;
	
	// BUtton display author detail
	private ImageButton ibtn_author = null;
	private ImageButton window_back = null; // go to back screen

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_screen);

		// initialize variables
		initData();
		
		window_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();				
			}
		});
		
		// on author button click show author detail dialog
		ibtn_author.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					// create author dialog
					Dialog dialog = new Dialog(Help_Screen.this);
					
					dialog.setCancelable(true);
					
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					
					dialog.setContentView(R.layout.author);
					
					// set dialog detail
					TextView tv_author1 = (TextView) dialog.findViewById(R.id.author_developr_1);
					tv_author1.setText("Rahul Kalkani");
					TextView tv_author2 = (TextView) dialog.findViewById(R.id.author_developr_2);
					tv_author2.setText("Vishal Bhingaradiya");
					TextView tv_author3 = (TextView) dialog.findViewById(R.id.author_developr_3);
					tv_author3.setText("Harsh Kevadia");
					TextView tv_author4 = (TextView) dialog.findViewById(R.id.author_developr_4);
					tv_author4.setText("Piyush Patel");
					TextView tv_thanks = (TextView) dialog.findViewById(R.id.author_thanks);
					tv_thanks.setText("DaLpat TapaniYa for CrackIt logo");
					
					// display dialog
					dialog.show();
				} catch (Exception e) {
					Log.e("Dialog_author", e.toString());
				}
				
			}
		});

		// set web view configuration
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setBackgroundColor(Color.WHITE);
		mWebView.setWebViewClient(new Callback());
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
			}
		});

		mWebView.loadUrl("file:///android_asset/www/help_inner.html");
	}

	private class Callback extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView webview, String url) {
			webview.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView webview, String url) {
			super.onPageFinished(webview, url);
		}
	}

	private void initData() {

		window_heading = (TextView) findViewById(R.id.window_title);
		mWebView = (WebView) findViewById(R.id.WebView_HelpScreen);
		window_heading.setText("Help");
		
		// set author dialog button
		ibtn_author = (ImageButton) findViewById(R.id.window_plus_button);
		ibtn_author.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.img_user));
		ibtn_author.setVisibility(View.VISIBLE);
		window_back = (ImageButton) findViewById(R.id.window_back);
		
	}

	// set screen size 
	public float getWidthDIP(float values) {
		return values * (float) getResources().getDisplayMetrics().widthPixels
				/ 800;
	}

	public float getHeightDIP(float values) {
		return values * (float) getResources().getDisplayMetrics().heightPixels
				/ 432;
	}
	
}
