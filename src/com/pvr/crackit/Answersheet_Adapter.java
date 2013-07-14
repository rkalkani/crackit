package com.pvr.crackit;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Answersheet_Adapter extends BaseAdapter {

	Context mContext = null;
	ArrayList<String> mCurrentList = null; // store answersheet list

	// used in create answer sheet list
	public Answersheet_Adapter(ArrayList<String> mCurrentList, Context mContext) {
		super();
		this.mCurrentList = mCurrentList;
		this.mContext = mContext;
	}

	// give list size
	@Override
	public int getCount() {
		if (mCurrentList != null)
			return mCurrentList.size();
		else
			return 0;
	}

	// give current item from list
	@Override
	public String getItem(int position) {
		if (mCurrentList != null)
			return mCurrentList.get(position);
		else
			return null;
	}

	// give list id
	@Override
	public long getItemId(int position) {
		return position;
	}

	// to get view for list item
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final int p = position;

		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.answersheet_row_layout, null);

		try {
			// display answer sheet detail
			TextView tv_title1 = (TextView) convertView
					.findViewById(R.id.tv_answersheet_title1);
			tv_title1.setText(mCurrentList.get(p));

			// set different color for heading
			if (mCurrentList.get(p).equalsIgnoreCase("\t\tCorrect Questions")
					|| mCurrentList.get(p)
							.equalsIgnoreCase("\t\tWrong Questions")
					|| mCurrentList.get(p).equalsIgnoreCase(
							"\t\tSkipped Questions")
					|| mCurrentList.get(p).equalsIgnoreCase("\t\tAnswer Sheet")) {
				tv_title1.setTextColor(mContext.getResources().getColor(
						R.drawable.dark_holo_red));
			}
		} catch (Exception e) {
			Log.d("EXCEPTION_ANSWERSHET_ADAPTER", e.toString());
		}

		return convertView;
	}

}
