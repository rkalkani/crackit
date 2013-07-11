package com.pvr.crackit;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pvr.database.DatabaseHelperResult;
import com.pvr.model.Result;

public class Result_Home extends Activity  {

	Context context = Result_Home.this;
	
	private TextView window_heading;
	
	// Image Button
	//private ImageButton ibtn_search; // search given keyword
	//private ImageButton ibtn_cancel; // cancel search

	// Edit Text
	//private EditText et_search; // take search keyword from user

	// List view
	private ListView listview; // display result list

	// List
	private ArrayList<Result> resultlist; // contain result list
	// private List<Integer> checklist; // hid check item index

	// Int
	//private int searchFlag; // used as flag in createlist

	// String
	//private String searchName; // hold search keyword

	// Adapter
	ResultAdapter mResultAdapter; // for result list

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resulthome);

		// Initialize variable
		initData();

		//ibtn_search.setOnClickListener(this);
		//ibtn_cancel.setOnClickListener(this);

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
		
		// Image Button
		//ibtn_search = (ImageButton) findViewById(R.id.ibtn_result_search);
		//ibtn_cancel = (ImageButton) findViewById(R.id.ibtn_result_cancel);

		// Edit Text
		//et_search = (EditText) findViewById(R.id.et_result_search);

		// List View
		listview = (ListView) findViewById(R.id.resultlist);

		// ArrayList
		resultlist = new ArrayList<Result>();

		// Int
		//searchFlag = 0; // set flag to zero for non search request

		// String
		//searchName = null;
	}

	// create List
	private void createList() {
		try {
			DatabaseHelperResult database = new DatabaseHelperResult(
					context);
			
			resultlist = (ArrayList<Result>) database.getAllResultReverse();
			
			// if flag is set for non search request fetch all result
			// else fetch result by examname
			//if (searchFlag == 0) {
			//	resultlist = (ArrayList<Result>) database.getAllResultReverse();
			//} else if (searchFlag == 1) {
			//	resultlist = (ArrayList<Result>) database
			//			.getResultByName(searchName);
			//}

			// create adapter
			// custom Result adapter and resultlistview layout used
			mResultAdapter = new ResultAdapter(resultlist, context);

			listview.setAdapter(mResultAdapter);

		} catch (Exception e) {
			Log.d("EXCEPTION_RESULT_HOME", e.toString());
			Toast.makeText(Result_Home.this,
					"ERROR: Cannot access result table in database.",
					Toast.LENGTH_SHORT).show();
		}
	}

	/*@Override
	public void onClick(View view) {

		switch (view.getId()) {

		// when search button pressed
		//case R.id.ibtn_result_search:
		//
			// get search keyword
		//	searchName = et_search.getText().toString().trim();
		//
			// if no keyword found send non search request
		//	if (searchName.equalsIgnoreCase("")) {
		//		searchFlag = 0; // search flag 0 for non search request
		//	} else {
		//		searchFlag = 1; // search flag 1 for search by exam name
		//	}
		//	createList();

		//	break;

		// when search cancel button pressed
		//case R.id.ibtn_result_cancel:
		//
			// reset search box
		//	et_search.setText("");
		//	et_search.setHint("Search");
		//	et_search.clearFocus();

			// send non search request and create list
		//	searchFlag = 0;
		//	createList();

		//	break;

		}

	}*/

}
