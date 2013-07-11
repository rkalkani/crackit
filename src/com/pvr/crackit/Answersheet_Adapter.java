package com.pvr.crackit;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Answersheet_Adapter extends BaseAdapter{
	
	Context mContext;
	ArrayList<String> mCurrentList;
	
	public Answersheet_Adapter(ArrayList<String> mCurrentList, Context mContext) {
		super();
		this.mCurrentList = mCurrentList;
		this.mContext = mContext;
	}
	
	@Override
	public int getCount() {
		if(mCurrentList != null)
			return mCurrentList.size();
		else
			return 0;
	}

	@Override
	public String getItem(int position) {
		if(mCurrentList != null)
			return mCurrentList.get(position);
		else
			return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final int p = position;
		
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.answersheet_row_layout, null);
		
		try {
			TextView tv_title1 = (TextView) convertView.findViewById(R.id.tv_answersheet_title1);
			//TextView tv_title2 = (TextView) convertView.findViewById(R.id.tv_answersheet_title2);
			tv_title1.setText(mCurrentList.get(p));
			if (mCurrentList.get(p).equalsIgnoreCase("\t\tCorrect Answers")
					|| mCurrentList.get(p).equalsIgnoreCase("\t\tWrong Answers")
					|| mCurrentList.get(p).equalsIgnoreCase("\t\tSkipped Questions")
					|| mCurrentList.get(p).equalsIgnoreCase("\t\tExam Detail")
					|| mCurrentList.get(p).equalsIgnoreCase("\t\tAnswer Sheet"))
			{
				//convertView.setBackgroundColor(R.drawable.light_blue);
				tv_title1.setTextColor(mContext.getResources().getColor(
						R.drawable.dark_holo_red));
			} 
		} catch (Exception e) {
		}
		
		
		return convertView;
	}

}
