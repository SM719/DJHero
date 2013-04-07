package com.group15.djhero;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/*
 * Class to detect swipes on pages
 */
public class SwipeDetector implements OnTouchListener {
 
    public static final int DIRECTION_SWIPE_LEFT = 0;
    public static final int DIRECTION_SWIPE_RIGHT = 1;
    public static final int DIRECTION_SWIPE_UP = 2;
    public static final int DIRECTION_SWIPE_DOWN = 3;
 
    private float startX, startY;
    private float MIN_SWIPE_DISTANCE = 100;

 
    private Activity activity;
    private Fragment fragment;
    
    //constructor when called swipe from an activity
    public SwipeDetector(Activity activity){
    	this.activity = activity;
    }
    
    //constructor when swipe called from fragment
    public SwipeDetector(Fragment fragment){
    	this.fragment = fragment;
    }

 
    //when the user touches the screen
    @Override
    public boolean onTouch(View view, MotionEvent event)
    {
        // if no listener has been registered, ignore the event.
 
    	//get position of touch of user
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            startX = event.getRawX();
            startY = event.getRawY();
            return true;
        }
 
        //when user raises finger, get position and calculate distance moved, if user moves more than set minimum do swipe action
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            float endX, endY;
            endX = event.getRawX();
            endY = event.getRawY();
 
            float swipeDistance = (float)Math.sqrt(Math.pow(startX - endX, 2) + Math.pow(startY - endY, 2));
 
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
 
    //if user is on music player then go to dj
    private void swipeRight() {
    	if (activity instanceof MainScreen){
    		((MainScreen)activity).goToDJ();
    	}
    	}

    //if user is on dj then go to music player
	private void swipeLeft() {
    	if (activity instanceof DJInterface){
		((DJInterface)activity).goToMusic();
    	}
	}
    
	//if user is on fragment 1 (turn table 1) then fast forward song 1 else fast forward song 2
    private void swipeUp(){
    	if(fragment instanceof fragment1){
    		((fragment1) fragment).forwardFrag1();
    		
    	}
    	else if(fragment instanceof fragment2){
    		((fragment2) fragment).forwardFrag2();
    	}
	}
    
	//if user is on fragment 1 (turn table 1) then rewind song 1 else rewind song 2
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
				Thread.sleep(1000);
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
				Thread.sleep(1000);
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
