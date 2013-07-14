package com.pvr.crackit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

public class Splash extends Activity {

	protected static final String TAG = null;
	// initialize active to true (yes)
	protected boolean _active = true;
	// Time for splash
	protected int _splashTime = 5000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		// thread for displaying the SplashScreen
		Thread splashThread = new Thread() {

			@Override
			// Overridden method of thread
			public void run() {

				try {
					int waited = 0;
					// active at 100 milisecond and check touch even and
					// increment
					// waiting time to 100 plus
					while (_active && (waited < _splashTime)) {
						sleep(100);
						if (_active) {
							waited += 100;
						}
					}
				}
				// if any exception is generated during thread
				catch (Exception e) {
					Log.d(TAG, e.toString());
				} finally {
					Log.d(TAG, "Exception not in start activity");
					startActivity(new Intent(Splash.this, Home_Screen.class));
					// stop();
				}

				// super.run();
			}

		};

		// start thread
		splashThread.start();

	}

	@Override
	// On touch event override for skip splash
	public boolean onTouchEvent(MotionEvent event) {
		// if motion down occur set active to false to stop splash
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			_active = false;
		}
		return true;
	}

	@Override
	// event have to occur when activity is paused
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

}
