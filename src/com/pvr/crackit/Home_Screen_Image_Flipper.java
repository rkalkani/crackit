package com.pvr.crackit;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

public class Home_Screen_Image_Flipper  extends Activity
{
	private int[] Images;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_screen);
		
		initData();
		
		ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);  
		   ImageSwipeAdapter adapter = new ImageSwipeAdapter(this, Images);  
		   viewPager.setAdapter(adapter);
	}

	private void initData() {

		Images = new int[] {
				R.drawable.a,
				R.drawable.b
		};
	}
	
	
}
