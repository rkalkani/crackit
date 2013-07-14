package com.pvr.crackit;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.pvr.common.Constant;
import com.pvr.model.Exam;

import dateandtime.DateAndTime;

public class Exam_Screen extends Activity {

	Context context = Exam_Screen.this;

	// Textview
	private TextView tv_question = null; // for show question
	private TextView tv_questionno = null; // for show current rank of question
											// and
	// total question
	private TextView tv_time = null; // for show remaining time if exam type is
										// time
	// limit

	// Radio Button
	private RadioButton rbtn_option1 = null; // for exam options
	private RadioButton rbtn_option2 = null;
	private RadioButton rbtn_option3 = null;
	private RadioButton rbtn_option4 = null;

	// Image Button
	private ImageButton btn_next = null; // for go to next question
	private ImageButton btn_previous = null; // for go to previous question
	private ImageButton btn_reset = null; // for reset given ans by uncheck all
											// radio
	// button
	private ImageButton btn_done = null; // to finish exam
	private ImageButton ibtn_flag = null; // to mark question for later see
	private ImageButton ibtn_flagfile = null; // to get only flag set question
	private ImageButton ibtn_skipfile = null; // to get only skip questions
	private ImageButton ibtn_quesiondetail = null; // show currently attempt
													// question detail

	// Scroll view
	private ScrollView sv_examscreen = null; // contain question and options

	// Exam detail
	private Exam exam = null; // for store exam preference

	// ArrayList
	// Store all question paper elements
	private ArrayList<String> ArrayList_Question_ID = null;
	private ArrayList<String> ArrayList_Questions = null;
	private ArrayList<String> ArrayList_Option_1 = null;
	private ArrayList<String> ArrayList_Option_2 = null;
	private ArrayList<String> ArrayList_Option_3 = null;
	private ArrayList<String> ArrayList_Option_4 = null;

	// Flag Array
	private Boolean[] flag = null;

	// Boolean FLag
	private Boolean flagfile = null;
	private Boolean skipfile = null;

	// Int
	private int[] User_Answer = null; // store user answer
	private static int current_position = 0; // set current index of question

	// for store bundle from previous intent
	private Bundle mBundle_From_Exam_Home = null;

	// to get date and time in custom format
	private DateAndTime mDateAndTime = null;

	//Dialog
	private Dialog dialog = null;
	
	// String
	private String date = null; // store exam attempt date
	private String time = null; // store exam attempt time

