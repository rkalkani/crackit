package com.pvr.crackit;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
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
	private TextView window_heading;
	

	// ImageButton
	private ImageButton ibtn_newexam; // For create new exam
	//private ImageButton ibtn_search; // For search exam for given keyword
	//private ImageButton ibtn_cancel; // For reset examlist and display all exam
										// and
										// erase keyword of search

	// Edit Text
	//private EditText et_search; // For type search keyword

	// List View
	private ListView listview; // For show exam list in list view

	// Dialog
	Dialog dialog_detail; // For hold detail dialog information
	Dialog dialog_delete; // For hold delete dialog information

	// Int
	//private int itemPosition;
	//private int searchFlag; // Search flag is used to check whether request to
							// database for exam list is for all exam or for
							// exam by search

	// String
	//private String searchName; // searchName for store search string from
								// et_search
	
	public static Boolean isStartExam = null;


	// List
	private ArrayList<Exam> examlist; // for store exam list from database

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.examhome);
		
		

		// initialize variables
		initData();

		// get Exam list from database and create listview
		createList();

		ibtn_newexam.setOnClickListener(this);
		//ibtn_search.setOnClickListener(this);
		//ibtn_cancel.setOnClickListener(this);

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		isStartExam = false;
		createList();
	}
	

	private void initData() {

		//searchFlag = 0; // 0 indicate none search request

		// TextView
		window_heading = (TextView) findViewById(R.id.window_title);
		window_heading.setText("Exam");

		// Image Button
		ibtn_newexam = (ImageButton) findViewById(R.id.window_plus_button);
		ibtn_newexam.setVisibility(View.VISIBLE);
		//ibtn_search = (ImageButton) findViewById(R.id.ibtn_search);
		//ibtn_cancel = (ImageButton) findViewById(R.id.ibtn_cancel);

		// List view
		listview = (ListView) findViewById(R.id.examlist);

		// Edit Text
		//et_search = (EditText) findViewById(R.id.et_search);

		// In stating of activity no exam are selected so
		// Related button is disabled which have no function with current exam
		// selection
		
		isStartExam = false;
		
		
	}

	private void createList() {

		try {

			DatabaseHelper database = new DatabaseHelper(context);
			
			examlist = (ArrayList<Exam>) database.getAllexamsReverse();

			// if search flag is zero so create list for activity not for search
			// get all exam reverse so new exam stay on top
			//if (searchFlag == 0) {
			//	examlist = (ArrayList<Exam>) database.getAllexamsReverse();
			//}
			// if search flag is 1 search by exam name and create search list
			//else if (searchFlag == 1) {
			//	examlist = (ArrayList<Exam>) database.getExamsByName(searchName);
			//}

			
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

		case R.id.window_plus_button:

			// start new exam activity in examtba_group
			startActivity(new Intent(context, New_Exam_Form.class));
	
			break;

		// When Search Button pressed
		//case R.id.ibtn_search:
		//
			// get search text from edit box
		//	searchName = et_search.getText().toString().trim();

			// if no text is there set search flag 0 for non search request
			// set search flag 1 for search by exam name
		//	if (searchName.equalsIgnoreCase("")) {
		//		searchFlag = 0;
		//	} else {
		//		searchFlag = 1;
		//	}

			// recreate list according to search flag
		//	createList();

		//	break;

		// when search cancel button pressed
		//case R.id.ibtn_cancel:
		//
			// reset edittext box
		//	et_search.setText("");
			// set edit text box hint to Search
		//	et_search.setHint("Search");
		//	et_search.clearFocus();

			// reset search flag
		//	searchFlag = 0;

			// recreate list for non search request
		//	createList();
		//	break;

		}

	}

	/*
	// Async task for start exam
	private class AsyncTask_PaperCreate extends
			AsyncTask<String, String, Boolean> {

		private ProgressDialog mProgressDialog = null; // To show process dialog
		PaperCreateConstant mPaperCreateConstant = new PaperCreateConstant(); // use
																				// as
																				// structure
																				// for
																				// store
																				// question
																				// detail

		int errorCode = 0; // for describe which kind of error generated
		int questionsInQuestionset = 0; // for store total no question in xml
										// file

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// create process dialog
			mProgressDialog = new ProgressDialog(ExamTab_GroupActivity.group);
			mProgressDialog.setMessage(Constant.TEXT_LOADING);
			mProgressDialog.show();
		}

		@Override
		synchronized protected Boolean doInBackground(String... params) {
			try {

				XMLParser parser = new XMLParser();

				// get text from file
				String xml = parser.getXmlFromFile(examlist.get(itemPosition)
						.getQuestionset());
				// create DOM type document
				Document doc = parser.getDomElement(xml);

				// get all item node
				NodeList mNodeList = doc
						.getElementsByTagName(Constant.XML_CONSTANTS.KEY_ITEM);

				// set total no of question
				questionsInQuestionset = mNodeList.getLength();

				// check whether xml file has sufficent question for exam
				if (examlist.get(itemPosition).getNoofquestion() <= questionsInQuestionset) {

					errorCode = 0; // 0 for no error

					// generate random number for questionpaper
					int k = 0;
					List<Integer> intList = new ArrayList<Integer>();

					while (k < questionsInQuestionset) {
						intList.add(k);
						k++;
					}

					Collections.shuffle(intList);

					// get random questions from document according to random
					// numbers taking as index

					for (int j = 0; j < examlist.get(itemPosition)
							.getNoofquestion(); j++) {

						int i = intList.get(j);
						// Log.d("RANDOM", "NO IS " + i);

						// get element and store question detail in
						// PaperCreateConstant Model

						Element mElement = (Element) mNodeList.item(i);

						mPaperCreateConstant.ArrayList_Question_ID.add(parser
								.getValue(mElement,
										Constant.XML_CONSTANTS.KEY_ID));
						mPaperCreateConstant.ArrayList_Questions.add(parser
								.getValue(mElement,
										Constant.XML_CONSTANTS.KEY_QUESTION));
						mPaperCreateConstant.ArrayList_Option_1.add(parser
								.getValue(mElement,
										Constant.XML_CONSTANTS.KEY_OPTION_1));
						mPaperCreateConstant.ArrayList_Option_2.add(parser
								.getValue(mElement,
										Constant.XML_CONSTANTS.KEY_OPTION_2));
						mPaperCreateConstant.ArrayList_Option_3.add(parser
								.getValue(mElement,
										Constant.XML_CONSTANTS.KEY_OPTION_3));
						mPaperCreateConstant.ArrayList_Option_4.add(parser
								.getValue(mElement,
										Constant.XML_CONSTANTS.KEY_OPTION_4));
						mPaperCreateConstant.ArrayList_Correct_Answer
								.add(parser
										.getValue(
												mElement,
												Constant.XML_CONSTANTS.KEY_CORRECT_ANSWER));
						// Log.d("Question NO", " : " + i);
					}
					// return true if xml parse correctly
					return true;
				}
				// if xml doesn't have sufficient questions for exam
				else if (examlist.get(itemPosition).getNoofquestion() > mNodeList
						.getLength()) {
					errorCode = 1; // 1 for less question in xml
					return false;
				} else {
					errorCode = -1; // -1 other errors
					return false;
				}

			} catch (Exception e) {
				Log.d("EXCEPTION_EXAM_HOME_ASYNC", e.toString());
				return false;
			}
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (mProgressDialog != null) {
				// dismiss progress dialog
				mProgressDialog.dismiss();
			}
			if (result == true) {

				// put all question paper detail in bundle for parse it between
				// activity
				mPaperCreateConstant.mBundle.putStringArrayList(
						Constant.XML_CONSTANTS.KEY_ID,
						mPaperCreateConstant.ArrayList_Question_ID);
				mPaperCreateConstant.mBundle.putStringArrayList(
						Constant.XML_CONSTANTS.KEY_QUESTION,
						mPaperCreateConstant.ArrayList_Questions);
				mPaperCreateConstant.mBundle.putStringArrayList(
						Constant.XML_CONSTANTS.KEY_OPTION_1,
						mPaperCreateConstant.ArrayList_Option_1);
				mPaperCreateConstant.mBundle.putStringArrayList(
						Constant.XML_CONSTANTS.KEY_OPTION_2,
						mPaperCreateConstant.ArrayList_Option_2);
				mPaperCreateConstant.mBundle.putStringArrayList(
						Constant.XML_CONSTANTS.KEY_OPTION_3,
						mPaperCreateConstant.ArrayList_Option_3);
				mPaperCreateConstant.mBundle.putStringArrayList(
						Constant.XML_CONSTANTS.KEY_OPTION_4,
						mPaperCreateConstant.ArrayList_Option_4);
				mPaperCreateConstant.mBundle.putStringArrayList(
						Constant.XML_CONSTANTS.KEY_CORRECT_ANSWER,
						mPaperCreateConstant.ArrayList_Correct_Answer);
				mPaperCreateConstant.mBundle.putSerializable(Constant.KEY_EXAM,
						examlist.get(itemPosition));

				Intent mIntent_ExamScreen = new Intent(getApplicationContext(),
						Exam_Screen.class);
				mIntent_ExamScreen.putExtra(Constant.XML_CONSTANTS.KEY_BUNDLE,
						mPaperCreateConstant.mBundle);

				View view = ExamTab_GroupActivity.group
						.getLocalActivityManager()
						.startActivity(
								"start exam",
								mIntent_ExamScreen
										.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
						.getDecorView();
				ExamTab_GroupActivity.group.replaceView(view);
			} else {
				// error generated show message
				if (errorCode == -1) {
					Toast.makeText(getApplicationContext(),
							"ERROR: Problem in parsing xml.", Toast.LENGTH_LONG)
							.show();
				}
				// if xml has less no of questions than exam required
				else if (errorCode == 1) {
					Toast.makeText(
							getApplicationContext(),
							"Total questions in XML :" + questionsInQuestionset
									+ "\nPlease change no of question in EXAM.",
							Toast.LENGTH_LONG).show();

					Intent intent = new Intent(Exam_Home.this,
							Edit_Exam_Form.class);
					intent.putExtra("resultId", examlist.get(itemPosition)
							.getId());
					View view = ExamTab_GroupActivity.group
							.getLocalActivityManager()
							.startActivity(
									"edit exam",
									intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
							.getDecorView();
					ExamTab_GroupActivity.group.replaceView(view);
				}
			}
		}
	}
	*/

}
