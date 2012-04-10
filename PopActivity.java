/**************************************************************
 * This activity is the activity that holds the PopView view.
 * It is the activity that is on when the game is being played
 * 
 */

package com.schen.pop;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.schen.pop.PopView.PopThread;

public class PopActivity extends Activity {
	
	Resources res;
	static PopThread popThread;
	static TextView scoreView;
	Typeface type;
	
	ImageView[] iViews = new ImageView[3];
	ImageView lives1;
	ImageView lives2;
	ImageView lives3;
	static PopView pop_view;
	CustomReceiver c;
	
	static boolean gone = false;
	
	//onCreate is the first method that is called when an Android Activity is started.. its like a constructor for a java class
	@Override
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		c = new CustomReceiver();
		this.registerReceiver(c, new IntentFilter(Intent.ACTION_BATTERY_LOW));
		System.gc();
		setContentView(R.layout.pop_layout);
		scoreView = (TextView)findViewById(R.id.score_tv);
		type = Typeface.createFromAsset(getBaseContext().getAssets(), "fonts/font.ttf");
		scoreView.setTypeface(type);
		lives1 = (ImageView)findViewById(R.id.ivl);
		lives2 = (ImageView)findViewById(R.id.ivc);
		lives3 = (ImageView)findViewById(R.id.ivr);
		iViews[0] = lives1;
		iViews[1] = lives2;
		iViews[2] = lives3;
		
		//get the view ready
		pop_view = (PopView)findViewById(R.id.pv);
		//give the view a handle to this activity
		pop_view.setActivity(this);
		pop_view.setTextViews(scoreView);
		pop_view.setImageViews(iViews);
		//get a handle to the games thread
		popThread = pop_view.getThread();
		System.gc();
	}
	
	public static void PAUSE_FROM_BROADCAST_RECEIVER_ONLY() {popThread.setState(PopThread.STATE_PAUSED);}
	
	//these next few methods just take care of activity stuff. like what happens when the user presses home
	//or back or search during the game
	@Override
	public boolean onSearchRequested() {return false;}
	
	@Override
	public void onStart() {
		super.onStart();
		System.gc();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(c);
	}

	@Override
	public void onUserLeaveHint() {
		popThread.setState(PopThread.STATE_LOSE);
		popThread.run = false;
		this.finish();
		PopActivity.gone = true;
		super.onUserLeaveHint();
	}
	
	@Override
	public void onBackPressed() {
		if (popThread.canBack == false) return;
		if (popThread.state == PopThread.STATE_PAUSED) {
			popThread.setRunning(PopThread.FALSE);
			popThread.setState(PopThread.STATE_LOSE);
			endGame();
		}
		else {popThread.setState(PopThread.STATE_PAUSED);}
	}
	
	public void endGame() {this.finish();}
}