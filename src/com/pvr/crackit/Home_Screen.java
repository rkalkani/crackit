package com.pvr.crackit;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class Home_Screen extends Activity {
	
	Context context = Home_Screen.this;

	private ImageButton btn_exam = null;
	private ImageButton btn_result = null;
	private ImageButton btn_help = null;
	
	TextView tv_quotes = null;
	
	String[] quotes = null;
	
	Random random = null;
	
	int randomNumber = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		initData();

		btn_exam.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent exam = new Intent(context, Exam_Home.class);
				startActivity(exam);
			}
		});

		btn_result.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent result = new Intent(context, Result_Home.class);
				startActivity(result);
			}
		});

		btn_help.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent help = new Intent(context, Help_Screen.class);
				startActivity(help);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		randomNumber = getRandomNumber(quotes.length - 1);
		Log.d("tag", "RandomNo : " + randomNumber );
		tv_quotes.setText(quotes[randomNumber]);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	private void initData() {
		btn_exam = (ImageButton) findViewById(R.id.btn_home_exam);
		btn_result = (ImageButton) findViewById(R.id.btn_home_result);
		btn_help = (ImageButton) findViewById(R.id.btn_home_help);
		
		tv_quotes = (TextView) findViewById(R.id.tv_home_quotes);
		
		quotes = context.getResources().getStringArray(R.array.quotes);
		
		random = new Random();
		
		//randomNumber = getRandomNumber(quotes.length - 1);
		//Log.d("tag", "RandomNo : " + randomNumber );
		
		//tv_quotes.setText(quotes[randomNumber]);
	}
	
	public int getRandomNumber(int size)
	{
		return random.nextInt(size);
	}

}
