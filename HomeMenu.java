/*******************************************************************
 * this is the activity of the home menu
 * this is just a lot of stupid UI stuff.. you don't have to worry about 
 * most of it yet. we probably won't change much of this

*/

package com.schen.pop;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class HomeMenu extends Activity {
	Button play_b, hs_b, th_b, opt_b, help_b, exit_b;
	PopView pop_view;
	Typeface typeface;
	Dialog id1;
 	Dialog id3;
 	Dialog id4;
 	Button next1;
 	Button back1;
 	Button back3;
 	Button next3;
 	Button back4;
 	Button play4, soundB;
 	CheckBox cb4;
	static boolean gone = false;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        SharedPreferences sp = getSharedPreferences("hs", 0);
        SharedPreferences.Editor spe = sp.edit();
        spe.putInt("from_game", 0);
        spe.commit();
        setContentView(R.layout.main);
        
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.main_rl);
        
        // Create the adView
        // Please replace MY_BANNER_UNIT_ID with your AdMob Publisher ID
        AdView adView = new AdView(this, AdSize.BANNER,"a14e0a45f64da63");
      
        // Add the adView to it
        layout.addView(adView);
         
        // Initiate a generic request to load it with an ad
        AdRequest request = new AdRequest();
        request.setTesting(true);

        adView.loadAd(request);
        
        Preferences.setFromGame(this, 0);
        typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/font.ttf");
        TextView nameTV = (TextView)findViewById(R.id.name_tv);
        nameTV.setTypeface(typeface);
        play_b = (Button)findViewById(R.id.play);
        hs_b = (Button)findViewById(R.id.high_scores);
        opt_b = (Button)findViewById(R.id.options);
        help_b = (Button)findViewById(R.id.help);
        exit_b = (Button)findViewById(R.id.exit);
        play_b.setTypeface(typeface);
        hs_b.setTypeface(typeface);
        opt_b.setTypeface(typeface);
        help_b.setTypeface(typeface);
        exit_b.setTypeface(typeface);
        final Intent show_hs = new Intent(this, HighScoreView.class);
        play_b.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				start_game();
			}
        });
        hs_b.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		try {
        		startActivity(show_hs);
        		} catch (Exception e) {}
        	}
        });
        opt_b.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showPrefs();
			}
        });
        help_b.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showInstructions();
			}
        });
        exit_b.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
        });
    
        soundB = (Button)findViewById(R.id.sound_ib);
        soundB.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (Preferences.getSound(getApplicationContext())) {
					soundB.setBackgroundResource(R.drawable.sound_off);
					Preferences.setSound(getApplicationContext(), false);
				}
				else {
					soundB.setBackgroundResource(R.drawable.sound_on);
					Preferences.setSound(getApplicationContext(), true);
				}
			}
        });
    }
    
	public void onUserLeaveHint() {
    	if (PopView.updatingScore) {gone = true;}
    }
    
	public void onStart() {
    	super.onStart();
    	FlurryAgent.onStartSession(this, "PEMA7K6EH1PM9JI4HZ5J");
    	if (Preferences.getSound(getApplicationContext())) {soundB.setBackgroundResource(R.drawable.sound_on);}
    	else {soundB.setBackgroundResource(R.drawable.sound_off);}
    }
    
    public void start_game() {
    	final Dialog d = new Dialog(this);
    	d.requestWindowFeature(Window.FEATURE_NO_TITLE);
    	d.setCancelable(true);
    	d.setContentView(R.layout.chose_diff);
    	TextView tvd = (TextView)d.findViewById(R.id.cdtv);
    	tvd.setTypeface(typeface);
    	Button origB = (Button)d.findViewById(R.id.cdb_orig);
    	Button insaneB = (Button)d.findViewById(R.id.cdb_insane);
    	origB.setTypeface(typeface);
    	insaneB.setTypeface(typeface);
    	origB.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				PopView.diff = PopView.ORIG;
				d.dismiss();
				sendStartIntent();
			}
    	});
    	insaneB.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				PopView.diff = PopView.INSANE;
				d.dismiss();
				sendStartIntent();
			}
    	});
    	d.show();
    }
    
    public void sendStartIntent() {
    	Intent start = new Intent(this, PopActivity.class);
    	startActivity(start);
    }
    
    public void showPrefs() {
    	Intent prefs = new Intent(this, Preferences.class);
    	startActivity(prefs);
    }
    
    public void showInstructions() {
    	id1 = new Dialog(this) {
			public boolean onSearchRequested() {
				this.dismiss();
				id3.show();
				return true;
			}
		};
		id1.requestWindowFeature(Window.FEATURE_NO_TITLE);
		id1.setContentView(R.layout.instructions_dialog1);
		id3 = new Dialog(this) {
			public boolean onSearchRequested() {
				this.dismiss();
				id4.show();
				return true;
			}
		};
		id3.requestWindowFeature(Window.FEATURE_NO_TITLE);
		id3.setContentView(R.layout.instructions_dialog3);
		id4 = new Dialog(this) {
			public boolean onSearchRequested() {
				this.dismiss();
				return true;
			}
		};
		id4.requestWindowFeature(Window.FEATURE_NO_TITLE);
		id4.setContentView(R.layout.instructions_dialog4);
		next1 = (Button)id1.findViewById(R.id.id1_next);
		next1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				id1.dismiss();
				id3.show();
			}
		});
		back1 = (Button)id1.findViewById(R.id.id1_back);
		back1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				id1.dismiss();
			}
		});
		back3 = (Button)id3.findViewById(R.id.id3_back);
		back3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				id3.dismiss();
				id1.show();
			}
		});
		next3 = (Button)id3.findViewById(R.id.id3_next);
		next3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				id3.dismiss();
				id4.show();
			}
		});
		back4 = (Button)id4.findViewById(R.id.id4_back);
		back4.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				id4.dismiss();
				id3.show();
			}
		});
		cb4 = (CheckBox)id4.findViewById(R.id.id4_cb);
		play4 = (Button)id4.findViewById(R.id.id4_play);
		play4.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				id4.dismiss();
				Preferences.setInstructions(getBaseContext(), false);
				start_game();
			}
		});
		cb4.setVisibility(View.INVISIBLE);
		id1.show();
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.home_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.report_bug) {
			final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
			emailIntent.setType("plain/text");
			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"antonis.lab1@gmail.com"});
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Space ZAP! (beta): BUG REPORT");
			emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
			this.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
		}
		return true;
	}
	
	@Override
	public void onStop()
	{
	   super.onStop();
	   FlurryAgent.onEndSession(this);
	}
}