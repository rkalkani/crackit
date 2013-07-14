package com.pvr.crackit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

public class Edit_Exam_Form extends Activity implements OnClickListener {

	Context context = Edit_Exam_Form.this;

	// Button
	private Button btn_create = null; // for update exam detail
	private Button btn_cancel = null; // for cancel edit exam
	private Button btn_reset = null; // for reset exam to initial detail
	private ImageButton window_back = null; // go to back screen

	// ImageButton
	private ImageButton btn_browse = null; // for browse xml file

	// EditText
	private EditText et_examname = null; // for examname
	private EditText et_date = null; // for exam creation date
	private EditText et_durationtime = null; // for exam duration
	private EditText et_noofquestion = null; // for no of question for exam

	// TextView
	private TextView tv_questionset_name = null;
	private TextView tv_questionset = null;// for show absolute path of xml
	private TextView window_heading = null;

	// Error message Text View to show data validations and errors
	private TextView msg_examname = null;
	private TextView msg_durationtime = null;
	private TextView msg_nofoquestion = null;
	private TextView msg_questionset = null;

	// Radio Button
	private RadioButton rdb_limit = null; // for exam type time limit
	private RadioButton rdb_nolimit = null; // for exam type no time limit

	// String
	private String examname = null; // store exam name
	private String date = null; // store exam creation date
	private String questionset = null; // store absolute path
	private String time = null; // store exam creation time
	private String durationtime = null; // store exam duration time
	private String noofquestion = null; // store no of question for exam

	// Dialog
	Dialog dialog_browse = null; // dialog for create xml question file chooser

	// File
	File currentDir = null; // For store current directory in browse dialog

	// Adapter
	FileArrayAdapter mFileArrayAdapter = null; // contain current directory
												// files for
	// Int
	private int examtype = 0; // 0 means time limit and 1 means no time limit
	private int dataId = 0; // id received from last activity through intent for
						// edit data
	private int maxQuestion = 999;
	private int initMaxQuestion = 999;

	// Exam
	private Exam result = null; // Object for store Exam detail

