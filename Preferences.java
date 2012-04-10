/*******************************************************
 * This class is a remade Preferences class.
 * It provides static methods to access preferences so the
 * other classes don't have to mess around with retarded preferences
 * code.
 * 
 * its pretty self-explanatory..
 */

package com.schen.pop;

import android.content.Context;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

public class Preferences extends PreferenceActivity {
	
	CheckBoxPreference instCB, soundCB;
	
	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        addPreferencesFromResource(R.xml.preferences);
	        PreferenceManager pm = this.getPreferenceManager();
	        Log.d("greg", "greg: " + Preferences.getInstructions(getApplicationContext()));
	        Log.d("greg", "greg: " + Preferences.getInstructions(getBaseContext()));
	        instCB = (CheckBoxPreference)pm.findPreference("instructions");
	        soundCB = (CheckBoxPreference)pm.findPreference("sound");
	        if (Preferences.getSound(getApplicationContext())) {soundCB.setChecked(true);}
	        else {soundCB.setChecked(false);}
	        if (Preferences.getInstructions(getApplicationContext())) {instCB.setChecked(true);}
	        else {instCB.setChecked(false);}
	 }
	 
	 public static boolean getSound(Context context) {return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("sound", true);}
	 
	 public static void setSound(Context context, boolean is) {PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("sound", is).commit();}
	 
	 public static boolean getInstructions(Context context) {return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("instructions", true);}
	 
	 public static void setInstructions(Context context, boolean inst) {PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("instructions", inst).commit();}
	 
	 public static String getScore(Context context, int pos, byte diff) {return PreferenceManager.getDefaultSharedPreferences(context).getString("" + pos + diff, " _0");}

	 public static void setScore(Context context, int pos, String name, int score, byte diff) {PreferenceManager.getDefaultSharedPreferences(context).edit().putString("" + pos + diff, name + "_" + score).commit();}
	 
	 public static void setScore(Context context, int pos, String score, byte diff) {PreferenceManager.getDefaultSharedPreferences(context).edit().putString("" + pos + diff, score).commit();}
	 
	 public static void setHS(Context context, int is) {PreferenceManager.getDefaultSharedPreferences(context).edit().putInt("hs?", is).commit();}
	 
	 public static int getHS(Context context) {return PreferenceManager.getDefaultSharedPreferences(context).getInt("hs?", 0);}
	 
	 public static void setPos(Context context, int pos) {PreferenceManager.getDefaultSharedPreferences(context).edit().putInt("pos", pos).commit();}
	 
	 public static int getPos(Context context) {return PreferenceManager.getDefaultSharedPreferences(context).getInt("pos", 9);}
	 
	 public static void setFromGame(Context context, int from_game) {PreferenceManager.getDefaultSharedPreferences(context).edit().putInt("from_game", from_game).commit();}
	 
	 public static int getFromGame(Context context) {return PreferenceManager.getDefaultSharedPreferences(context).getInt("from_game", 0);}
	 
	 public static void setCurrentScore(Context context, int score) {PreferenceManager.getDefaultSharedPreferences(context).edit().putInt("score", score).commit();}
	 
	 public static int getCurrentScore(Context context) {return PreferenceManager.getDefaultSharedPreferences(context).getInt("score", 9);}
	 
	 public static String getName(Context context) {return PreferenceManager.getDefaultSharedPreferences(context).getString("name", "...");}
	 
	 public static void setName(Context context, String name) {PreferenceManager.getDefaultSharedPreferences(context).edit().putString("name", name).commit();}
	 
	 public static void resetHighScores(Context context, byte diff) {
		 for (int i = 0; i < 10; i++) {PreferenceManager.getDefaultSharedPreferences(context).edit().putString("" + i + diff, " _0").commit();}
	 }
	 
	 public static void printScores(Context context) {
		 for (int i = 0; i < 10; i++) {Log.d("HELLO", "SCORES: " + PreferenceManager.getDefaultSharedPreferences(context).getString("" + i, "NOT THERE"));}
	 }
}