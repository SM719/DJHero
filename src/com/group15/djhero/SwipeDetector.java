package com.group15.djhero;

import com.group15.djhero.DJInterface.DjProgressDialog;
import com.group15.djhero.DJInterface.DownloadImages;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class SwipeDetector implements OnTouchListener {
 
    public static final int DIRECTION_SWIPE_LEFT = 0;
    public static final int DIRECTION_SWIPE_RIGHT = 1;
    public static final int DIRECTION_SWIPE_UP = 2;
    public static final int DIRECTION_SWIPE_DOWN = 3;
 
    private long downTime;
    private float startX, startY;
    private float MIN_SWIPE_DISTANCE = 100;

 
    private Activity activity;
    private Fragment fragment;
    
    public SwipeDetector(Activity activity){
    	this.activity = activity;
    }
    
    public SwipeDetector(Fragment fragment){
    	this.fragment = fragment;
    }

 
    @Override
    public boolean onTouch(View view, MotionEvent event)
    {
        // if no listener has been registered, ignore the event.
 
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            downTime = System.currentTimeMillis();
            startX = event.getRawX();
            startY = event.getRawY();
            return true;
        }
 
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            float endX, endY;
            endX = event.getRawX();
            endY = event.getRawY();
 
            long upTime = System.currentTimeMillis();
            long swipeTime = (upTime - downTime) / 1000;
 
            float swipeDistance = (float)Math.sqrt(Math.pow(startX - endX, 2) + Math.pow(startY - endY, 2));
            float velocity = swipeDistance / swipeTime;
 
            if(swipeDistance >= MIN_SWIPE_DISTANCE)
            {
                float dx = Math.abs(startX - endX);
                float dy = Math.abs(startY - endY);
               
                //See if the swipe was up/down movement
                if(dy > dx)
                {
                    if (startY > endY)
                    {
                    	swipeUp();
                    }
                    else
                    {
                    	swipeDown();
                    }
                    }
               //If the swipe was a left/right movement
                else
                {
                    if (startX > endX)
                    	swipeRight();
                    else
                    {
                    	swipeLeft();
                    }
                }
            }
 
            return true;
        }
 
        return false;
    }
 
    private void swipeRight() {
		// TODO Auto-generated method stub
    	if (activity instanceof MainScreen){
    		((MainScreen)activity).goToDJ();
    	}
    	}

	private void swipeLeft() {
		// TODO Auto-generated method stub
    	if (activity instanceof DJInterface){
		((DJInterface)activity).goToMusic();
    	}
	}
    
    private void swipeUp(){
    	if(fragment instanceof fragment1){
    		((fragment1) fragment).forwardFrag1();
    		
    	}
    	else if(fragment instanceof fragment2){
    		((fragment2) fragment).forwardFrag2();
    	}
	}
    
    private void swipeDown(){
    	if(fragment instanceof fragment1){
    		((fragment1) fragment).rewindFrag1();
    		((fragment1) fragment).turnccw();
    		new RotateWait1().execute();
    	}
    	
    	else if(fragment instanceof fragment2){
    		((fragment2) fragment).rewindFrag2();
    		((fragment2) fragment).turnccw();
    		new RotateWait2().execute();
    	}
	}
    
    class RotateWait1 extends AsyncTask<Void, Void, Integer> {

    	@Override
		protected Integer doInBackground(Void... arg0) {

    		try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 0;

		}

		@Override
		protected void onPostExecute(Integer result) {
	    		((fragment1) fragment).turncw();
		}
    }
    
    class RotateWait2 extends AsyncTask<Void, Void, Integer> {

    	@Override
		protected Integer doInBackground(Void... arg0) {

    		try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 0;

		}

		@Override
		protected void onPostExecute(Integer result) {
	    		((fragment2) fragment).turncw();
		}
    }
}
