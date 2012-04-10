/****************************************************
 * a Sprite class to represent all objects on the screen 
 * 
 */

package com.schen.pop;

public class Sprite {
	//which picture should be drawn
	public byte drawableID;
	//x and y coordinates (y should be more exact)
	public int x;
	public float y;
	public float dy;
	public int speed;
	public boolean touched = false;
	//is it under the screen?
	public boolean under = false;
	//this should be changed to be relative to the screen size.. I can't believe i didn't catch this.. fuck
	public int width = 54;
	public byte instance = 0;
	
	//need empty constructor to allocate all memory at the beginning
	public Sprite() {}
	
	public Sprite(byte dID, int x, float y, int speed, int w) {
		this.drawableID = dID;
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.width = w;
	} 
}