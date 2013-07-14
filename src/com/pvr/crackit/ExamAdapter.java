package com.pvr.crackit;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.pvr.common.Constant;
import com.pvr.common.PaperCreateConstant;
import com.pvr.database.DatabaseHelper;
import com.pvr.model.Exam;
import com.pvr.quickaction.ActionItem;
import com.pvr.quickaction.QuickAction;
import com.pvr.xmlmodel.XMLParser;

public class ExamAdapter extends BaseAdapter {

	// Int set id for actions
	private final int QUICK_ACTION_DELETE = 0;
	private final int QUICK_ACTION_EDIT = 1;
	private final int QUICK_ACTION_DETAIL = 2;

	// contain exam list
	private ArrayList<Exam> mCurrentList = null;

	private Context mContext = null;

	// dialog
	Dialog dialog_detail = null;
	Dialog dialog_delete = null;

	// Int
	int index = 0;

	// for exam list
	public ExamAdapter(ArrayList<Exam> mCurrentList, Context mContext) {
		super();
		this.mCurrentList = mCurrentList;
		this.mContext = mContext;
	}

	// get no of exams in list
	@Override
	public int getCount() {
		if (mCurrentList != null)
			return mCurrentList.size();
		else
			return 0;
	}

	// get exam from position
	@Override
	public Exam getItem(int position) {
		if (mCurrentList != null)
			return mCurrentList.get(position);
		else
			return null;
	}

	// get Exam id
	@Override
	public long getItemId(int position) {
		return position;
	}