	CountDownTimer mCountDownTimer = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.examscreen);

		// initialize all variables
		initData();

		// get question paper and other data from previous activity to defined
		// variables
		exam = (Exam) mBundle_From_Exam_Home.getSerializable(Constant.KEY_EXAM);
		ArrayList_Question_ID = mBundle_From_Exam_Home
				.getStringArrayList(Constant.XML_CONSTANTS.KEY_ID);
		ArrayList_Questions = mBundle_From_Exam_Home
				.getStringArrayList(Constant.XML_CONSTANTS.KEY_QUESTION);
		ArrayList_Option_1 = mBundle_From_Exam_Home
				.getStringArrayList(Constant.XML_CONSTANTS.KEY_OPTION_1);
		ArrayList_Option_2 = mBundle_From_Exam_Home
				.getStringArrayList(Constant.XML_CONSTANTS.KEY_OPTION_2);
		ArrayList_Option_3 = mBundle_From_Exam_Home
				.getStringArrayList(Constant.XML_CONSTANTS.KEY_OPTION_3);
		ArrayList_Option_4 = mBundle_From_Exam_Home
				.getStringArrayList(Constant.XML_CONSTANTS.KEY_OPTION_4);

		// flag array
		flag = new Boolean[ArrayList_Question_ID.size()];
		Arrays.fill(flag, false);

		// create array of size equal to total question in paper
		User_Answer = new int[ArrayList_Question_ID.size()];
		// set all ans default zero for indicated skip question
		Arrays.fill(User_Answer, 0);

		// set date and time
		this.date = mDateAndTime.getDate();
		this.time = mDateAndTime.getTime();

		// start taking exam
		loadQuestion(0);

		// check exam type
		// if exam is time limited start count down timer
		if (exam.getExamtype() == 0) {

			// new CountDownTimer( time set for count down in milli second, time
			// when on tick execute)
			mCountDownTimer = new CountDownTimer(
					exam.getDurationofexam() * 60 * 1000, 1000) {

				// every 1 second on tick execuete and time change set on
				// textview
				public void onTick(long millisUntilFinished) {
					tv_time.setText((int) (millisUntilFinished / 3600000) + ":"
							+ ((int) (millisUntilFinished % 3600000) / 60000)
							+ ":"
							+ ((int) ((millisUntilFinished % 60000) / 1000)));
				}

				// when time finish close exam and go to result screen
				public void onFinish() {

					Intent intent = new Intent(context, Result_Screen.class);

					// put all question paper detail and also user answer
					mBundle_From_Exam_Home
							.putIntArray(
									Constant.XML_CONSTANTS.KEY_USER_ANSWER,
									User_Answer);
					mBundle_From_Exam_Home.putString(Constant.KEY_DATE, date);
					mBundle_From_Exam_Home.putString(Constant.KEY_TIME, time);

					intent.putExtra(Constant.XML_CONSTANTS.KEY_BUNDLE,
							mBundle_From_Exam_Home);

					startActivity(intent);
					finish();
				}
			};
			mCountDownTimer.start();
		}
		// if exam type is no time limit hide
		else {
			tv_time.setVisibility(View.INVISIBLE);
		}

		ibtn_quesiondetail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int skipQuestions = 0;
				int attemptQuestions = 0;
				int flagQuestions = 0;
				for (int i = 0; i < ArrayList_Question_ID.size(); i++) {
					if (flag[i] == true) {
						flagQuestions++;
					}
					if (User_Answer[i] == 0) {
						skipQuestions++;
					} else {
						attemptQuestions++;
					}
				}

				Dialog dialog = new Dialog(context);

				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setCancelable(true);
				dialog.setContentView(R.layout.examscreen_detail_dialog);
				

				TextView tv_detail = (TextView) dialog
						.findViewById(R.id.examscreen_dialog_detail);
				if (tv_detail != null)
					tv_detail.setText( attemptQuestions + "\n"
							+ skipQuestions + "\n"
							+ flagQuestions);

				dialog.show();

			}
		});

		// Button Listener use as toggle button
		// set flag for current question or remove flag
		ibtn_flag.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				flag[current_position] = !flag[current_position];

				if (flag[current_position] == false) {
					ibtn_flag.setImageResource(R.drawable.img_flag_white);
				} else {
					ibtn_flag.setImageResource(R.drawable.img_flag_red);
				}

			}
		});

		// Button Listener use as toggle button
		// set to traverse only flag set question
		ibtn_flagfile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				flagfile = !flagfile;

				if (flagfile == false) {
					ibtn_flagfile
							.setImageResource(R.drawable.img_flagfile_normal);
				} else {
					ibtn_flagfile
							.setImageResource(R.drawable.img_flagfile_selected);
				}

			}
		});

		// Button Listener use as toggle button
		// set to traverse only skip question
		ibtn_skipfile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				skipfile = !skipfile;

				if (skipfile == false) {
					ibtn_skipfile
							.setImageResource(R.drawable.img_skipfile_normal);
				} else {
					ibtn_skipfile
							.setImageResource(R.drawable.img_skipfile_selected);
				}

			}
		});

		// on previous button click
		// get previous question according to flagfile and skipfiel variable
		// state
		btn_previous.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				current_position = getPreviousQuestion(current_position);
				loadQuestion(current_position);
				initView(); // set screen view according to current position
			}

		});

		// on next button click
		// get next question according to flagfile and skipfiel variable state
		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				current_position = getNextQuestion(current_position);
				loadQuestion(current_position);
				initView(); // set screen view according to current position
			}

		});

		// reset all radio button and make user ans 0 for skip question
		btn_reset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				User_Answer[current_position] = 0;
				initView(); // set screen view according to current position and
							// reset scroll
			}
		});

		// when done button press close exam and go to result screen
		btn_done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// create dialog for conformation
				dialog.setContentView(R.layout.deletedialog);
				dialog.setCancelable(true);

				dialog.setTitle("  Finish");
				dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
						R.drawable.img_done_normal);

				TextView tv_delete = (TextView) dialog
						.findViewById(R.id.tv_delete_dialog);
				tv_delete.setText("Do you really want to finish exam?");

				Button btn_yes = (Button) dialog
						.findViewById(R.id.btn_delete_dialog_yes);
				Button btn_no = (Button) dialog
						.findViewById(R.id.btn_delete_dialog_no);

				// on true confirmation finish exam and go to answer sheet
				btn_yes.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context, Result_Screen.class);

						// put all question paper detail and also user answer
						mBundle_From_Exam_Home.putIntArray(
								Constant.XML_CONSTANTS.KEY_USER_ANSWER,
								User_Answer);
						mBundle_From_Exam_Home.putString(Constant.KEY_DATE,
								date);
						mBundle_From_Exam_Home.putString(Constant.KEY_TIME,
								time);

						intent.putExtra(Constant.XML_CONSTANTS.KEY_BUNDLE,
								mBundle_From_Exam_Home);

						dialog.dismiss();

						// cancel count down timer other wise on
						// completion of counter counter.finish method execute
						if (mCountDownTimer != null) {
							mCountDownTimer.cancel();
						}

						startActivity(intent);
						finish();
					}
				});

				// for no continue exam
				btn_no.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();

					}
				});

				dialog.show();

			}
		});

	}

	// when back button pressed
	@Override
	public void onBackPressed() {
		dialog.setContentView(R.layout.deletedialog);
		//super.onBackPressed();
		
		// create dialog for conformation
		//


		dialog.setCancelable(true);

		dialog.setTitle("  Cancel");
		dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				R.drawable.img_done_normal);

		TextView tv_delete = (TextView) dialog
				.findViewById(R.id.tv_delete_dialog);
		tv_delete.setText("Do you really want to cancel exam?");

		Button btn_yes = (Button) dialog
				.findViewById(R.id.btn_delete_dialog_yes);
		Button btn_no = (Button) dialog
				.findViewById(R.id.btn_delete_dialog_no);

		// on true confirmation finish exam and go to answer sheet
		btn_yes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mCountDownTimer != null) {
					mCountDownTimer.cancel();
				}
				finish();
			}
		});

		// for no continue exam
		btn_no.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});

		dialog.show();
	}

	// when radio button checked
	// store user answer according to radio button checked
	// and unchecked other radio button
	public void onRadioButtonChecked(View view) {
		Boolean checked = ((RadioButton) view).isChecked();

		switch (view.getId()) {
		case R.id.rbtn_examscreen_option1:
			if (checked) {
				User_Answer[current_position] = 1;
				rbtn_option2.setChecked(false);
				rbtn_option3.setChecked(false);
				rbtn_option4.setChecked(false);
			}
			break;
		case R.id.rbtn_examscreen_option2:
			if (checked) {
				User_Answer[current_position] = 2;
				rbtn_option1.setChecked(false);
				rbtn_option3.setChecked(false);
				rbtn_option4.setChecked(false);
			}
			break;
		case R.id.rbtn_examscreen_option3:
			if (checked) {
				User_Answer[current_position] = 3;
				rbtn_option1.setChecked(false);
				rbtn_option2.setChecked(false);
				rbtn_option4.setChecked(false);
			}
			break;
		case R.id.rbtn_examscreen_option4:
			if (checked) {
				User_Answer[current_position] = 4;
				rbtn_option1.setChecked(false);
				rbtn_option2.setChecked(false);
				rbtn_option3.setChecked(false);
			}
			break;
		}
	}

	// initialize variables
	private void initData() {

		//Dialog
		dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
		
		// Text View
		tv_question = (TextView) findViewById(R.id.tv_examscreen_question);
		tv_questionno = (TextView) findViewById(R.id.tv_examscreen_questionno);
		tv_time = (TextView) findViewById(R.id.tv_examscreen_time);

		// Radio Button
		rbtn_option1 = (RadioButton) findViewById(R.id.rbtn_examscreen_option1);
		rbtn_option2 = (RadioButton) findViewById(R.id.rbtn_examscreen_option2);
		rbtn_option3 = (RadioButton) findViewById(R.id.rbtn_examscreen_option3);
		rbtn_option4 = (RadioButton) findViewById(R.id.rbtn_examscreen_option4);

		// Image Button
		btn_previous = (ImageButton) findViewById(R.id.btn_examscreen_previous);
		btn_next = (ImageButton) findViewById(R.id.btn_examscreen_next);
		btn_reset = (ImageButton) findViewById(R.id.btn_examscreen_reset);
		btn_done = (ImageButton) findViewById(R.id.btn_examscreen_done);

		// ImageButton
		ibtn_flag = (ImageButton) findViewById(R.id.ibtn_examscreen_flag);
		ibtn_flagfile = (ImageButton) findViewById(R.id.ibtn_examscreen_flagfile);
		ibtn_skipfile = (ImageButton) findViewById(R.id.ibtn_examscreen_skipfile);
		ibtn_quesiondetail = (ImageButton) findViewById(R.id.ibtn_examscreen_detail);

		// Scroll View
		sv_examscreen = (ScrollView) findViewById(R.id.sv_examscreen1);

		// Exam
		exam = new Exam();

		// ArrayList
		ArrayList_Question_ID = new ArrayList<String>();
		ArrayList_Questions = new ArrayList<String>();
		ArrayList_Option_1 = new ArrayList<String>();
		ArrayList_Option_2 = new ArrayList<String>();
		ArrayList_Option_3 = new ArrayList<String>();
		ArrayList_Option_4 = new ArrayList<String>();

		// Set current position 0 as first question index
		current_position = 0;

		// flag
		flagfile = false;
		skipfile = false;

		// set date and time
		mDateAndTime = new DateAndTime();
		// final date and time set at just before question load
		date = " ";
		time = " ";

		// one variable User_ans array is initialized in on create block after
		// size of array found

		// get bundle from intent
		mBundle_From_Exam_Home = getIntent().getBundleExtra(
				Constant.XML_CONSTANTS.KEY_BUNDLE);
	}

	// initialized based on question number
	private void initView() {
		// set scroll to top
		sv_examscreen.setScrollY(0);

		// reset all check box onload new question
		// since user given no answer
		rbtn_option1.setChecked(false);
		rbtn_option2.setChecked(false);
		rbtn_option3.setChecked(false);
		rbtn_option4.setChecked(false);

		// if user already refer question
		// again refer then if user already answer question
		// set appropriate check button true
		if (User_Answer[current_position] == 1) {
			rbtn_option1.setChecked(true);
		} else if (User_Answer[current_position] == 2) {
			rbtn_option2.setChecked(true);
		} else if (User_Answer[current_position] == 3) {
			rbtn_option3.setChecked(true);
		} else if (User_Answer[current_position] == 4) {
			rbtn_option4.setChecked(true);
		}

		// set flag state according flag array status for current position
		if (flag[current_position] == false) {
			ibtn_flag.setImageResource(R.drawable.img_flag_white);
		} else {
			ibtn_flag.setImageResource(R.drawable.img_flag_red);
		}
	}

	// get previous question according flag states
	private int getPreviousQuestion(int index) {

		// get consecutive previous question
		if (flagfile == false && skipfile == false) {
			index--;
			return index;
		}
		// get previous flag set question
		else if (flagfile == true && skipfile == false) {

			for (int i = index - 1; i >= 0; i--) {
				if (flag[i] == true) {
					return i;
				}
			}

			Toast.makeText(context, "No more previous Flag set question found",
					Toast.LENGTH_SHORT).show();

			return index;
		}
		// get previous skip question
		else if (flagfile == false && skipfile == true) {
			for (int i = index - 1; i >= 0; i--) {
				if (User_Answer[i] == 0) {
					return i;
				}
			}

			Toast.makeText(context, "No more previous Skip question found",
					Toast.LENGTH_SHORT).show();

			return index;

		}
		// get previous skip or flag set question
		else if (flagfile == true && skipfile == true) {

			for (int i = index - 1; i >= 0; i--) {
				if (User_Answer[i] == 0 || flag[i] == true) {
					return i;
				}
			}

			Toast.makeText(context,
					"No more previous Skip or Flag set question found",
					Toast.LENGTH_SHORT).show();

			return index;
		}

		return index;
	}

	// get next question according flag states
	private int getNextQuestion(int index) {

		// get next consecutive question
		if (flagfile == false && skipfile == false) {
			index++;
			return index;
		}
		// get next flag set question
		else if (flagfile == true && skipfile == false) {

			for (int i = index + 1; i <= ArrayList_Question_ID.size() - 1; i++) {
				if (flag[i] == true) {
					return i;
				}
			}

			Toast.makeText(context, "No more next Flag set question found",
					Toast.LENGTH_SHORT).show();

			return index;
		}
		// get next skip question
		else if (flagfile == false && skipfile == true) {
			for (int i = index + 1; i <= ArrayList_Question_ID.size() - 1; i++) {
				if (User_Answer[i] == 0) {
					return i;
				}
			}

			Toast.makeText(context, "No more previous Skip question found",
					Toast.LENGTH_SHORT).show();

			return index;

		}
		// get next skip or flag set question
		else if (flagfile == true && skipfile == true) {

			for (int i = index + 1; i <= ArrayList_Question_ID.size() - 1; i++) {
				if (User_Answer[i] == 0 || flag[i] == true) {
					return i;
				}
			}

			Toast.makeText(context,
					"No more previous Skip or Flag set question found",
					Toast.LENGTH_SHORT).show();

			return index;
		}

		return index;
	}

	// load question on user request
	private void loadQuestion(int position) {

		// manage exam control button according to question index
		// View.VISIBLE make button visible
		// View.INVISIBALE make button invisible
		if (position > 0 && position < (ArrayList_Question_ID.size() - 1)) {
			btn_next.setVisibility(View.VISIBLE);
			btn_previous.setVisibility(View.VISIBLE);
		} else if (position == 0) {
			btn_next.setVisibility(View.VISIBLE);
			btn_previous.setVisibility(View.INVISIBLE);
		} else if (position == (ArrayList_Question_ID.size() - 1)) {
			btn_next.setVisibility(View.INVISIBLE);
			btn_previous.setVisibility(View.VISIBLE);
		}

		// Set Question and option on screen
		if (position >= 0 && position < ArrayList_Question_ID.size()) {
			tv_question.setText("Q.\t"
					+ ArrayList_Questions.get(position).trim());
			rbtn_option1.setText("1. "
					+ ArrayList_Option_1.get(position).trim());
			rbtn_option2.setText("2. "
					+ ArrayList_Option_2.get(position).trim());
			rbtn_option3.setText("3. "
					+ ArrayList_Option_3.get(position).trim());
			rbtn_option4.setText("4. "
					+ ArrayList_Option_4.get(position).trim());

			// change question no on screen
			int questionNo = current_position + 1;
			tv_questionno.setText("" + questionNo + " of "
					+ ArrayList_Question_ID.size());
		}

	}

}
