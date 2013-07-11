package com.pvr.crackit;

import java.io.File;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.pvr.common.Constant;
import com.pvr.database.DatabaseHelper;
import com.pvr.file.BrowseHelper;
import com.pvr.model.Exam;
import com.pvr.model.FileDir;
import com.pvr.xmlmodel.XMLParser;

import dateandtime.DateAndTime;

public class New_Exam_Form extends Activity implements OnClickListener {

	Context context = New_Exam_Form.this;

	// Button
	private Button btn_create; // for create exam
	private Button btn_cancel; // for cancel exam creation task
	private Button btn_reset; // for reset all field

	// Image Button
	private ImageButton btn_browse; // for browse xml file

	// Edit text
	private EditText et_examname; // for examname
	private EditText et_date; // for exam creation date
	private EditText et_durationtime; // for exam duration time in minutes
	private EditText et_noofquestion; // for no of question in question paper
	// private EditText et_questionset; // for absoulute path of xml

	// Text view
	private TextView tv_questionset_name;
	private TextView tv_questionset; // show absolute path of xml
	private TextView window_heading;

	// Error message Text View
	private TextView msg_examname;
	private TextView msg_durationtime;
	private TextView msg_nofoquestion;
	private TextView msg_questionset;

	// radio button
	private RadioButton rdb_limit; // for exam with time limit
	private RadioButton rdb_nolimit; // for exam with no time limit

	// String
	private String examname; // store examname
	private String date; // store exam creation date in format
	private String questionset; // store absolute path of xml
	private String time; // store exam creation time in format
	private String durationtime; // store exam duration time in minute
	private String noofquestion; // store no of question in question paper

	// Static String
	// public static String file_path;
	// public static String file_name;

	// Dialog
	Dialog dialog_browse; // dialog for create xml question file chooser

	// File
	File currentDir; // For store current directory in browse dialog
						// implementation

	// Adapter
	FileArrayAdapter mFileArrayAdapter; // contain current directory files for
										// listview

	// Integer
	private int examtype = 0; // 0 means examtype is time limit

	// for get date and time in custom extracted form
	DateAndTime dateandtime = new DateAndTime();

