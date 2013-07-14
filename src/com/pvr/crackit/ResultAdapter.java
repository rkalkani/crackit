package com.pvr.crackit;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
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

import com.pvr.database.DatabaseHelperResult;

public class ResultAdapter extends BaseAdapter {

	private ArrayList<com.pvr.model.Result> mCurrentList = null;
	private Context mContext = null;

	Dialog dialog_detail = null;
	Dialog dialog_delete = null;

	// create result list adapter
	public ResultAdapter(ArrayList<com.pvr.model.Result> resultlist,
			Context mContext) {
		super();
		this.mCurrentList = resultlist;
		this.mContext = mContext;
	}

	// get size of list
	@Override
	public int getCount() {
		if (mCurrentList != null)
			return mCurrentList.size();
		else
			return 0;
	}

	// get result from position
	@Override
	public com.pvr.model.Result getItem(int position) {
		if (mCurrentList != null)
			return mCurrentList.get(position);
		else
			return null;
	}

	// get result id
	@Override
	public long getItemId(int position) {
		return position;
	}

	// get View
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final int p = position;

		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.result_row_layout, null);

		try {
			// set result list data
			TextView textRow = (TextView) convertView
					.findViewById(R.id.tv_resultlist_name);
			TextView tv_detail = (TextView) convertView
					.findViewById(R.id.tv_resultlist_detail);
			textRow.setText(getItem(p).getExamname());

			String data = getItem(p).getDate();
			data = data + "   Right Answers : "
					+ formateQuestionNumber(getItem(p).getRightans())
					+ "   Wrong Answers : "
					+ formateQuestionNumber(getItem(p).getWrongans())
					+ "   Skip Questions : "
					+ formateQuestionNumber(getItem(p).getSkipans());
			
			
			tv_detail.setText(data);
		} catch (Exception e) {
			Log.d("EXCEPTION_RESULT_ADAPTER", e.toString());
		}

		final ImageButton buttonDelete = (ImageButton) convertView
				.findViewById(R.id.btn_resultlist_delete);
		final ImageButton buttonDetail = (ImageButton) convertView
				.findViewById(R.id.btn_resultlist_detail);

		// give result detail
		if (buttonDetail != null)
			buttonDetail.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					try {
						// create detail dialog
						dialog_detail = new Dialog(mContext);

						dialog_detail
								.requestWindowFeature(Window.FEATURE_LEFT_ICON);
						dialog_detail.setContentView(R.layout.detaildialog);

						// get single selected result detail for detail data
						com.pvr.model.Result result = mCurrentList.get(p);

						String type = null;

						// extract exam type to string
						if (result.getExamtype() == 0) {
							type = "Time Limit";
						} else {
							type = "No Time Limit";
						}

						// create string message contain result detail
						String message = result.getExamname().trim() + "\n"
								+ type + "\n" + result.getDurationofexam()
								+ " minitue\n" + result.getNoofquestion() + "\n"
								+ result.getDate() + "  " + result.getTime()
								+ "\n";

						// extract file name from absolute path
						try {
							String filename = result.getQuestionset()
									.toString();
							String filenameArray[] = filename.split("/");
							filename = filenameArray[filenameArray.length - 1];
							filenameArray = filename.split("\\.");
							filename = "" + filenameArray[0];
							for(int i = 1; i < filenameArray.length - 1; i++)
							{
								filename = filename + "." + filenameArray[i]; 
							}
							message = message + filename + "\n";
						} catch (Exception e) {
							Log.d("EXCEPTION_RESULT_ADAPTER", e.toString());
						}

						message = message //+ result.getRightans() + "\n"
								//+ result.getWrongans() + "\n"
								//+ result.getSkipans();// + "\n"
								+ result.getPercentage();

						// set dialog title
						dialog_detail.setTitle("  Exam Detail");
						dialog_detail.setFeatureDrawableResource(
								Window.FEATURE_LEFT_ICON,
								R.drawable.img_information_normal);

						// Left text view for display data type
						TextView tv_detail_left = (TextView) dialog_detail
								.findViewById(R.id.tv_detail_dialog_left);
						// Right text view display Exam detail
						TextView tv_detail_right = (TextView) dialog_detail
								.findViewById(R.id.tv_detail_dialog_right);
						// for dismiss dialog
						Button btn_detail_ok = (Button) dialog_detail
								.findViewById(R.id.btn_detail_dialog_ok);

						// set left side text view
						tv_detail_left.setText("Exam Name" + "\nExam Type"
								+ "\nExam Duration" + "\nNo of Questions"
								+ "\nAttempt Date and Time"
								+ "\nQuestion Set"//+ "\nRight Answers"
								//+ "\nWrong Answers" + "\nSkipped Questions");
								+ "\nPercentage");

						// set result detail on right side
						if (message != null) {
							tv_detail_right.setText(message);
						}

						// OK button for dismiss dialog
						btn_detail_ok
								.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View v) {
										dialog_detail.dismiss();
									}
								});
						dialog_detail.show();

					} catch (Exception e) {
						Log.d("EXCEPTION_RESULT_ADAPTER", e.toString());
					}

				}
			});
		
		// to delete exam from database
		if (buttonDelete != null)
			buttonDelete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					try {

						// create dialog
						dialog_delete = new Dialog(mContext);
						dialog_delete
								.requestWindowFeature(Window.FEATURE_LEFT_ICON);
						dialog_delete.setContentView(R.layout.deletedialog);

						// dialog can not cancel on back button pressed
						dialog_delete.setCancelable(true);

						// set dialog detail
						dialog_delete.setTitle("  Delete");
						dialog_delete.setFeatureDrawableResource(
								Window.FEATURE_LEFT_ICON,
								R.drawable.img_delete_normal);

						TextView tv_delete = (TextView) dialog_delete
								.findViewById(R.id.tv_delete_dialog);

						tv_delete
								.setText("Do you really want to delete result?");

						// button for delete result
						Button btn_yes = (Button) dialog_delete
								.findViewById(R.id.btn_delete_dialog_yes);
						// button for cancel delete result
						Button btn_no = (Button) dialog_delete
								.findViewById(R.id.btn_delete_dialog_no);

						btn_yes.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								try {
									
									com.pvr.model.Result result_delete = mCurrentList
											.get(p);

									// delete from database
									DatabaseHelperResult database = new DatabaseHelperResult(
											mContext);

									database.deleteResult(result_delete);
									mCurrentList.remove(p);
									notifyDataSetChanged();
									Toast.makeText(mContext, "Deleted",
											Toast.LENGTH_SHORT).show();

								} catch (Exception e) {
									Log.d("EXCELTION_RESULT_ADAPTER",
											e.toString());
									Toast.makeText(mContext,
											"ERROR: Database can't accessed",
											Toast.LENGTH_SHORT).show();

								}
								dialog_delete.dismiss();

							}
						});
						
						// no for cancel delete
						btn_no.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								dialog_delete.dismiss();

							}
						});

						// show dialog
						dialog_delete.show();

					} catch (Exception e) {
						Log.d("EXCEPTION_RESULT_ADAPTER", e.toString());
					}

				}
			});

		return convertView;
	}
	
	// to transform number into three digit space
	String formateQuestionNumber(int n)
	{
		if( n < 10)
			return "  " + n;
		else if( n < 100)
			return " " + n;
		else 
			return "" + n;
	}

}
