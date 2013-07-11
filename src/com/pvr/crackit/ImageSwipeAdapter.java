package com.pvr.crackit;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageSwipeAdapter extends PagerAdapter 
{
	   Context context;  
	   private int[] Images ; 
	   ImageSwipeAdapter(Context context, int[] _images)
	   {  
	        this.context=context; 
	        Images = _images;
	   }  
	   @Override  
	   public int getCount() 
	   {  
	    return Images.length;  
	   }  
	   
	   @Override  
	   public boolean isViewFromObject(View view, Object object) 
	   {  
	    return view == ((ImageView) object);  
	   }  
	   @Override  
	   public Object instantiateItem(ViewGroup container, int position) 
	   {  
		    ImageView imageView = new ImageView(context);  
		    //int pad = context.getResources().getDimensionPixelSize(R.dimen.m_padding);
		    int pad = 0;
		    imageView.setPadding(pad, pad, pad, pad);  
		    imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);  
		    imageView.setImageResource(Images[position]);  
		    ((ViewPager) container).addView(imageView, 0);  
		    return imageView;  
	   }  
	   @Override  
	   public void destroyItem(ViewGroup container, int position, Object object) 
	   {  
		   ((ViewPager) container).removeView((ImageView) object);  
	   }  

}