	// for get date and time in custom extracted form
	private DateAndTime dateandtime = new DateAndTime();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editexamform);

		Intent intent = getIntent();
		// get exam if of exam which need to edit
		// if id not found than default value is -1
		dataId = intent.getIntExtra(Constant.RESULT_ID, -1);
		maxQuestion = intent.getIntExtra(Constant.MAX_QUESTIONS, 999);

		// if exam id not found
		if (dataId == -1) {
			Toast.makeText(Edit_Exam_Form.this, "No exam is found.",
					Toast.LENGTH_SHORT).show();
			finish();
		}

		// initialize variables
		initData();
		
		window_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// set no of question limitation
		if (maxQuestion == 999) {
			msg_nofoquestion
					.setText("*Total Questions in exam should between 2 to 999");
		} else if (maxQuestion != 999) {
			msg_nofoquestion
					.setText("*Total Questions in exam should between 2 to "
							+ maxQuestion + ". File has maximum " + maxQuestion
							+ "  questions.");
			msg_nofoquestion.setTextColor(getResources().getColor(
					R.drawable.error_color));
		} else {
			msg_nofoquestion
					.setText("*Total Questions in exam should between 2 to 999");
		}

		// get Exam detail to edit
		try {
			DatabaseHelper database = new DatabaseHelper(context);
			result = database.getExam(dataId);
			
			// set all edit text box from exam value
			setValues();

			ProgressDialog mProgressDialog = null;

			// set no fo maximum question for currently selected file
			try {

				mProgressDialog = new ProgressDialog(context);
				mProgressDialog.setMessage("XML Processing");
				mProgressDialog.show();

				XMLParser parser = null;
				String xml = null;
				Document doc = null;

				parser = new XMLParser();
				xml = null;
				//xml = parser.getXmlFromFile(context, result.getQuestionset());
				xml = result.getQuestionset();
				doc = parser.getDomElement(context, xml,
						R.raw.questionbank);

				// get all item node
				NodeList mNodeList = doc
						.getElementsByTagName(Constant.XML_CONSTANTS.KEY_ITEM);

				// set total no of question
				int questionsInQuestionset = mNodeList.getLength();

				// question set must contain minimum two question
				if (questionsInQuestionset <= 1) {
					Toast.makeText(
							context,
							"Quesion set(XML) Required more than two questions in file.",
							Toast.LENGTH_LONG).show();
					
					msg_nofoquestion
							.setText("*Choose Quesion set(XML) than has minimum two questions in file.");
					maxQuestion = questionsInQuestionset;
					initMaxQuestion = questionsInQuestionset;
					tv_questionset_name.setText("");
					tv_questionset.setText("");
				} else {
					maxQuestion = questionsInQuestionset;
					initMaxQuestion = maxQuestion;
					msg_nofoquestion
							.setText("*Total Questions in exam should between 2 to "
									+ maxQuestion
									+ ". File has maximum "
									+ maxQuestion + " questions.");
				}
			} 
			catch (FileNotFoundException e)
			{
				Log.e("EXCEPTION_NEW_EXAM_FORM", e.toString());
				Toast.makeText(context, "ERROR: XML file can't found.",
						Toast.LENGTH_LONG).show();
			}
			catch(IOException e) 
			{
				Log.e("EXCEPTION_NEW_EXAM_FORM", e.toString());
				Toast.makeText(context, "ERROR: XML file can't read.",
						Toast.LENGTH_LONG).show();
			}
			catch (SAXException e)
			{
				Log.e("EXCEPTION_NEW_EXAM_FORM", e.toString());
				Toast.makeText(context, "ERROR: XML file can't parsed properly.",
						Toast.LENGTH_LONG).show();
			}
			catch (ParserConfigurationException e)
			{
				Log.e("EXCEPTION_NEW_EXAM_FORM", e.toString());
				Toast.makeText(context, "ERROR: XML file is invalid. Validate xml. Go to help for more instruction.",
						Toast.LENGTH_LONG).show();
			}
			catch (Exception e) {
				Log.e("EXCEPTION_NEW_EXAM_FORM", e.toString());
				Toast.makeText(context, "ERROR: XML can't parsed to verify.",
						Toast.LENGTH_LONG).show();
			}
			finally {
				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}
			}
		} catch (Exception e) {
			Log.d("EXCEPTION_EDIT_EXAM_FORM", e.toString());
		}

		// set button listener
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
	
	// initialize variables
	private void initData() {

		// File
		currentDir = null;

		// Dialog
		dialog_browse = null;

		// Adapter
		mFileArrayAdapter = null;

		// Link Edit Text to id
		et_examname = (EditText) findViewById(R.id.et_examname1);
		et_date = (EditText) findViewById(R.id.et_date1);
		et_durationtime = (EditText) findViewById(R.id.et_durationtime1);
		et_noofquestion = (EditText) findViewById(R.id.et_noofquestion1);

		// TextView
		tv_questionset_name = (TextView) findViewById(R.id.tv_questionset1);
		tv_questionset = (TextView) findViewById(R.id.tv_editexam_uri);
		window_heading = (TextView) findViewById(R.id.window_title);
		window_heading.setText("Edit Exam");

		// error text view
		msg_examname = (TextView) findViewById(R.id.msg_examname_edit);
		msg_durationtime = (TextView) findViewById(R.id.msg_durationtime_edit);
		msg_nofoquestion = (TextView) findViewById(R.id.msg_noofquestion_edit);
		msg_questionset = (TextView) findViewById(R.id.msg_browse_edit);
		msg_questionset.setVisibility(View.INVISIBLE);

		// Button
		btn_create = (Button) findViewById(R.id.btn_create1);
		btn_cancel = (Button) findViewById(R.id.btn_cancel1);
		btn_reset = (Button) findViewById(R.id.btn_reset1);

		// Image Button
		btn_browse = (ImageButton) findViewById(R.id.btn_editexam_browse);
		window_back = (ImageButton) findViewById(R.id.window_back);

		// RadioButton
		rdb_limit = (RadioButton) findViewById(R.id.radio_limit1);
		rdb_nolimit = (RadioButton) findViewById(R.id.radio_nolimit1);

		// get date from custom created class DateAndTime
		date = dateandtime.getDate();

		// set edit text layout to date value
		et_date.setText(date);
	}

	private final class OnCheckedChangeListenerImplementation implements
			OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {

			switch (buttonView.getId()) {
			case R.id.radio_nolimit1:
				// on no time limit radio button pressed
				// uncheck time limit
				// disable duration time edit box
				// and set duration to 0
				if (rdb_nolimit.isChecked()) {
					rdb_limit.setChecked(false);
					et_durationtime.setEnabled(false);
					et_durationtime.setText("");
					et_durationtime.setHint("Exam contains no time limit");
					examtype = 1; // exam type 1 means no time limit
					// Log.d("EDIT", "onclick listener radio no limit");
				}
				break;
			case R.id.radio_limit1:
				// on time limit radio button pressed
				// uncheck no time limit
				// enable duration time edit box
				// and set duration to previous value
				if (rdb_limit.isChecked()) {
					rdb_nolimit.setChecked(false);
					et_durationtime.setEnabled(true);
					examtype = 0; // exam type 0 means time limit
					et_durationtime.setText(result.getDurationofexam() + "");
					// Log.d("EDIT", "onclick listener radio limit");
				}
				break;
			}

		}
	}

	// set all edit text value
	private void setValues() {
		try {
			et_examname.setText(result.getExamname().toString());
			if(result.getDurationofexam() == 0)
			{
				et_durationtime.setText("");
			} else {
				et_durationtime.setText(result.getDurationofexam() + "");
			}			
			et_noofquestion.setText(result.getNoofquestion() + "");
			tv_questionset.setText(result.getQuestionset() + "");

			// Extract filename from absolute path
			try {
				String filename = result.getQuestionset().toString();
				String filenameArray[] = filename.split("/");
				filename = filenameArray[filenameArray.length - 1];
				filenameArray = filename.split("\\.");
				filename = "" + filenameArray[0];
				for (int i = 1; i < filenameArray.length - 1; i++) {
					filename = filename + "." + filenameArray[i];
				}
				tv_questionset_name.setText(filename);
			} catch (Exception e) {
				Log.d("EXCEPTION_EDIT_EXAM_FORM", e.toString());
			}

			// set radio button according to exam type
			if (result.getExamtype() == 0) {
				rdb_limit.setChecked(true);
				rdb_nolimit.setChecked(false);
				et_durationtime.setEnabled(true);
				examtype = 0; // exam type 0 means time limit
				et_durationtime.setText(result.getDurationofexam() + "");
				Log.d("EDIT", "set value radio limit");
			} else {
				rdb_limit.setChecked(false);
				rdb_nolimit.setChecked(true);
				et_durationtime.setEnabled(false);
				et_durationtime.setText("");
				et_durationtime.setHint("Exam contains no time limit");
				examtype = 1;
				Log.d("EDIT", "set value radio no limit");
			}
		} catch (Exception e) {
			Log.d("EXCEPTION_EDIT_EXAM_FORM", e.toString());
		}
	}

	// get value from current use for reset
	// trim use to ignore extra white space
	private void getValues() {

		examname = et_examname.getText().toString().trim();
		durationtime = et_durationtime.getText().toString().trim();
		noofquestion = et_noofquestion.getText().toString().trim();

		// store absolute path not file name
		questionset = tv_questionset.getText().toString();

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
		// a accessible directory for get start
		if (currentDir == null) {
			currentDir = mBrowseHelper.getCurrentDir();
		}

		if (currentDir != null) {
			// get all directory folders and xml files
			List<FileDir> dir = mBrowseHelper.fill(currentDir);

			// set dialog title as current directory
			dialog_browse.setTitle(currentDir.getPath());

			// if any valid directory found then set adapter and
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

						// remove extension 
						String filename = mFileDir.getName();
						String[] filenameArray = filename.split("\\.");
						filename = "" + filenameArray[0];
						for (int i = 1; i < filenameArray.length - 1; i++) {
							filename = filename + "." + filenameArray[i];
						}

						tv_questionset_name.setText(filename);
						tv_questionset.setText(mFileDir.getPath());

						dialog_browse.dismiss();

						tv_questionset_name.setText(filename);
						tv_questionset.setText(mFileDir.getPath());
						dialog_browse.dismiss();

						ProgressDialog mProgressDialog = null;
						
						// get no of question in file to set no of question edit box limit
						try {

							mProgressDialog = new ProgressDialog(context);
							mProgressDialog.setMessage("XML Processing");
							mProgressDialog.show();

							XMLParser parser = null;
							String xml = null;
							Document doc = null;

							parser = new XMLParser();
							xml = null;
							//xml = parser.getXmlFromFile(context,
							//		mFileDir.getPath());
							xml = mFileDir.getPath();
							doc = parser.getDomElement(context, xml,
									R.raw.questionbank);

							// get all item node
							NodeList mNodeList = doc
									.getElementsByTagName(Constant.XML_CONSTANTS.KEY_ITEM);

							// set total no of question
							int questionsInQuestionset = mNodeList.getLength();

							// no of question should more than one
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
						}
						catch (FileNotFoundException e)
						{
							Log.e("EXCEPTION_NEW_EXAM_FORM", e.toString());
							Toast.makeText(context, "ERROR: XML file can't found.",
									Toast.LENGTH_LONG).show();
						}
						catch(IOException e) 
						{
							Log.e("EXCEPTION_NEW_EXAM_FORM", e.toString());
							Toast.makeText(context, "ERROR: XML file can't read.",
									Toast.LENGTH_LONG).show();
						}
						catch (SAXException e)
						{
							Log.e("EXCEPTION_NEW_EXAM_FORM", e.toString());
							Toast.makeText(context, "ERROR: XML file can't parsed properly.",
									Toast.LENGTH_LONG).show();
						}
						catch (ParserConfigurationException e)
						{
							Log.e("EXCEPTION_NEW_EXAM_FORM", e.toString());
							Toast.makeText(context, "ERROR: XML file is invalid. Validate xml. Go to help for more instruction.",
									Toast.LENGTH_LONG).show();
						}						
						catch (Exception e) {
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
		case R.id.btn_editexam_browse:

			create_browse_dialog();

			break;

		case R.id.btn_create1:

			// get all edit text value in variables
			getValues();
			// get date and time from custom created DateAndTime Class
			time = dateandtime.getTime();
			date = dateandtime.getDate();

			String msg = "updated"; // for if form is updated then show toast
									// message AND take as flag if msg is change
									// from updated to other value
									// don't update exam since there is some
									// error

			int duration = 0, // take as default value if no value found

			noq = 0; // noq = no of questions

			// set error flag if exam name field is empty
			if (examname.equals("")) {
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
			if (durationtime.equals("") && rdb_nolimit.isChecked())
			{
				duration = 0;
			}
			// set error flag in msg if duration field is empty
			else if (durationtime.equals("") && rdb_limit.isChecked()) {
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
					Log.d("EXCEPTION_EDIT_EXAM_FORM", e.toString());
					msg = "invalid value";
					msg_durationtime.setTextColor(getResources().getColor(
							R.drawable.error_color));
				}

				// for time limit exam type duration time should be
				// greater then 0
				if (duration <= 0 && rdb_limit.isChecked()) {
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
					Log.d("EXCEPTION_EDIT_EXAM_FORM", e.toString());
					msg = "invalid value";
					msg_nofoquestion.setTextColor(getResources().getColor(
							R.drawable.error_color));
				}

				// minimum 2 question required to create exam
				// other wise set error flag
				// and less or equal than total question in file 
				if (noq <= 1 || noq > maxQuestion) {
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
			if (msg.equals("updated")) {
				// update exam to database
				try {
					DatabaseHelper database = new DatabaseHelper(context);
					Exam exam = new Exam(dataId, examname, examtype, date,
							time, duration, noq, questionset);
					// return long id value greater than zero
					// if exam successfully updated
					if (database.update(exam) > 0)
						;
					else {
						msg = "ERROR: Exam can't updated to database";
					}
				} catch (Exception e) {
					Log.d("EXCEPTION_EDIT_EXAM_FORM", e.toString());
					msg = "ERROR: Database can't accessed";
				}
			}

			Toast.makeText(Edit_Exam_Form.this, msg, Toast.LENGTH_SHORT).show();
			if (msg.equals("updated")) {
				finish();
			}

			break;

		// when cancel button press exit activity without any change
		case R.id.btn_cancel1:
			Toast.makeText(Edit_Exam_Form.this, "canceled", Toast.LENGTH_SHORT)
					.show();
			finish();
			break;

		// reset all values to it initial state
		case R.id.btn_reset1:
			msg_examname.setTextColor(getResources().getColor(
					R.drawable.gray_small_text));
			msg_durationtime.setTextColor(getResources().getColor(
					R.drawable.gray_small_text));
			msg_nofoquestion.setTextColor(getResources().getColor(
					R.drawable.gray_small_text));
			msg_questionset.setTextColor(getResources().getColor(
					R.drawable.gray_small_text));
			msg_questionset.setVisibility(View.INVISIBLE);
			maxQuestion = initMaxQuestion;
			if (initMaxQuestion <= 1) {
				msg_nofoquestion
						.setText("*Choose Quesion set(XML) than has minimum two questions in file.");
			} else {
				msg_nofoquestion
						.setText("*Total Questions in exam should between 2 to "
								+ maxQuestion
								+ ". File has maximum "
								+ maxQuestion + "  questions.");
			}
			setValues();
			Toast.makeText(Edit_Exam_Form.this, "reseted", Toast.LENGTH_SHORT)
					.show();
			break;
		}

	}

}
