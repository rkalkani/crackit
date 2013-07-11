package com.pvr.crackit;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class TabActivity_viewgroup extends android.app.TabActivity {
	
	// Tab Host to hold tab host
	private TabHost tabHost = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		//  Set Tab view 
		setContentView(R.layout.xtabs);
		
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.windowbar);
		
		// declare Tab spec for contain tab information
		TabHost.TabSpec spec;
		Intent intent;
		
		// get Tab Host
		this.tabHost = getTabHost();
		
		 //tabHost = getTabHost();
		
		//addTab("", R.drawable.img_tab_home, Home_Screen_Image_Flipper.class);
	    //addTab("", R.drawable.img_tab_exam, ExamTab_GroupActivity.class);
	    //addTab("", R.drawable.img_tab_result, ResultTab_GroupActivity.class);
	    //addTab("", R.drawable.img_tab_help, Help_Screen.class);

		// Add Home tab
		intent = new Intent().setClass(this, Home_Screen_Image_Flipper.class);
		//spec = tabHost.newTabSpec("Home").setIndicator("Home", getResources().getDrawable(R.drawable.home)).setContent(intent);
		spec = tabHost.newTabSpec("Home").setIndicator("Home").setContent(intent);
		tabHost.addTab(spec);
		
		// Add Exam Tab
		intent = new Intent().setClass(this, ExamTab_GroupActivity.class);
		spec = tabHost.newTabSpec("Exam").setIndicator("Exam").setContent(intent);
		tabHost.addTab(spec);
		// Add Result Tab
		intent = new Intent().setClass(this, ResultTab_GroupActivity.class);
		spec = tabHost.newTabSpec("Result").setIndicator("Result").setContent(intent);
		tabHost.addTab(spec);
		
		// Add Help Tab
		intent = new Intent().setClass(this, Help_Screen.class);
		spec = tabHost.newTabSpec("Help").setIndicator("Help").setContent(intent);
		tabHost.addTab(spec);

		// Setting onclicklistener for Exam
		tabHost.getTabWidget().getChildAt(1).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				// Execute if block if clicked tab is already opened
				if (tabHost.getCurrentTab() == 1) 
				{
					// Remove all replace view and set initial view by remove all view from history except initial view
					while (true) {
						if (ExamTab_GroupActivity.history.size() > 1) {
							ExamTab_GroupActivity.history.remove(ExamTab_GroupActivity.history.size() - 1);
							
							// ExamTab_GroupActivity.group gives context
							ExamTab_GroupActivity.group.setContentView(ExamTab_GroupActivity.history.get(ExamTab_GroupActivity.history.size() - 1));
						} else {
							break;
						}
					}
				
				} 
				// Open Clicked tab view
				else {
					tabHost.setCurrentTab(1);
				}
			}
		});
		
		//Setting onclicklistener for Result
		tabHost.getTabWidget().getChildAt(2).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				// Execute below code if clicked tab is already opened
				if (tabHost.getCurrentTab() == 2) 
				{
					// Remove all replace view and set initial view by remove all view from history except initial view
					while (true) {
						if (ResultTab_GroupActivity.history.size() > 1) {
							ResultTab_GroupActivity.history.remove(ResultTab_GroupActivity.history.size() - 1);
							
							// ResultTab_GroupActivity.group gives context
							ResultTab_GroupActivity.group.setContentView(ResultTab_GroupActivity.history.get(ResultTab_GroupActivity.history.size() - 1));
						} else {
							break;
						}
					}
				
				} 
				// Open Clicked tab view
				else {
					tabHost.setCurrentTab(2);
				}
			}
		});
	}
	
	//private void addTab(String labelId, int drawableId, Class<?> targetClass) {
	//  
	//    Intent intent = new Intent(this, targetClass);
	//    TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);

//	    View tabIndicator = LayoutInflater.from(this).inflate(
//	            R.layout.tab, getTabWidget(), false);
//	    TextView title = (TextView) tabIndicator.findViewById(R.id.tab_title);
//	    title.setText(labelId);
//	    ImageView icon = (ImageView) tabIndicator.findViewById(R.id.tab_icon);
//	    icon.setImageResource(drawableId);
//
//	    tabIndicator.setBackgroundResource(R.drawable.white);
//	    
//	    spec.setIndicator(tabIndicator);
//	    spec.setContent(intent);
//	    tabHost.addTab(spec);
//
//	}
	
}
