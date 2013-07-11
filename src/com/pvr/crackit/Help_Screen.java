package com.pvr.crackit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class Help_Screen extends Activity {
	TextView window_heading = null;
	private WebView mWebView = null;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_screen);

		initData();

		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setBackgroundColor(Color.WHITE);
		mWebView.setWebViewClient(new Callback());
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
			}
		});

		mWebView.loadUrl("file:///android_asset/www/help.html");
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
	}

	public float getWidthDIP(float values) {
		return values * (float) getResources().getDisplayMetrics().widthPixels
				/ 800;
	}

	public float getHeightDIP(float values) {
		return values * (float) getResources().getDisplayMetrics().heightPixels
				/ 432;
	}

}
