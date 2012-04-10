/*************************************************************
 * This class takes care of scoring and high scores, its name
 * should be changed....
 * 
 * The code for this class is absolutely shitty.  It has multiple
 * bugs that I actually know about but was too lazy to fix.
 * 
 * *****we should rewrite this immediately*****
 * 
 * also this is a good class for you to write cause it doesn't
 * have much to do with Android itself except for the files
 * but that is obvious stuff
 * 
 */

package com.schen.pop;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import android.content.Context;
import android.os.Environment;

public class Settings {
	private static boolean hasES = false;
	static String scores[] = new String[10];
	static String score = "";
	static File file;
	static byte diff = 0;
	
	public static void load(byte d) {
		diff = d;
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			File externalDir = Environment.getExternalStorageDirectory();
			String fName = "hs" + diff + ".txt";
			file = new File(externalDir.getAbsolutePath() + File.separator + fName);
			hasES = true;
			if (file.exists() == false) {
				try {
					file.createNewFile();
					hasES = true;
				} catch (IOException e) {hasES = false;}
			}
		}
		else {hasES = false;}
	}

	public static String[] getScores(Context context) {
		Arrays.fill(scores, "-_0");
		int ctr = 0;
		if (hasES) {
			Scanner scan;
			try {
				scan = new Scanner(file);
				while (scan.hasNextLine()) {
					if (ctr >= scores.length) break;	//just added
					scores[ctr] = scan.nextLine();
					ctr++;
				}
			} catch (FileNotFoundException e) {hasES = false;}
		}
		if (hasES == false) {
			try {	//just added
				for (int i = 0; i < 10; i++) {scores[i] = Preferences.getScore(context, i, diff);}
			}
			catch (Exception e) {e.printStackTrace();}	//just added try catch
		}
		return scores;
	}
	
	public static String getScore(Context context, int n) {
		if (hasES) {
			Scanner scan;
			try {
				scan = new Scanner(file);
				for (int i = 0; i < n - 1; i++) {scan.nextLine();}
				score = scan.nextLine();
				scan.close();
			} catch (FileNotFoundException e) {hasES = false;}
		}
		if (hasES == false) {score = Preferences.getScore(context, n, diff);}
		return score;
	}
	
	public static void setScores(Context context, String[] s) {
		if (hasES) {
			BufferedWriter writer;
			try {
				writer = new BufferedWriter(new FileWriter(file, false));
				for (int i = 0; i < 10; i++) {writer.write(s[i] + "\n");}
				writer.close();
			} catch (IOException e) {hasES = false;}
		}
		for (int i = 0; i < 10; i++) {Preferences.setScore(context, i, s[i], diff);}
	}
}
