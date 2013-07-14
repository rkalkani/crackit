package com.pvr.crackit;

import java.util.Random;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class Home_Screen extends Activity {
	
	Context context = Home_Screen.this;

	// Image button
	private ImageButton btn_exam = null; // for go to exam screen 
	private ImageButton btn_result = null; // for go to result screen
	private ImageButton btn_help = null; // for go to help screen
	private ImageButton window_back = null; // go to back screen
	private ImageButton ibtn_author = null;
	
	// to display quotes at footer
	TextView tv_quotes = null;
	
	// contain all quotes from raw string array
	String[] quotes = null;
	
	// generate random number for select random quotes form string array
	Random random = null;
	
	// Store random number
	int randomNumber = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		// Initialize variables
		initData();
		
ibtn_author.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					// create author dialog
					Dialog dialog = new Dialog(context);
					
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


		btn_exam.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// start exam screen
				Intent exam = new Intent(context, Exam_Home.class);
				startActivity(exam);
			}
		});

		btn_result.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// start result screen
				Intent result = new Intent(context, Result_Home.class);
				startActivity(result);
			}
		});

		btn_help.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// start help screen
				Intent help = new Intent(context, Help_Screen.class);
				startActivity(help);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		// create random number and set random quotes
		randomNumber = getRandomNumber(quotes.length - 1);
		Log.d("tag", "RandomNo : " + randomNumber );
		tv_quotes.setText(quotes[randomNumber]);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		// exit from application
		finish();
	}

	private void initData() {
		
		// Image Button
		btn_exam = (ImageButton) findViewById(R.id.btn_home_exam);
		btn_result = (ImageButton) findViewById(R.id.btn_home_result);
		btn_help = (ImageButton) findViewById(R.id.btn_home_help);
		window_back = (ImageButton) findViewById(R.id.window_back);
		window_back.setVisibility(View.GONE);
		// set author dialog button
		ibtn_author = (ImageButton) findViewById(R.id.window_plus_button);
		ibtn_author.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.img_user));
		ibtn_author.setVisibility(View.VISIBLE);
		
		// text view
		tv_quotes = (TextView) findViewById(R.id.tv_home_quotes);
		
		// store all quotes from raw to string array
		quotes = context.getResources().getStringArray(R.array.quotes);
		
		random = new Random();
		
	}
	
	// function to generate random number
	public int getRandomNumber(int size)
	{
		return random.nextInt(size);
	}

}
