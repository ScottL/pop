package com.schen.pop;

public class Sprite {
	public byte drawableID;
	public int x;
	public float y;
	public float dy;
	public int speed;
	public boolean touched = false;
	public boolean under = false;
	public final int WIDTH = 54;
	public byte instance = 0;
	public Sprite() {}
	
	public Sprite(byte dID, int x, float y, int speed) {
		this.drawableID = dID;
		this.x = x;
		this.y = y;
		this.speed = speed;
	} 
}