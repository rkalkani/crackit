package com.pvr.crackit;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pvr.database.DatabaseHelper;
import com.pvr.model.Exam;

public class Exam_Home extends Activity implements OnClickListener {

	Context context = Exam_Home.this;

	// TextView
	private TextView window_heading = null; // for heading in title bar

	// ImageButton
	private ImageButton ibtn_newexam = null; // For create new exam
	private ImageButton window_back = null; // go to back screen

	// List View
	private ListView listview = null; // For show exam list in list view

	// List
	private ArrayList<Exam> examlist = null; // for store exam list from
												// database

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);

		setContentView(R.layout.examhome);

		// initialize variables
		initData();

		window_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// get Exam list from database and create listview
		createList();

		ibtn_newexam.setOnClickListener(this);

	}

	@Override
	protected void onResume() {
		super.onResume();
		createList();
	}

	private void initData() {

		// TextView
		window_heading = (TextView) findViewById(R.id.window_title);
		window_heading.setText("Exam");

		// Image Button
		ibtn_newexam = (ImageButton) findViewById(R.id.window_plus_button);
		ibtn_newexam.setVisibility(View.VISIBLE);
		window_back = (ImageButton) findViewById(R.id.window_back);

		// List view
		listview = (ListView) findViewById(R.id.examlist);

		// In stating of activity no exam are selected so
		// Related button is disabled which have no function with current exam
		// selection

		// set as start exam button is not pressed

	}

	// create exam list
	private void createList() {

		try {

			DatabaseHelper database = new DatabaseHelper(context);

			examlist = (ArrayList<Exam>) database.getAllexamsReverse();

			listview.setAdapter(new ExamAdapter(examlist, context));

		} catch (SQLException e) {
			Toast.makeText(context, "ERROR: Database can't accessed.",
					Toast.LENGTH_SHORT).show();
			Log.d("SQLEXCEPTION_EXAM_HOME", e.toString());
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		// to create new exam
		case R.id.window_plus_button:

			// start create new exam activity in examtba_group
			startActivity(new Intent(context, New_Exam_Form.class));

			break;

		}

	}

	public void gotoback() {
		finish();
	}

}
