package com.pvr.crackit;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pvr.common.Constant;
import com.pvr.database.DatabaseHelperResult;
import com.pvr.model.Exam;
import com.pvr.model.Result;

public class Result_Screen extends Activity {

	Context context = Result_Screen.this;

	// TextView
	private TextView window_heading = null; // set heading in title bar

	// ListView
	private ListView resultlist = null; // store answer sheet

	// Image Button
	private ImageButton btn_examhome = null; // for go to Exam screen
	private ImageButton window_back = null; // go to back screen

	// Exam
	private Exam exam = null;

	// ArrayList for contain answersheet element
	private ArrayList<String> ArrayList_Question_ID = null;
	private ArrayList<String> ArrayList_Questions = null;
	private ArrayList<String> ArrayList_Option_1 = null;
	private ArrayList<String> ArrayList_Option_2 = null;
	private ArrayList<String> ArrayList_Option_3 = null;
	private ArrayList<String> ArrayList_Option_4 = null;
	private ArrayList<String> ArrayList_Correct_Answer = null;
	private ArrayList<String> ArrayList_RightAns = null;
	private ArrayList<String> ArrayList_WrongAns = null;
	private ArrayList<String> ArrayList_SkipAns = null; // Skipped questions

	// String List
	private List<String> List_data = null;

	// User Answer
	private int[] User_Answer = null;

	// String
	private String string_percentage = null; // store percentage
	private String date = null; // store date
	private String time = null; // store time

	// Double
	private double percentage = 0.0;

	// Int
	private int rightans = 0; // total right answers
	private int wrongans = 0; // total wrong answers
	private int skipans = 0; // total skip questions
	private int database = 0; // use as flag for insert result in to database
								// also used for count no of try to insert
								// result
								// here maximum two try is taken for insert
								// result