	// get view for exam list
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final int p = position;

		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.exam_row_layout, null);

		try {

			// set exam name and other action button
			TextView textRow = (TextView) convertView.findViewById(R.id.name);
			TextView tv_detail = (TextView) convertView
					.findViewById(R.id.detail);
			textRow.setText(getItem(p).getExamname());

			String data = getItem(p).getDate();

			// get only file name
			try {
				String filename = getItem(p).getQuestionset().toString();
				String filenameArray[] = filename.split("/");
				filename = filenameArray[filenameArray.length - 1];
				filenameArray = filename.split("\\.");
				filename = "" + filenameArray[0];
				for (int i = 1; i < filenameArray.length - 1; i++) {
					filename = filename + "." + filenameArray[i];
				}
				data = data + "     Question Set : " + filename;
			} catch (Exception e) {
				Log.d("EXCEPTION_EXAM_ADAPTER", e.toString());
			}
			tv_detail.setText(data);
		} catch (Exception e) {
		}

		// plus to get quick action menu
		final ImageButton buttonPlus = (ImageButton) convertView
				.findViewById(R.id.buttonQuickAction);
		// start to start exam
		final ImageButton buttonStart = (ImageButton) convertView
				.findViewById(R.id.btn_examlist_start);

		// Set Quick Action item
		ActionItem itemDetail = new ActionItem(QUICK_ACTION_DETAIL, "",
				mContext.getResources().getDrawable(R.drawable.img_detail1));
		ActionItem itemEdit = new ActionItem(QUICK_ACTION_EDIT, "", mContext
				.getResources().getDrawable(R.drawable.img_edit));
		ActionItem itemDelete = new ActionItem(QUICK_ACTION_DELETE, "",
				mContext.getResources().getDrawable(R.drawable.img_delete));

		// hold all quick action item in one quick action menu
		final QuickAction mQuickAction = new QuickAction(mContext,
				QuickAction.HORIZONTAL);

		mQuickAction.addActionItem(itemDetail);
		mQuickAction.addActionItem(itemEdit);
		mQuickAction.addActionItem(itemDelete);

		// execute on quick action is dismissed or cancel
		mQuickAction.setOnDismissListener(new QuickAction.OnDismissListener() {

			public void onDismiss() {
				buttonPlus.setImageResource(R.drawable.img_properties_normal);
			}
		});

		// on quick action item clicked
		mQuickAction
				.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {

					@Override
					public void onItemClick(QuickAction source, int pos,
							int actionId) {

						buttonPlus
								.setImageResource(R.drawable.img_properties_normal);

						switch (actionId) {

						// show detail dialog
						case QUICK_ACTION_DETAIL:

							try {
								// create detail dialog
								dialog_detail = new Dialog(mContext);
								dialog_detail
										.requestWindowFeature(Window.FEATURE_LEFT_ICON);
								dialog_detail
										.setContentView(R.layout.detaildialog);

								TextView tv_detail_left = (TextView) dialog_detail
										.findViewById(R.id.tv_detail_dialog_left);
								TextView tv_detail_right = (TextView) dialog_detail
										.findViewById(R.id.tv_detail_dialog_right);
								Button btn_detail_ok = (Button) dialog_detail
										.findViewById(R.id.btn_detail_dialog_ok);

								Exam exam = new Exam();
								exam = mCurrentList.get(p);

								// set exam information
								dialog_detail.setFeatureDrawableResource(
										Window.FEATURE_LEFT_ICON,
										R.drawable.img_information_normal);
								dialog_detail.setTitle("  Exam Detail");
								tv_detail_left
										.setText("Exam Name\nExam Type\nCreation Date\nExam Duration\nNo of Questions\nQuestion Set");

								// get detail dialog message text
								String type = null;
								if (exam.getExamtype() == 0) {
									type = "Time Limit";
								} else {
									type = "No Time Limit";
								}

								String message = exam.getExamname().trim()
										+ "\n" + type + "\n"
										+ exam.getDate().trim() + "\n"
										+ exam.getDurationofexam() + "\n"
										+ exam.getNoofquestion() + "\n";

								// get filename from absolute path store in
								// questionset
								try {
									String filename = exam.getQuestionset()
											.toString();
									String filenameArray[] = filename
											.split("/");
									filename = filenameArray[filenameArray.length - 1];
									filenameArray = filename.split("\\.");
									filename = "" + filenameArray[0];
									for (int i = 1; i < filenameArray.length - 1; i++) {
										filename = filename + "."
												+ filenameArray[i];
									}
									message = message + filename;
								} catch (Exception e) {
									Log.d("EXCEPTION_EXAM_ADAPTER",
											e.toString());
								}

								// if exam detail is not null
								if (message != null) {
									tv_detail_right.setText(message);
								}

								// on OK button pressed close dialog
								btn_detail_ok
										.setOnClickListener(new View.OnClickListener() {

											@Override
											public void onClick(View v) {
												dialog_detail.dismiss();

											}
										});

								// display detail dialog
								dialog_detail.show();

							} catch (Exception e) {
								Log.d("EXCEPTION_EXAM_ADAPTER", e.toString());
								Toast.makeText(mContext,
										"ERROR: Detail dialog can't created.",
										Toast.LENGTH_SHORT).show();
							}

							break;

						// start edit exam activity
						case QUICK_ACTION_EDIT:

							Intent intent_edit = new Intent(mContext,
									Edit_Exam_Form.class);

							// put exam id in extras so in edit exam activity
							// exam detail is fetched and updated
							intent_edit.putExtra(Constant.RESULT_ID,
									mCurrentList.get(p).getId());
							intent_edit.putExtra(Constant.MAX_QUESTIONS, 999);

							mContext.startActivity(intent_edit);

							break;

						// ask to deleted exam and delete exam
						case QUICK_ACTION_DELETE:

							try {
								// create delete dialog
								dialog_delete = new Dialog(mContext);
								dialog_delete
										.requestWindowFeature(Window.FEATURE_LEFT_ICON);
								dialog_delete
										.setContentView(R.layout.deletedialog);

								dialog_delete.setCancelable(true);

								dialog_delete.setTitle("  Delete");
								dialog_delete.setFeatureDrawableResource(
										Window.FEATURE_LEFT_ICON,
										R.drawable.img_delete_normal);

								TextView tv_delete = (TextView) dialog_delete
										.findViewById(R.id.tv_delete_dialog);
								tv_delete
										.setText("Do you really want to delete exam?");

								Button btn_yes = (Button) dialog_delete
										.findViewById(R.id.btn_delete_dialog_yes);
								Button btn_no = (Button) dialog_delete
										.findViewById(R.id.btn_delete_dialog_no);

								// on yes button click delete exam
								btn_yes.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View v) {
										Log.d("tag", "deleted");

										try {
											Exam exam = (Exam) mCurrentList
													.get(p);
											// delete from data base
											DatabaseHelper db = new DatabaseHelper(
													mContext);
											db.deleteExam(exam);
											mCurrentList.remove(p);
											notifyDataSetChanged();
											Toast.makeText(mContext, "Deleted",
													Toast.LENGTH_SHORT).show();

										} catch (Exception e) {
											Log.d("EXCELTION_EXAM_ADAPTER",
													e.toString());
											Toast.makeText(
													mContext,
													"ERROR: Database can't accessed",
													Toast.LENGTH_SHORT).show();

										}
										dialog_delete.dismiss();

									}
								});

								// on NO button pressed cancel delete
								btn_no.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View v) {
										Log.d("tag", "cancel");
										dialog_delete.dismiss();

									}
								});

								// show delete dialog
								dialog_delete.show();

							} catch (Exception e) {
								Log.d("EXCEPTION_EXAM_HOME", e.toString());
								Toast.makeText(mContext,
										"ERROR: Delete dialog can't created.",
										Toast.LENGTH_SHORT).show();
							}

							break;

						default:
							break;
						}

					}
				});

		// on action menu click
		if (buttonPlus != null)
			buttonPlus.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// v.setBackgroundResource(R.drawable.plus_selected);
					buttonPlus
							.setImageResource(R.drawable.img_properties_selected);
					mQuickAction.show(v);
					mQuickAction.setAnimStyle(QuickAction.ANIM_AUTO);
				}
			});

		// on exam start button clicked
		if (buttonStart != null)
			buttonStart.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					index = p;

					final Dialog dialog_start = new Dialog(mContext);
					dialog_start.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog_start.setCancelable(true);

					dialog_start.setContentView(R.layout.dialog_start_exam);

					TextView tv_title = (TextView) dialog_start
							.findViewById(R.id.dialog_start_title);
					if (tv_title != null)
						tv_title.setText("Press here to start exam : "
								+ mCurrentList.get(index).getExamname());
					ImageButton ibtn_start = (ImageButton) dialog_start
							.findViewById(R.id.dialog_start_play);

					ibtn_start.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {

							AsyncTask_PaperCreate mAsyncTask_PaperCreate = new AsyncTask_PaperCreate();

							dialog_start.dismiss();
							
							// create exam paper and start exam
								mAsyncTask_PaperCreate
										.execute("Get Questions from XML & Load it into the Answer Sheet");

						}
					});

					dialog_start.show();
				}
			});

		return convertView;
	}

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

		int errorCode = -1; // for describe which kind of error generated
		int questionsInQuestionset = 0; // for store total no question in xml
										// file

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// create process dialog
			mProgressDialog = new ProgressDialog(mContext);
			mProgressDialog.setMessage(Constant.TEXT_LOADING);
			mProgressDialog.show();
		}

		@Override
		synchronized protected Boolean doInBackground(String... params) {
			try {

				XMLParser parser = null;
				String xml = null;
				Document doc = null;

				parser = new XMLParser();
				xml = null;
				// xml = parser.getXmlFromFile(mContext, mCurrentList.get(index)
				// .getQuestionset());
				xml = mCurrentList.get(index).getQuestionset();
				doc = parser.getDomElement(mContext, xml, R.raw.questionbank);

				if (doc == null) {
					return false;
				}

				// get all item node
				NodeList mNodeList = doc
						.getElementsByTagName(Constant.XML_CONSTANTS.KEY_ITEM);

				// set total no of question
				questionsInQuestionset = mNodeList.getLength();

				if (questionsInQuestionset <= 1) {
					errorCode = 2; // for initially less question in question
									// set
					return false;
				}

				// check whether xml file has sufficent question for exam
				if (mCurrentList.get(index).getNoofquestion() <= questionsInQuestionset) {

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

					for (int j = 0; j < mCurrentList.get(index)
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
				else if (mCurrentList.get(index).getNoofquestion() > mNodeList
						.getLength()) {
					errorCode = 1; // 1 for less question in xml compate to
									// noofquestion in exam
					return false;
				} else {
					errorCode = -1; // -1 other errors
					return false;
				}

			}

			catch (FileNotFoundException e) {
				Log.e("EXCEPTION_NEW_EXAM_FORM", e.toString());
				errorCode = 91; // for file not found
				return false;

			} catch (IOException e) {
				Log.e("EXCEPTION_NEW_EXAM_FORM", e.toString());
				errorCode = 92; // file can not read
				return false;

			} catch (SAXException e) {
				Log.e("EXCEPTION_NEW_EXAM_FORM", e.toString());
				errorCode = 93; // for file can not parsed
				return false;

			} catch (ParserConfigurationException e) {
				Log.e("EXCEPTION_NEW_EXAM_FORM", e.toString());
				errorCode = 94;
				return false;
			} catch (Exception e) {
				Log.d("EXCEPTION_EXAM_ADAPTER", e.toString());
				// Toast.makeText(mContext, " Xml File cann't parsed.",
				// Toast.LENGTH_LONG).show();
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (mProgressDialog != null) {
				// dismiss progress dialog
				mProgressDialog.dismiss();
			}

			// if result is true start exam
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
						mCurrentList.get(index));

				Intent mIntent_ExamScreen = new Intent(mContext,
						Exam_Screen.class);
				mIntent_ExamScreen.putExtra(Constant.XML_CONSTANTS.KEY_BUNDLE,
						mPaperCreateConstant.mBundle);

				mContext.startActivity(mIntent_ExamScreen);

			} else {
				// error generated show message
				if (errorCode == -1) {
					Toast.makeText(
							mContext,
							"ERROR: Problem in parsing Question Set(XML) Or Question set(XML) is not found.",
							Toast.LENGTH_LONG).show();
				}
				// if xml has less no of questions than exam required
				else if (errorCode == 1) {
					Toast.makeText(
							mContext,
							"Total questions in Question Set(XML) :"
									+ questionsInQuestionset
									+ "\nPlease change no of question in EXAM.",
							Toast.LENGTH_LONG).show();

					Intent intent = new Intent(mContext, Edit_Exam_Form.class);
					intent.putExtra(Constant.RESULT_ID, mCurrentList.get(index)
							.getId());
					intent.putExtra(Constant.MAX_QUESTIONS,
							questionsInQuestionset);
					mContext.startActivity(intent);
				} else if (errorCode == 2) {
					Toast.makeText(
							mContext,
							"ERROR: Question Set(XML) Should have minimum 2 questions.",
							Toast.LENGTH_LONG).show();
				} else if (errorCode == 91) {
					Toast.makeText(mContext, "ERROR: XML file can't found.",
							Toast.LENGTH_LONG).show();
				} else if (errorCode == 92) {
					Toast.makeText(mContext, "ERROR: XML file can't read.",
							Toast.LENGTH_LONG).show();
				} else if (errorCode == 93) {
					Toast.makeText(mContext,
							"ERROR: XML file can't parsed properly.",
							Toast.LENGTH_LONG).show();
				} else if (errorCode == 94) {
					Toast.makeText(
							mContext,
							"ERROR: XML file is invalid. Validate xml. Go to help for more instruction.",
							Toast.LENGTH_LONG).show();
				}
			}
		}
	}

	String formateQuestionNumber(int n) {
		if (n < 10)
			return "  " + n;
		else if (n < 100)
			return " " + n;
		else
			return "" + n;
	}

}
