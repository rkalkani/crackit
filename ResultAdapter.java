package com.pvr.crackit;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pvr.model.Result;

public class ResultAdapter extends ArrayAdapter<Result> {

	private Context context;
	int rowViewResourceId;
	List<Result> results = new ArrayList<Result>();
	List<Integer> checklist = new ArrayList<Integer>();
	ResultHolder holder =null;

	public ResultAdapter(Context _context, int _rowViewResourceId,List<Result> _results, List<Integer> _checklist) {
		super(_context, _rowViewResourceId, _results);
		this.context = _context;
		this.rowViewResourceId = _rowViewResourceId;
		this.results = _results;
		this.checklist = _checklist;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		View row = convertView;
		final Integer index = position;

		if (row == null) 
		{
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(rowViewResourceId, parent, false);
			holder = new ResultHolder();
			holder.tv_result_title = (TextView) row.findViewById(R.id.tv_result_title);
			holder.tv_result_subtitle = (TextView) row.findViewById(R.id.tv_result_subtitle);
			holder.tv_result_right_subtitle = (TextView) row.findViewById(R.id.tv_result_right_subtitle);
			holder.cb_result_checkbox = (CheckBox) row.findViewById(R.id.cb_result_list_right);
			holder.RLay_ListView_Result = (RelativeLayout)row.findViewById(R.id.Rly_ListView_Result);

			holder.RLay_ListView_Result.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast.makeText(context, ""+position, Toast.LENGTH_SHORT).show();
					holder.cb_result_checkbox.setChecked(true);
				}
			});
			holder.cb_result_checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() 
			{
						@Override
						public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) 
						{
							switch (buttonView.getId()) 
							{
							case R.id.cb_result_list_right:
								if (isChecked == true) 
								{
									checklist.add(index);
								} 
								else 
								{
									checklist.remove(index);
								}
								if (checklist.size() == 0) 
								{
									Result_Home.btn_deleteresult.setEnabled(false);
									Result_Home.btn_detailresult.setEnabled(false);
								} 
								else if (checklist.size() == 1) 
								{
									Result_Home.btn_deleteresult.setEnabled(true);
									Result_Home.btn_detailresult.setEnabled(true);
								} 
								else if (checklist.size() > 0) 
								{
									Result_Home.btn_deleteresult.setEnabled(true);
									Result_Home.btn_detailresult.setEnabled(false);
								}
								break;
							}
						}
					});
			row.setTag(holder);
		} 
		else 
		{
			holder = (ResultHolder) row.getTag();
		}

		Result result = results.get(position);
		holder.tv_result_title.setText(result.getExamname());
		holder.tv_result_subtitle.setText("Percentage : "+ result.getPercentage());
		holder.tv_result_right_subtitle.setText(result.getDate() + "  "+ result.getTime());
		return row;
	}

	static class ResultHolder 
	{
		TextView tv_result_title;
		TextView tv_result_subtitle;
		TextView tv_result_right_subtitle;
		CheckBox cb_result_checkbox;
		RelativeLayout RLay_ListView_Result;
	}
}
