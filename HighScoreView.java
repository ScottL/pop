package com.schen.pop;

import java.util.Arrays;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.scoreninja.adapter.ScoreNinjaAdapter;

public class HighScoreView extends Activity {
	private ScoreNinjaAdapter snjOrig, snjInsane;
	String[] highScores = new String[10];
	ListView lv;
	SharedPreferences prefsHSV;
	SharedPreferences.Editor editorHSV;
	Typeface type;
	String fk;
	Dialog scoreDialog;
	Spinner spinner;
	Button scoreBT;
	boolean dialogOpen = false;
	int isHighScore;
	EditText yourNameET;
	byte currentDiff = 0;
	int pos, score;
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		currentDiff = PopView.diff;
		snjOrig = new ScoreNinjaAdapter(this, "pop-original", "9ABB360B3AB6C7651B23B8E144685F7C");
		snjInsane = new ScoreNinjaAdapter(this, "pop-hard", "EB5B512DBD7F5BBFA3CDA50BEF8C993D");
		setContentView(R.layout.high_scores);
		type = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/font.ttf");
		TextView highScores = (TextView)findViewById(R.id.hs_textview);
		highScores.setTypeface(type);
		spinner = (Spinner)findViewById(R.id.diff_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.diff_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				showScores((byte)arg2);
			}
			public void onNothingSelected(AdapterView<?> arg0) {
				showScores(PopView.ORIG);
			}
		});
		Button goHome = (Button)findViewById(R.id.go_home_b);
		goHome.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		lv = (ListView)findViewById(R.id.hs_list);
		Settings.load(currentDiff);
		spinner.setSelection(currentDiff);
		showScores(currentDiff);
		isHighScore = Preferences.getHS(getApplicationContext());
		if (Preferences.getFromGame(getApplicationContext()) == 1) {
			{
				dialogOpen = true;
				score = Preferences.getCurrentScore(getApplicationContext());
				pos = Preferences.getPos(getApplicationContext());
				Preferences.setFromGame(getApplicationContext(), 0);
				Preferences.setHS(getApplicationContext(), 0);
				String name = Preferences.getName(getApplicationContext());
				scoreDialog = new Dialog(this) {
					public boolean onSearchRequested() {
						String nameTemp = "" + Preferences.getName(getApplicationContext());
						addNewScore(currentDiff, nameTemp, pos, score);
						return true;
					}
					public void onBackPressed() {
						String nameTemp = "" + Preferences.getName(getApplicationContext());
						addNewScore(currentDiff, nameTemp, pos, score);
					}
				};
				scoreDialog.setContentView(R.layout.score_dialog);
				TextView yourScoreTV = (TextView) scoreDialog.findViewById(R.id.scoredialogtv);
				TextView yourNameTV = (TextView) scoreDialog.findViewById(R.id.score_nametv);
				yourNameET = (EditText) scoreDialog.findViewById(R.id.scoredialoget);
				scoreBT = (Button)scoreDialog.findViewById(R.id.scoredialogbutton);
				scoreBT.setTypeface(type);
				scoreBT.setText("OK");
				if (isHighScore == 1) {
					yourScoreTV.setText("Your Score: " + score);
					scoreDialog.setTitle("#" + (pos + 1) + " High Score!");
				}
				else {
					yourScoreTV.setText("Your score: " + score);
					yourNameET.setVisibility(View.INVISIBLE);
					yourNameTV.setVisibility(View.INVISIBLE);
					scoreBT.setText("CANCEL");
				}
				yourNameET.setTypeface(type);
				yourNameET.setText(name);
				Button onlineBT = (Button)scoreDialog.findViewById(R.id.scoredialogbuttononline);
				onlineBT.setTypeface(type);
				yourNameET.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						yourNameET.selectAll();
					}
				});
				scoreBT.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if (isHighScore == 1) {
							String nameTemp = "" + yourNameET.getText();
							addNewScore(currentDiff, nameTemp, pos, score);
						}
						else {scoreDialog.dismiss();}
					}
				});
				onlineBT.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						scoreBT.performClick();
						scoreDialog.dismiss();
						if (currentDiff == PopView.INSANE) {snjInsane.show(score, "" + yourNameET.getText(), "");}
						else {snjOrig.show(score, "" + yourNameET.getText(), "");}
					}
				});
				scoreDialog.show();
				dialogOpen = false;
			}
		}
		PopView.updatingScore = false;
		showScores(currentDiff);
	}
	
	public void showScores(byte diff) {
		Settings.load(diff);
		String highScoresTemp[] = Settings.getScores(getApplicationContext());
		for (int i = 0; i < highScores.length; i++) {
			String s = highScoresTemp[i];
			int fuck = Integer.parseInt(s.substring(s.indexOf("_") + 1)); 
			if (fuck != 0) {
				if (fuck == 1) highScores[i] = "-_-";
				else highScores[i] = s;
			}
			else {highScores[i] = "-_-";}
		}
		lv.setAdapter(new CustomAdapter(this, highScores, lv));
		lv.invalidate();
	}
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.clearhs_menu_item) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		builder.setMessage("Are you sure you want to clear these high scores?")
    		       .setCancelable(false)
    		       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    		           public void onClick(DialogInterface dialog, int id) {
    		        	   String lala[] = new String[10];
    		        	   Arrays.fill(lala, "-_0");
    		        	   Settings.load((byte)spinner.getSelectedItemPosition());
    		        	   Settings.setScores(getApplicationContext(), lala);
    		        	   showScores((byte)spinner.getSelectedItemPosition());
    		        	   dialog.dismiss();
    		           }
    		       })
    		       .setNegativeButton("No", new DialogInterface.OnClickListener() {
    		           public void onClick(DialogInterface dialog, int id) {
    		        	   	dialog.dismiss();
    		           }
    		       });
    		AlertDialog alert = builder.create();
    		alert.show();
		}
		return true;
	}
	
	public void addNewScore(byte difficulty, String name, int pos, int s) {
		spinner.setSelection(difficulty);
		Preferences.setName(getApplicationContext(), name);
		String ts[] = Settings.getScores(getApplicationContext());
		ts[pos] = name + "_" + s;
		Settings.setScores(getApplicationContext(), ts);
		scoreDialog.dismiss();
		PopView.updatingScore = false;
		showScores(difficulty);
	}
	
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      if (currentDiff == PopView.INSANE) {snjInsane.onActivityResult(requestCode, resultCode, data);}
      else {snjOrig.onActivityResult(requestCode, resultCode, data);}
    }
}