	private int maxQuestion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newexamform);

		// initialize variables
		initData();

		btn_create.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
		btn_reset.setOnClickListener(this);
		btn_browse.setOnClickListener(this);

		// Radio Button
		rdb_nolimit
				.setOnCheckedChangeListener(new OnCheckedChangeListenerImplementation());
		rdb_limit
				.setOnCheckedChangeListener(new OnCheckedChangeListenerImplementation());

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	private void initData() {

		// File
		currentDir = null;

		// Dialog
		dialog_browse = null;

		// Adapter
		mFileArrayAdapter = null;

		// Link Edit Text to id
		et_examname = (EditText) findViewById(R.id.et_examname);
		et_date = (EditText) findViewById(R.id.et_date);
		et_durationtime = (EditText) findViewById(R.id.et_durationtime);
		et_noofquestion = (EditText) findViewById(R.id.et_noofquestion);
		// et_questionset = (EditText) findViewById(R.id.et_questionset);

		// TextView
		tv_questionset_name = (TextView) findViewById(R.id.tv_questionset);
		tv_questionset = (TextView) findViewById(R.id.tv_newexam_uri);
		window_heading = (TextView) findViewById(R.id.window_title);
		window_heading.setText("Create New Exam");

		// error text view
		msg_examname = (TextView) findViewById(R.id.msg_examname_new);
		msg_durationtime = (TextView) findViewById(R.id.msg_durationtime_new);
		msg_nofoquestion = (TextView) findViewById(R.id.msg_noofquestion_new);
		msg_questionset = (TextView) findViewById(R.id.msg_browse_new);
		msg_questionset.setVisibility(View.INVISIBLE);

		// Button
		btn_create = (Button) findViewById(R.id.btn_create);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_reset = (Button) findViewById(R.id.btn_reset);

		// Image Button
		btn_browse = (ImageButton) findViewById(R.id.btn_newexam_browse);

		// radio button
		rdb_limit = (RadioButton) findViewById(R.id.radio_limit);
		rdb_nolimit = (RadioButton) findViewById(R.id.radio_nolimit);

		// get date from custom created class DateAndTime
		date = dateandtime.getDate();
		// set edit text layout to date value
		et_date.setText(date);

		maxQuestion = 999;

	}

	private final class OnCheckedChangeListenerImplementation implements
			OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// if no limit radio button is checked disable duration time
			// edittext box
			// and set duration time to 0 for no time limit
			if (rdb_nolimit.isChecked()) {
				et_durationtime.setEnabled(false);
				et_durationtime.setText("0");
				examtype = 1; // exam type = 1 for no time limit
			}
			// if time limit radio button is checked enable duration time
			// edittext box
			else {
				et_durationtime.setEnabled(true);
				examtype = 0; // exam type = 0 for time limit
			}

		}
	}

	private void create_browse_dialog() {

		// create dialog
		dialog_browse = new Dialog(context);

		// dialog can not cancel on back button press
		dialog_browse.setCancelable(true);

		// set view
		dialog_browse.setContentView(R.layout.browsedialog);

		// list view display all folder and files in directory
		ListView lv_browse = (ListView) dialog_browse
				.findViewById(R.id.lv_browse_dialog);

		// Helper class to get all directory contents and paths
		BrowseHelper mBrowseHelper = new BrowseHelper();

		// if current dir is null it means it is first attempt to create
		// create dialog so we have to check SDCARD status and
		// a acessable directory for get start
		if (currentDir == null) {
			currentDir = mBrowseHelper.getCurrentDir();
		}

		if (currentDir != null) {
			// get all directory folders and xml files
			List<FileDir> dir = mBrowseHelper.fill(currentDir);

			// set dialog title as current directory
			dialog_browse.setTitle(currentDir.getPath());

			// if any valid directory found then set adapater and
			// create list
			if (dir != null) {
				mFileArrayAdapter = new FileArrayAdapter(
						getApplicationContext(), R.layout.filebrowser, dir);
				lv_browse.setAdapter(mFileArrayAdapter);
			} else {
				Log.d("ERROR: ", "Directory not found");
				mFileArrayAdapter = null;
			}
		} else {
			Log.d("ERROR: ", "Directory not found");
			mFileArrayAdapter = null;
		}

		// when any browse dialog -> list item is clicked
		lv_browse.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> l, View v, int position,
					long id) {

				if (mFileArrayAdapter != null) {

					// get selected item
					FileDir mFileDir = mFileArrayAdapter.getItem(position);

					// check if selected item is folder or back list content
					// then reset current directory, dismiss dialog and
					// create new dialog with new directory
					if (mFileDir.getData().equalsIgnoreCase("folder")
							|| mFileDir.getData().equalsIgnoreCase(
									"parent directory")) {
						currentDir = new File(mFileDir.getPath());

						dialog_browse.dismiss();

						create_browse_dialog();
					}
					// if selected item file then set store file path in new
					// exam form
					// and close dialog
					else if ((!(mFileDir.getData().equalsIgnoreCase("folder")))
							&& (!(mFileDir.getData()
									.equalsIgnoreCase("parent directory")))) {

						String filename = mFileDir.getName();
						String[] filenameArray = filename.split("\\.");
						filename = "" + filenameArray[0];
						for (int i = 1; i < filenameArray.length - 1; i++) {
							filename = filename + "." + filenameArray[i];
						}

						tv_questionset_name.setText(filename);
						tv_questionset.setText(mFileDir.getPath());
						dialog_browse.dismiss();

						ProgressDialog mProgressDialog = null;

						try {

							mProgressDialog = new ProgressDialog(context);
							mProgressDialog.setMessage("XML Processing");
							mProgressDialog.show();

							XMLParser parser = null;
							String xml = null;
							Document doc = null;

							parser = new XMLParser();
							xml = null;
							xml = parser.getXmlFromFile(context,
									mFileDir.getPath());
							doc = parser.getDomElement(context, xml,
									R.raw.encryptquestionbank);

							// get all item node
							NodeList mNodeList = doc
									.getElementsByTagName(Constant.XML_CONSTANTS.KEY_ITEM);

							// set total no of question
							int questionsInQuestionset = mNodeList.getLength();

							if (questionsInQuestionset <= 1) {
								Toast.makeText(
										context,
										"Quesion set(XML) Required more than two questions in file.",
										Toast.LENGTH_LONG).show();
								msg_nofoquestion
								.setText("*Choose Quesion set(XML) than has minimum two questions in file.");
								maxQuestion = questionsInQuestionset;
								tv_questionset_name.setText("");
								tv_questionset.setText("");
							} else {
								tv_questionset_name.setText(filename);
								tv_questionset.setText(mFileDir.getPath());
								maxQuestion = questionsInQuestionset;
								msg_nofoquestion
										.setText("*Total Questions in exam should between 2 to "
												+ maxQuestion
												+ ". File has maximum "
												+ maxQuestion + " questions.");
							}
						} catch (Exception e) {
							Log.e("EXCEPTION_NEW_EXAM_FORM", e.toString());
							Toast.makeText(context,
									"ERROR: XML can't parsed to verify.",
									Toast.LENGTH_LONG).show();
						} finally {
							if (mProgressDialog != null) {
								mProgressDialog.dismiss();
							}
						}

					}
				}

			}
		});

		dialog_browse.show();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		// when browse button pressed
		case R.id.btn_newexam_browse:

			create_browse_dialog();

			break;

		// when exam create button pressed
		case R.id.btn_create:

			// store all edit text values in variables
			getValues();

			// get time from custom created DateAndTime Class
			time = dateandtime.getTime();
			date = dateandtime.getDate();

			String msg = "created"; // for if form is created then show toast
									// message
									// AND take as flag if msg is change from
									// created to other value
									// don't create exam since there is some
									// error

			int duration = 0, // take as default value if no value found

			noq = 0; // noq = no of questions

			// set error flag in msg if examname field is empty
			if (examname.equals("")) {
				// et_examname.setBackgroundDrawable()
				msg = "invalid value"; // set msg flag as invalid values in form
				msg_examname.setTextColor(getResources().getColor(
						R.drawable.error_color));
			} else {
				msg_examname.setTextColor(getResources().getColor(
						R.drawable.gray_small_text));
			}

			// if examtype = 0 means examtype is time limit
			// so time limit radio button should selected
			// if examtype = 1 means examtype is no time limit
			// so no time limit radio button should selected
			if ((examtype == 0 && rdb_nolimit.isChecked())
					|| (examtype == 1 && rdb_limit.isChecked())) {
				msg = "invalid value";
			}

			// set error flag in msg if duration field is empty
			if (durationtime.equals("")) {
				// et_durationtime.setTextColor();
				msg = "invalid value";
				msg_durationtime.setTextColor(getResources().getColor(
						R.drawable.error_color));
			} else {
				// if durationtime can't convert in numeric value
				// then set error flag
				try {
					duration = Integer.parseInt(durationtime.trim());
					msg_durationtime.setTextColor(getResources().getColor(
							R.drawable.gray_small_text));
				} catch (NumberFormatException e) {
					// et_durationtime.setBackgroundColor(65536);
					Log.d("EXCEPTION_NEW_EXAM_FORM", e.toString());
					msg = "*Invalid value";
					msg_durationtime.setTextColor(getResources().getColor(
							R.drawable.error_color));
				}

				// for time limit exam type duration time should be
				// greater then 0
				if (duration <= 0 && rdb_limit.isChecked()) {
					// et_durationtime.setBackgroundColor(65536);
					msg = "invalid value";
					msg_durationtime.setTextColor(getResources().getColor(
							R.drawable.error_color));
				} else {
					msg_durationtime.setTextColor(getResources().getColor(
							R.drawable.gray_small_text));
				}
			}

			// set error code in msg if noofquestion field is empty
			if (noofquestion.equals("")) {
				// et_noofquestion.setBackgroundColor(65536);
				msg = "invalid value";
				msg_nofoquestion.setTextColor(getResources().getColor(
						R.drawable.error_color));
			} else {

				// if nofoquestion can't convert in numeric value
				// then set error flag
				try {
					noq = Integer.parseInt(noofquestion.trim()); // noq no of
																	// questions
					msg_nofoquestion.setTextColor(getResources().getColor(
							R.drawable.gray_small_text));
				} catch (NumberFormatException e) {
					// et_noofquestion.setBackgroundColor(65536);
					Log.d("EXCEPTION_NEW_EXAM_FORM", e.toString());
					msg = "invalid value";
					msg_nofoquestion.setTextColor(getResources().getColor(
							R.drawable.error_color));
				}

				// minimum 2 question required to create exam
				// other wise set error flag
				if (noq <= 1 || noq > maxQuestion) {
					// et_noofquestion.setBackgroundColor(65536);
					msg = "invalid value";
					msg_nofoquestion.setTextColor(getResources().getColor(
							R.drawable.error_color));
				} else {
					msg_nofoquestion.setTextColor(getResources().getColor(
							R.drawable.gray_small_text));
				}
			}

			// set error coed in msg if question set filed is empty
			if (questionset.equals("")) {
				// et_questionset.setBackgroundColor(65536);
				msg = "invalid value";
				msg_questionset.setVisibility(View.VISIBLE);
				msg_questionset.setTextColor(getResources().getColor(
						R.drawable.error_color));
			} else {
				msg_questionset.setVisibility(View.INVISIBLE);
				msg_questionset.setTextColor(getResources().getColor(
						R.drawable.gray_small_text));
			}

			// if all field contain valid value
			// msg value would not change
			if (msg.equals("created")) {
				// add exam to database
				try {

					DatabaseHelper database = new DatabaseHelper(context);
					Exam exam = new Exam(examname, examtype, date, time,
							duration, noq, questionset);
					// return long id value greater than zero
					// if exam successfully created
					if (database.addExam(exam) > 0) {

					} else {
						msg = "ERROR: Exam can't added to database.";
					}
				} catch (Exception e) {
					Log.d("EXCEPTION_NEW_EXAM_FORM", e.toString());
					msg = "ERROR: Database can't accessed";
				}
			}

			Toast.makeText(New_Exam_Form.this, msg, Toast.LENGTH_SHORT).show();
			if (msg.equals("created")) {
				// ExamTab_GroupActivity.group.back();
				finish();
			}

			break;

		// when cancel button press exit activity without any change
		case R.id.btn_cancel:
			Toast.makeText(New_Exam_Form.this, "canceled", Toast.LENGTH_SHORT)
					.show();
			// ExamTab_GroupActivity.group.back();
			// ExamTab_GroupActivity.group.back();
			finish();
			break;

		// reset all values to it initial state
		case R.id.btn_reset:
			msg_examname.setTextColor(getResources().getColor(
					R.drawable.gray_small_text));
			msg_durationtime.setTextColor(getResources().getColor(
					R.drawable.gray_small_text));
			msg_nofoquestion.setTextColor(getResources().getColor(
					R.drawable.gray_small_text));
			msg_questionset.setTextColor(getResources().getColor(
					R.drawable.gray_small_text));
			msg_questionset.setVisibility(View.INVISIBLE);
			maxQuestion = 999;
			msg_nofoquestion
					.setText("*Total Questions in exam should between 2 to "
							+ maxQuestion);
			setInitialValues();
			Toast.makeText(New_Exam_Form.this, "reseted", Toast.LENGTH_SHORT)
					.show();
			break;
		}

	}

	// get value from current use for reset
	// trim use to ignore extra white space
	private void getValues() {

		examname = et_examname.getText().toString().trim();
		durationtime = et_durationtime.getText().toString().trim();
		noofquestion = et_noofquestion.getText().toString().trim();

		// questions set is set from textview
		// beacuse it hold full path
		questionset = tv_questionset.getText().toString();

	}

	// set edit text for initialization
	private void setInitialValues() {

		et_examname.setText("");
		et_durationtime.setText("");
		et_noofquestion.setText("");
		tv_questionset_name.setText("");

		tv_questionset.setText("");

		et_date.setText(dateandtime.getDate());

		rdb_limit.setChecked(true);
	}

}
