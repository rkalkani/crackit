package com.pvr.crackit;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pvr.database.DatabaseHelperResult;
import com.pvr.model.Result;

public class Result_Home extends Activity  {

	Context context = Result_Home.this;
	
	// Text View
	private TextView window_heading = null; // set screen heading in title bar
	
	// List view
	private ListView listview = null; // display result list
	private ImageButton window_back = null; // go to back screen

	// List
	private ArrayList<Result> resultlist = null; // contain result list

	// Adapter
	ResultAdapter mResultAdapter = null; // for result list

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resulthome);

		// Initialize variable
		initData();
		
		window_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// create list ans set listview
		createList();
	}

	@Override
	protected void onResume() {
		super.onResume();
		createList();
	}

	// initialize variable
	private void initData() {

		// TextView
		window_heading = (TextView) findViewById(R.id.window_title);
		window_heading.setText("Result");
		window_back = (ImageButton) findViewById(R.id.window_back);
		
		// List View
		listview = (ListView) findViewById(R.id.resultlist);

		// ArrayList
		resultlist = new ArrayList<Result>();

	}

	// create List
	private void createList() {
		try {
			DatabaseHelperResult database = new DatabaseHelperResult(
					context);
			
			resultlist = (ArrayList<Result>) database.getAllResultReverse();
			
			// create adapter
			// custom Result adapter and resultlistview layout used
			mResultAdapter = new ResultAdapter(resultlist, context);

			// set up result list
			listview.setAdapter(mResultAdapter);

		} catch (Exception e) {
			Log.d("EXCEPTION_RESULT_HOME", e.toString());
			Toast.makeText(Result_Home.this,
					"ERROR: Cannot access result table in database.",
					Toast.LENGTH_SHORT).show();
		}
	}
	
}