	// store answer sheet from previous activity
	private Bundle mBundle_From_Exam_Screen = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resultscreen);

		// initialized variable
		initData();

		// get all element of answer sheet from intent
		exam = (Exam) mBundle_From_Exam_Screen
				.getSerializable(Constant.KEY_EXAM);
		ArrayList_Question_ID = mBundle_From_Exam_Screen
				.getStringArrayList(Constant.XML_CONSTANTS.KEY_ID);
		ArrayList_Questions = mBundle_From_Exam_Screen
				.getStringArrayList(Constant.XML_CONSTANTS.KEY_QUESTION);
		ArrayList_Option_1 = mBundle_From_Exam_Screen
				.getStringArrayList(Constant.XML_CONSTANTS.KEY_OPTION_1);
		ArrayList_Option_2 = mBundle_From_Exam_Screen
				.getStringArrayList(Constant.XML_CONSTANTS.KEY_OPTION_2);
		ArrayList_Option_3 = mBundle_From_Exam_Screen
				.getStringArrayList(Constant.XML_CONSTANTS.KEY_OPTION_3);
		ArrayList_Option_4 = mBundle_From_Exam_Screen
				.getStringArrayList(Constant.XML_CONSTANTS.KEY_OPTION_4);
		ArrayList_Correct_Answer = mBundle_From_Exam_Screen
				.getStringArrayList(Constant.XML_CONSTANTS.KEY_CORRECT_ANSWER);
		User_Answer = new int[ArrayList_Question_ID.size()];
		User_Answer = mBundle_From_Exam_Screen
				.getIntArray(Constant.XML_CONSTANTS.KEY_USER_ANSWER);
		date = mBundle_From_Exam_Screen.getString(Constant.KEY_DATE);
		time = mBundle_From_Exam_Screen.getString(Constant.KEY_TIME);

		// create answer sheet
		AsyncTask_DisplayData mAsyncTask_DisplayData = new AsyncTask_DisplayData();
		mAsyncTask_DisplayData.execute("Create Answerpaper detail");
		
		window_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// when exam home button pressed
		btn_examhome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// if database flag is not zero data is not inserted in database
				if (database != 0) {
					addResult();
				}
				if (database <= 0) {
					finish();
				}

			}
		});

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	// initialize variables
	private void initData() {

		// TextView
		window_heading = (TextView) findViewById(R.id.window_title);
		window_heading.setText("Answer Sheet");

		// ListView
		resultlist = (ListView) findViewById(R.id.lv_resultlist);

		// Button
		btn_examhome = (ImageButton) findViewById(R.id.ibtn_resultscreen_home);
		window_back = (ImageButton) findViewById(R.id.window_back);

		// Exam
		exam = new Exam();

		// Array List
		ArrayList_Question_ID = new ArrayList<String>();
		ArrayList_Questions = new ArrayList<String>();
		ArrayList_Option_1 = new ArrayList<String>();
		ArrayList_Option_2 = new ArrayList<String>();
		ArrayList_Option_3 = new ArrayList<String>();
		ArrayList_Option_4 = new ArrayList<String>();
		ArrayList_Correct_Answer = new ArrayList<String>();
		ArrayList_RightAns = new ArrayList<String>();
		ArrayList_WrongAns = new ArrayList<String>();
		ArrayList_SkipAns = new ArrayList<String>();

		// List
		List_data = new ArrayList<String>();

		// Int
		percentage = 0;
		rightans = 0;
		wrongans = 0;
		skipans = 0;
		database = 2; // database flag is set for maximum two try

		// String
		date = " ";
		time = " ";

		// one variable User_ans array is initialized in on create block after
		// size of array found

		// get all answer sheet elements from intent
		mBundle_From_Exam_Screen = getIntent().getBundleExtra(
				Constant.XML_CONSTANTS.KEY_BUNDLE);
	}

	// Add result to database
	private void addResult() {
		try {

			// Store result in database
			DatabaseHelperResult db = new DatabaseHelperResult(context);

			// Constructor signature
			// public Result(int _examid, String _examname, int _examtype,
			// int _durationofexam, int _noofquestion, String _questionset,
			// String _date, String _time, int _rightans, int _wrongans,
			// int _skipans, String _percentage)

			// Store all result detail to result class
			Result result = new Result(exam.getId(), exam.getExamname(),
					exam.getExamtype(), exam.getDurationofexam(),
					exam.getNoofquestion(), exam.getQuestionset(), date, time,
					rightans, wrongans, skipans, string_percentage);

			// if result inserted successfully it return non positive value
			if (db.addResult(result) > 0) {
				this.database = 0; // set flag zero for data successfully
									// inserted
			} else {
				Toast.makeText(getApplicationContext(),
						"ERROR: Result can't store in database.",
						Toast.LENGTH_SHORT).show();
				this.database--; // decrement try since data insertion failed
			}
		} catch (Exception e) {
			Log.d("EXCEPTION_RESULT_SCREEN", e.toString());
			Toast.makeText(getApplicationContext(),
					"ERROR: Database can't accessed.", Toast.LENGTH_SHORT)
					.show();
			this.database--; // decrement try since data insertion failed
		}
	}

	// create answer sheet
	public class AsyncTask_DisplayData extends
			AsyncTask<String, String, Boolean> {

		String data = null; // store answer sheet
		String type = " "; // store exam type in string

		private ProgressDialog mProgressDialog = null;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Create Progress Dialog
			mProgressDialog = new ProgressDialog(context);
			mProgressDialog.setMessage(Constant.TEXT_LOADING);
			mProgressDialog.show();
		}

		@Override
		protected Boolean doInBackground(String... params) {

			// separate answer sheet in three criteria
			// right , wrong , and skip question | answer
			for (int i = 0; i < ArrayList_Question_ID.size(); i++) {
				String ans = null; // store user's relevant answer from options

				// get user answer from option based in number
				switch (User_Answer[i]) {
				case 1:
					ans = ArrayList_Option_1.get(i).toString();
					break;
				case 2:
					ans = ArrayList_Option_2.get(i).toString();
					break;
				case 3:
					ans = ArrayList_Option_3.get(i).toString();
					break;
				case 4:
					ans = ArrayList_Option_4.get(i).toString();
					break;
				default:
					ans = " ";
					break;
				}

				// if answer string is black then is is skip answer
				if (ans.equalsIgnoreCase(" ")) {
					skipans++; // increment no of skip questions
					ArrayList_SkipAns.add(createQuestionAnswer(i) + ans + "\n");
				}
				// if answer string is equal to correct answer then it is right
				// answer
				else if (ArrayList_Correct_Answer.get(i).toString().equals(ans)) {
					rightans++; // increment no of right questions.
								// this no is also consider as marks
					ArrayList_RightAns
							.add(createQuestionAnswer(i) + ans + "\n");
				}
				// if answer string is not equal to correct answer then it is
				// wrong answer
				else if (!(ArrayList_Correct_Answer.get(i).toString()
						.equals(ans))) {
					wrongans++;// increment no of wrong questions.
					ArrayList_WrongAns
							.add(createQuestionAnswer(i) + ans + "\n");
				}
			}

			// get percentage of right answer and convert to two fractional
			// number
			try {

				if (ArrayList_Question_ID.size() > 0) {
					percentage = 100 * rightans / ArrayList_Question_ID.size();
					int int_percentage = (int) percentage;
					double dec_percentage = percentage - int_percentage;
					int int_dec_percentage = (int) (dec_percentage * 100);
					string_percentage = int_percentage + "."
							+ int_dec_percentage;
				} else {
					Toast.makeText(Result_Screen.this,
							"Some Error occured in Calculating Result",
							Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				Log.d("EXCEPTION_RESULT_SCREEN", e.toString());
			}

			// clear all not required list
			ArrayList_Question_ID.clear();
			ArrayList_Questions.clear();
			ArrayList_Option_1.clear();
			ArrayList_Option_2.clear();
			ArrayList_Option_3.clear();
			ArrayList_Option_4.clear();
			ArrayList_Correct_Answer.clear();

			// extract exam type to string
			if (exam.getExamtype() == 0) {
				type = "Time Limit";
			} else if (exam.getExamtype() == 1) {
				type = "No Time Limit";
			}

			// create answer sheet list
			List_data.add("\t\tAnswer Sheet");
			List_data.add("Exam Name  :  " + exam.getExamname()
					+ "\nExam Type  :  " + type + "\nExam Duration  :  "
					+ exam.getDurationofexam() + " minutes"
					+ "\nNo of Question  :  " + exam.getNoofquestion()
					+ "\nQuestion Set  :  " + exam.getQuestionset()
					+ "\nRight Answer  :  " + rightans + "\nWrong Answer  :  "
					+ wrongans + "\nSkip Answer  :  " + skipans
					+ "\nPercentage  :  " + string_percentage);
			//List_data.add("\t\tAnswer Sheet");

			return true;

		}

		// crate question and answer concrete string from index
		private String createQuestionAnswer(int i) {

			int j = i + 1;

			String questionAndAnswer = null;
			questionAndAnswer = "Q - " + j + " : " + ArrayList_Questions.get(i)
					+ "\n";
			questionAndAnswer = questionAndAnswer + "\ta. "
					+ ArrayList_Option_1.get(i) + "\n";
			questionAndAnswer = questionAndAnswer + "\tb. "
					+ ArrayList_Option_2.get(i) + "\n";
			questionAndAnswer = questionAndAnswer + "\tc. "
					+ ArrayList_Option_3.get(i) + "\n";
			questionAndAnswer = questionAndAnswer + "\td. "
					+ ArrayList_Option_4.get(i) + "\n";

			questionAndAnswer = questionAndAnswer + "\tCorrect Answer : "
					+ ArrayList_Correct_Answer.get(i) + "\n";
			questionAndAnswer = questionAndAnswer + "\tYour Answer : ";

			return questionAndAnswer;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			// if background process successfully executed
			if (result == true) {

				// add result to database
				addResult();

				// create final answer sheet from all three
				// type of ArrayyList
				// correct answer , wrong answer , skip answer
				if(ArrayList_RightAns.size() > 0)
					List_data.add("\t\tCorrect Questions");

				// add right answers
				for (int i = 0; i < ArrayList_RightAns.size(); i++) {
					List_data.add(ArrayList_RightAns.get(i));
				}
				ArrayList_RightAns.clear();

				if(ArrayList_WrongAns.size() > 0)
					List_data.add("\t\tWrong Questions");

				// add wrong answers
				for (int i = 0; i < ArrayList_WrongAns.size(); i++) {
					List_data.add(ArrayList_WrongAns.get(i));
				}
				ArrayList_WrongAns.clear();

				if(ArrayList_SkipAns.size() > 0)
					List_data.add("\t\tSkipped Questions");

				// Add skip questions
				for (int i = 0; i < ArrayList_SkipAns.size(); i++) {
					List_data.add(ArrayList_SkipAns.get(i));
				}
				ArrayList_SkipAns.clear();

				// set answer sheet in list view
				Answersheet_Adapter mAnswersheet_Adapter = new Answersheet_Adapter(
						(ArrayList<String>) List_data, context);
				resultlist.setAdapter(mAnswersheet_Adapter);
			} else {
				Toast.makeText(
						getApplicationContext(),
						"ERROR: There was some problem in creating answersheet.",
						Toast.LENGTH_SHORT).show();
			}
			// dissmiss process dialog
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
			}
		}

	}

}
