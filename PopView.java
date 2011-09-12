package com.schen.pop;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
public class PopView extends SurfaceView implements SurfaceHolder.Callback {
	public TextView scoreView, livesView;
	static Bundle b2, bShit;
	static boolean updatingScore = false;
	static ImageView imageViews[] = new ImageView[3];
	Activity activity;
	Context contextForView;
	static final boolean FALSE = false;
	static final boolean TRUE = true;
	static byte diff;
	static final byte ORIG = 0;
	static final byte INSANE = 1;
	static final byte KIDS = 2;
	boolean playSound = false;
	
	class PopThread extends Thread {
		
		Bitmap sky;
		Bitmap earth;
		int skyWidth, skyHeight, earthWidth, earthHeight;
		float prop;
		int drawSkyEnd;
		int drawEarthPos;
		boolean gone = false;
		Message msg; Bundle b;
		Message msgShit; Bundle bShit;
		int sendS, sendL;
		int sendShit = 0;
		String sss;	//debugging
		boolean canBack = true;
		Sprite[] sprites = new Sprite[99];
		double[] random = new double[9999];	//1000 pre-calculated random variables
		double[] random2 = new double[100];
		double randomS = 0;
		int random2Ctr = 0;
		static final double SLOW_CHANCE = 0.42;
		static final double SHRINK_CHANCE = 0.75;
		static final int random2Length = 100;
		long lastTime2, now2;
		double elapsed2;
		long lastTime, now;
		long lastTimeG, nowG;
		double elapsedG;
		double elapsed;
		int spriteListStart = 0, spriteListEnd = 0;
		Bitmap background, btt;
		int screenHeight, screenWidth, randomWidth;
		int halfScreenHeight, halfScreenWidth;
		float spriteWidth;
		SurfaceHolder surfaceHolder;
		boolean run = false;
		Bitmap bitmaps[] = new Bitmap[10];
		Sprite tempSprite;
		Sprite tempSpritePh;
		Sprite tempSpriteG;
		Sprite tempSpriteIn;
		double gravity = 1;
		static final int itemLength = 800;
		static final int loseLifeVibrate = 300;
		static final int gameOverVibrate = 706;
		static final int SLOW_END = 200;
		static final int SMALL_START = 410;
		static final int SMALL_STARTx = 90;
		static final int SMALL_END = 45;
		static final int X500 = 500;
		static final int X1000 = 1000;
		static final int X1500 = 1500;
		static final int X10K = 10000;
		final int bitmapsLength = bitmaps.length;
		final int spritesLength = sprites.length;
		static final byte Y_START = -20;
		static final byte STATE_LOSE = 1, STATE_PAUSED = 2, STATE_RUNNING = 4;
		static final byte heightRatio = 6;	//bigger = smaller sprites; smaller = bigger sprites
		static final byte HUNDRED = 100;
		static final byte TEN = 10;
		static final byte NINE = 9;
		byte SPAWN_RATE_MAX = 11;
		static final byte FIVE = 5;
		static final byte FOUR = 4;
		static final byte THREE = 3;
		static final byte TWO = 2;
		static final byte ONE = 1;
		static final byte ZERO = 0;
		static final byte TEXT_STOP = Byte.MAX_VALUE;
		static final byte INSTANCE_BUBBLE = ZERO;
		static final byte INSTANCE_ROCK = TWO;
		static final byte INSTANCE_BOMB = 4;
		static final byte INSTANCE_EXTRA_ITEM = 6;
		static final byte INSTANCE_EXTRA_LIFE = 8;
		static final byte SMALL_CTR_OFFSET = 80;
		static final float volume = 0.4f;
		byte SPEED_SLOW = 3;
		byte SPEED_MED = 4;
		byte SPEED_FAST = 5;
		float MAX_GAME_SPEED = 1.3f;
		static final float X02 = 0.02f;
		static final float sizeRatio = 1.5f;
		static final float xBUBBLE_MIN = 0.72f;
		static final float xBUBBLE_MAX = 0.72f;
		float xBUBBLE = 0.58f;
		float xROCK = 	0.92f;
		float xBOMB =		0.93f;
		float xITEM = 0.985f;
		static final float DELTA_GAME_SPEED = 0.026f;
		static final float xSPEED_SLOW = 0.5f;
		static final float xSPEED_MED = 0.751f;
		static final float SHRINK_RATIO = 1.2f;
		static final String BK_SCORE = "score";
		static final String BK_LIVES = "lives";
		static final String HF = "hf";
		static final String TST = "tst";
		static final String TSTSTRING = "tstString";
		static final String VL = "vl";
		static final String YESLIVES = "yeslives";
		static final String YESSCORE = "yesscore";
		static final String NOT = "";
		final CharSequence sSHRINK = "shrink!";
		final CharSequence sSLOW = "slow down!";
		final CharSequence sPOINTS = "100 points!";
		final CharSequence sLIFE = "extra life!";
		final CharSequence sLOSELIFE = "-1 life!";
		final CharSequence sGAMEOVER = "GAME OVER!";
		static final boolean FALSE = false;
		static final boolean TRUE = true;
		final Paint paint = new Paint();
		byte state = STATE_LOSE;
		public int score = ZERO, lastScore = ZERO;
		byte lives = THREE, lastLives = THREE;
		public float gameSpeed = 0.2f;	//speed that is factored into each sprites dy value
		float holdGameSpeed = gameSpeed;
		float holdSpriteWidth;
		int spawnRate = 15;	//how often new sprites are spawned (millis)
		int holdSpawnRate = ZERO;
		long iter = ZERO;		//iteration number; reset at 100k
		int slowCtr = ZERO;
		int smallCtr = ZERO;
		byte STATUS_SLOW = ZERO;
		byte STATUS_SMALL = ZERO;
		byte lastSprite = ZERO;
		float x;
		float y;
		int sIdBubble = 0; int sIdRock = 0; int sIdBomb = 0; int sIdSmall = 0; int sIdSlow = 0; int sIdPoints = 0; int sIdLife = 0;
		int idAlien = 0, idLoseLife = 0, idLife = 0, idItem = 0;
		byte playAlien;
		byte playLoseLife;
		byte playLife;
		byte playItem;
		static final String PAUSED = "PAUSED!";
		static final String TSTP = "TAP SCREEN TO PLAY";
		static final String PBTE = "PRESS BACK TO EXIT";
		SoundPool soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
		PopView pv;
		int startY;
		CharSequence toastTxt = "";
		byte textStopCtr = Byte.MAX_VALUE;
		float SHRINK_WIDTH;
		Context context;
		Resources res;
		Handler handler;
		Handler handlerShit;
		int foo, ctr, lawl, i1, i2, i3, i4, a, xy, g = ZERO, mn, randCtr, randLength;
		double ab;
		byte FPS = 17;	//58 fps
		Paint fontPaint, fontPaint2, pausePaint, backPaint;
		Canvas c;
		int blue = Color.rgb(30, 150, 255);
		public PopThread(Context context, SurfaceHolder surface_holder, int theme, Handler handler, Handler h2, PopView pop) {
			this.surfaceHolder = surface_holder;
			this.context = context;
			this.handler = handler;
			this.handlerShit = h2;
			this.pv = pop;
			res = context.getResources();
			Display display = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
			for (xy = ZERO; xy < sprites.length; xy++) sprites[xy] = new Sprite();
			for (xy = ZERO; xy < random.length; xy++) random[xy] = Math.random();
			tempSprite = new Sprite();
			screenHeight = display.getHeight();
			screenWidth = display.getWidth();
			setDifficulty(PopView.diff);
			halfScreenHeight = screenHeight / TWO;
			halfScreenWidth = screenWidth / TWO;
			spriteWidth = (screenHeight / heightRatio);
			startY = (int) -(1.1 * spriteWidth);
			SHRINK_WIDTH = spriteWidth / 400;
			AssetManager am = res.getAssets();
			AssetFileDescriptor afd = null;
			try {
				afd = am.openFd("sounds/alien.ogg");
				idAlien = soundPool.load(afd, 1);
				afd = am.openFd("sounds/lose_life.ogg");
				idLoseLife = soundPool.load(afd, 1);
				afd = am.openFd("sounds/life.ogg");
				idLife = soundPool.load(afd, 1);
				afd = am.openFd("sounds/item.ogg");
				idItem = soundPool.load(afd, 1);
			} catch (IOException e) {
			}
			randomWidth = (int)(screenWidth - spriteWidth);
			spriteListStart = ZERO;
			spriteListEnd = ZERO;
			randLength = random.length;
			loadTheme(ZERO);
			b = new Bundle();
			bShit = new Bundle();
			msg = handler.obtainMessage();
			msgShit = handlerShit.obtainMessage();
			Typeface lala = Typeface.createFromAsset(getContext().getAssets(), "fonts/font.ttf");
			paint.setTypeface(lala);
			paint.setTextAlign(Paint.Align.CENTER);
			paint.setTextSize(34);
			paint.setColor(Color.WHITE);
			fontPaint = new Paint();
			fontPaint.setTypeface(lala);
			fontPaint.setColor(Color.WHITE);
			fontPaint.setTextSize(38);
			fontPaint.setTextAlign(Paint.Align.CENTER);
			fontPaint2 = new Paint();
			fontPaint2.setTypeface(lala);
			fontPaint2.setColor(Color.WHITE);
			fontPaint2.setTextSize(22);
			fontPaint2.setTextAlign(Paint.Align.CENTER);
			pausePaint = new Paint();
			pausePaint.setColor(Color.BLACK);
			backPaint = new Paint();
			setup();
			sky = BitmapFactory.decodeResource(res, R.drawable.sky);
			earth = BitmapFactory.decodeResource(res, R.drawable.earth);
			skyWidth = sky.getWidth(); skyHeight = sky.getHeight();
			earthWidth = earth.getWidth(); earthHeight = earth.getHeight();
			prop = skyWidth / screenWidth;
			int correctY = (int)(skyHeight / prop);
			drawSkyEnd = correctY;
			backPaint.setShader(new LinearGradient((screenWidth / 2), drawSkyEnd, (screenWidth / 2), (screenHeight), new int[] {Color.BLACK, blue}, new float[] {0, 1f}, Shader.TileMode.MIRROR));
			sky = Bitmap.createScaledBitmap(sky, screenWidth, correctY, false);
			prop = earthWidth / screenWidth;
			correctY = (int)(earthHeight / prop);
			drawEarthPos = screenHeight - correctY;
			earth = Bitmap.createScaledBitmap(earth, screenWidth, correctY, false);
		}
		public void setup() {
			if (screenHeight <= 480) {
				SPEED_SLOW = THREE;
				SPEED_MED = FOUR;
				SPEED_FAST = 5;
				gravity = 0.6;
				fontPaint.setTextSize(26);
				fontPaint2.setTextSize(14);
				paint.setTextSize(24);
			}
			else if (screenHeight <= 640) {
				SPEED_SLOW = FOUR;
				SPEED_MED = 5;
				SPEED_FAST = 6;
				gravity = 0.8;
				fontPaint.setTextSize(30);
				fontPaint2.setTextSize(20);
				paint.setTextSize(28);
			}
			else {
				SPEED_SLOW = 5;
				SPEED_MED = 6;
				SPEED_FAST = 7;
				gravity = ONE;
				fontPaint.setTextSize(38);
				fontPaint2.setTextSize(22);
				paint.setTextSize(34);
			}
		}
		
		public void setDifficulty(byte n) {
			if (n == PopView.ORIG) {
				this.xBUBBLE = 0.72f;
				this.xROCK = 0.95f;
				this.xBOMB = 0.958f;
				this.xITEM = 0.985f;
				this.MAX_GAME_SPEED = 0.42f;
				this.SPAWN_RATE_MAX = 8;
				this.spawnRate = 16;
				this.spawnRate = 17;
			//	this.gameSpeed = 0.06f;
				this.gameSpeed = 0.05f;
			}
			else if (n == PopView.INSANE) {
				this.xBUBBLE = 0.82f;
				this.xROCK = 0.95f;
				this.xBOMB = 0.985f;
				this.xITEM = ZERO;
				this.MAX_GAME_SPEED = 0.44f;
//				this.spawnRate = 11;
				this.SPAWN_RATE_MAX = 6;
//				this.gameSpeed = 0.10f;	//0.25
				this.gameSpeed = 0.085f;
				this.spawnRate = 13;
			}
		}
		
		public void updateSound() {
			if (playAlien == ONE) {
				pv.post(new Runnable() {
					public void run() {
						soundPool.play(idAlien, volume, volume, ZERO, ZERO, ONE);
						playAlien = ZERO;
					}
				});
			}
			if (playLoseLife == ONE) {
				pv.post(new Runnable() {public void run() {soundPool.play(idLoseLife, volume, volume, ZERO, ZERO, ONE);playLoseLife = ZERO;}});
			}
			if (playLife == ONE) {
				pv.post(new Runnable() {public void run() {soundPool.play(idLife, volume, volume, ZERO, ZERO, ONE);playLife = ZERO;}});
			}
			if (playItem == ONE) {
				pv.post(new Runnable() {public void run() {soundPool.play(idItem, volume, volume, ZERO, ZERO, ONE);playItem = ZERO;}});
			}
		}
		
		public void run() {
			System.gc();
			while (run) {
				c = null;
				try {
					c = surfaceHolder.lockCanvas(null);
					synchronized (surfaceHolder) {
						if (state == STATE_RUNNING) {
							if (textStopCtr < TEXT_STOP) textStopCtr++;
							if (STATUS_SLOW == ONE) {
								if (--slowCtr < SLOW_END) {
									gameSpeed += (holdGameSpeed / SLOW_END);
									if (slowCtr <= ZERO) {
										gameSpeed = holdGameSpeed;
										spawnRate = holdSpawnRate;
										STATUS_SLOW = ZERO;
									}
								}
							}
							if (STATUS_SMALL == ONE) {
								smallCtr--;
								if (smallCtr > itemLength - SMALL_CTR_OFFSET) {
									spriteWidth -= SHRINK_WIDTH;
									for (i1 = ZERO; i1 < bitmapsLength; i1 += TWO) {
										bitmaps[i1].recycle();
										bitmaps[i1] = Bitmap.createScaledBitmap(bitmaps[i1 + ONE], (int)(spriteWidth), (int)(spriteWidth), FALSE);
									}
								}
								else if (smallCtr < SMALL_CTR_OFFSET && smallCtr > ZERO) {
									spriteWidth += SHRINK_WIDTH;
									for (i1 = ZERO; i1 < bitmapsLength; i1 += TWO) {
										bitmaps[i1].recycle();
										bitmaps[i1] = Bitmap.createScaledBitmap(bitmaps[i1 + ONE], (int)(spriteWidth), (int)(spriteWidth), FALSE);
									}
								}
								if (smallCtr < ZERO) {
									spriteWidth = holdSpriteWidth;
									for (i1 = ZERO; i1 < bitmapsLength; i1 += TWO) {
										bitmaps[i1].recycle();
										bitmaps[i1] = Bitmap.createScaledBitmap(bitmaps[i1 + ONE], (int)spriteWidth, (int)spriteWidth, FALSE);
									}
									STATUS_SMALL = ZERO;
								}
							}
							iter++;
							if (iter > X10K) {iter = ZERO;}
							if (iter % spawnRate == ZERO) {
								ab = random[randCtr++];
								tempSprite = sprites[spriteListEnd];
								if (ab < xBUBBLE) {
									tempSprite.drawableID = INSTANCE_BUBBLE;
								}
								else if (ab < xROCK) {
									tempSprite.drawableID = INSTANCE_ROCK;
								}
								else if (ab < xBOMB) {tempSprite.drawableID = INSTANCE_BOMB;}
								else if (ab < xITEM) {tempSprite.drawableID = INSTANCE_EXTRA_ITEM;}
								else {tempSprite.drawableID = INSTANCE_EXTRA_LIFE;}
								tempSprite.y = startY; ab = random[randCtr++];
								tempSprite.x = (int)(ab * randomWidth); ab = random[randCtr++]; if (randCtr >= randLength) randCtr = ZERO;
								if (ab < xSPEED_SLOW) tempSprite.speed = SPEED_SLOW;
								else if (ab < xSPEED_MED) tempSprite.speed = SPEED_MED;
								else tempSprite.speed = SPEED_FAST;
								tempSprite.touched = FALSE; tempSprite.under = FALSE; tempSprite.dy = ZERO;
								sprites[spriteListEnd] = tempSprite; spriteListEnd++;
								if (spriteListEnd >= sprites.length) spriteListEnd = ZERO;	//TODO: how to wrap when iterating in update methods?!?!?!?!
								if (spriteListEnd == spriteListStart) {spriteListStart++;}	//WHY THE FUCK DOESNT THIS SHIT WORK:?????
							}
							updateState();
							updatePhysics();
							now2 = System.currentTimeMillis();
							elapsed2 = (now2 - lastTime2);
							while (elapsed2<FPS) {
								now2 = System.currentTimeMillis();
								elapsed2 = now2 - lastTime2;
							}
							lastTime2 = now2;
						}
						if (!gone) updateGraphics2(c);
					}
				} finally {if (c != null) surfaceHolder.unlockCanvasAndPost(c);}
			}
		}
		
		public void loadTheme(int theme) {
			if (theme == ZERO) {
				bitmaps[ZERO] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.bubble), (int)spriteWidth, (int)spriteWidth, FALSE);
				bitmaps[ONE] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.bubble), (int)spriteWidth, (int)spriteWidth, FALSE);
				bitmaps[TWO] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.rock), (int)spriteWidth, (int)spriteWidth, FALSE);
				bitmaps[THREE] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.rock), (int)spriteWidth, (int)spriteWidth, FALSE);
				bitmaps[4] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.bomb), (int)spriteWidth, (int)spriteWidth, FALSE);
				bitmaps[5] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.bomb), (int)spriteWidth, (int)spriteWidth, FALSE);
				bitmaps[6] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.item), (int)spriteWidth, (int)spriteWidth, FALSE);
				bitmaps[7] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.item), (int)spriteWidth, (int)spriteWidth, FALSE);
				bitmaps[8] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.extra), (int)spriteWidth, (int)spriteWidth, FALSE);
				bitmaps[NINE] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.extra), (int)spriteWidth, (int)spriteWidth, FALSE);
				btt = Bitmap.createScaledBitmap(bitmaps[INSTANCE_EXTRA_LIFE], screenHeight / 20, screenHeight / 20, FALSE);
			}
		}
		public void setState(byte state) {this.state = state;}
		public void updateState() {
				sendL = ZERO;
				sendS = ZERO;
				if (score != lastScore) {
					if (score % 420 == ZERO) {
						if (score % 840 == ZERO && spawnRate > SPAWN_RATE_MAX) spawnRate--;
						if (gameSpeed < MAX_GAME_SPEED) {gameSpeed += DELTA_GAME_SPEED;}
					}
					lastScore = score;
					sendS = ONE;
				}
				if (lives < lastLives) {
					toastTxt = sLOSELIFE;
					textStopCtr = ZERO;
					msgShit = handlerShit.obtainMessage();
					bShit.putBoolean(HF, TRUE);
					bShit.putBoolean(TST, TRUE);
					if (lives >= ONE) {
						bShit.putInt(VL, loseLifeVibrate);
						bShit.putCharSequence(TSTSTRING, sLOSELIFE);
					}
					else {
						bShit.putInt(VL, gameOverVibrate);
						bShit.putCharSequence(TSTSTRING, sGAMEOVER);
					}
					msgShit.setData(bShit);
					handlerShit.sendMessage(msgShit);
					lastLives = lives;
					sendL = lives;
					if (lives < ONE) {
						setState(STATE_LOSE);
						toastTxt = sGAMEOVER;
						textStopCtr = ZERO;
						updatingScore = TRUE;
						msg = handler.obtainMessage();
						b.putCharSequence(BK_SCORE, NOT + score);
						b.putInt(BK_LIVES, lives);
						msg.setData(b);
						handler.sendMessage(msg);
						canBack = FALSE;
						
						Settings.load(PopView.diff);
						String scores[] = new String[TEN];
						scores = Settings.getScores(contextForView);
						sss = scores[NINE];
						foo = Integer.parseInt(sss.substring(sss.indexOf('_') + ONE));
						ctr = NINE;
						if (score >= foo && score > ZERO) {
							while (score >= foo && ctr >= ZERO) {
								ctr--;
								String blah = "";
								if (ctr >= ZERO) blah = scores[ctr];
								else blah = "-_0";
								blah = blah.substring(blah.indexOf('_') + ONE);
								foo = Integer.parseInt(blah.trim());
							}//ctr is now place in front of correct pos
							lawl = ZERO;
							if (ctr <= NINE) {
								for (lawl = NINE; lawl > (ctr); lawl--) {
									scores[lawl] = scores[lawl - ONE];
									if (lawl == ONE) break;
								}
								scores[ctr + ONE] = "-_1";
								Preferences.setHS(contextForView, ONE);
							}
							Settings.setScores(contextForView, scores);
							Preferences.setPos(contextForView, ctr + ONE);
							Preferences.setFromGame(contextForView, ONE);
							Preferences.setCurrentScore(contextForView, score);
						}
						Preferences.setCurrentScore(contextForView, score);
						Preferences.setFromGame(contextForView, ONE);
						Timer t = new Timer();
						t.schedule(new TimerTask() {
							@Override
							public void run() {
								run = false;
								Intent in = new Intent(activity, HighScoreView.class);
								endGame();
								activity.startActivity(in);
							}
						}, 2000);
					} else {
						msg = handler.obtainMessage();
						if (sendS == ONE) {
							b.putCharSequence(BK_SCORE, NOT + score);
							b.putBoolean(YESSCORE, TRUE);
						} else {b.putBoolean(YESSCORE, FALSE);}
						if (sendL != ZERO) {
							b.putInt(BK_LIVES, lives);
							b.putBoolean(YESLIVES, TRUE);
						} else {b.putBoolean(YESLIVES, FALSE);}
						msg.setData(b);
						handler.sendMessage(msg);
					}
				}
				else if (lives > lastLives) {
					lastLives = lives;
					sendL = -ONE * lives;
					msg = handler.obtainMessage();
					if (sendS == ONE) {
						b.putCharSequence(BK_SCORE, NOT + score);
						b.putBoolean(YESSCORE, TRUE);
					} else {b.putBoolean(YESSCORE, FALSE);}
					if (sendL != ZERO) {
						b.putInt(BK_LIVES, sendL);
						b.putBoolean(YESLIVES, TRUE);
					} else {b.putBoolean(YESLIVES, FALSE);}
					msg.setData(b);
					handler.sendMessage(msg);
				}
				else {
					if (sendS != ZERO) {
						msg = handler.obtainMessage();
						if (sendS == ONE) {
							b.putCharSequence(BK_SCORE, NOT + score);
							b.putBoolean(YESSCORE, TRUE);
						} else {b.putBoolean(YESSCORE, FALSE);}
						if (sendL != ZERO) {
							b.putInt(BK_LIVES, lives);
							b.putBoolean(YESLIVES, TRUE);
						} else {b.putBoolean(YESLIVES, FALSE);}
						msg.setData(b);
						handler.sendMessage(msg);
					}
				}
		}
		
		public void updatePhysics() {
			if (spriteListStart <= spriteListEnd) {
				for (a = spriteListStart; a < spriteListEnd; a++) {	//0
					tempSpritePh = sprites[a];
					if (!tempSpritePh.under) {
						if (!tempSpritePh.touched){ 
							sprites[a].y += (tempSpritePh.dy + tempSpritePh.speed) * gameSpeed;
							sprites[a].dy += gravity;
						}
						if (tempSpritePh.y > screenHeight) {
							if (tempSpritePh.drawableID == INSTANCE_BUBBLE) {if (lives != ZERO) lives--;}
							else if (tempSpritePh.drawableID == INSTANCE_ROCK) score += FIVE;
							sprites[a].under = TRUE;
							if (tempSpritePh.speed == SPEED_SLOW) spriteListStart = a;
						}
					}
				}
			}
			else {
				for (a = spriteListStart; a < sprites.length; a++) {	//0
					tempSpritePh = sprites[a];
					if (!tempSpritePh.under) {
						if (!tempSpritePh.touched){ 
							sprites[a].y += (tempSpritePh.dy + tempSpritePh.speed) * gameSpeed;
							sprites[a].dy += gravity;
						}
						if (tempSpritePh.y > screenHeight) {
							if (tempSpritePh.drawableID == INSTANCE_BUBBLE) {if (lives != ZERO) lives--;}
							else if (tempSpritePh.drawableID == INSTANCE_ROCK) score += FIVE;
							sprites[a].under = TRUE;
							if (tempSpritePh.speed == SPEED_SLOW) spriteListStart = a;
						}
					}
				}
				for (a = ZERO; a < spriteListEnd; a++) {
					tempSpritePh = sprites[a];
					if (!tempSpritePh.under) {
						if (!tempSpritePh.touched){ 
							sprites[a].y += (tempSpritePh.dy + tempSpritePh.speed) * gameSpeed;
							sprites[a].dy += gravity;
						}
						if (tempSpritePh.y > screenHeight) {
							if (tempSpritePh.drawableID == INSTANCE_BUBBLE) {if (lives != ZERO) lives--;}
							else if (tempSpritePh.drawableID == INSTANCE_ROCK) score += FIVE;
							sprites[a].under = TRUE;
							if (tempSpritePh.speed == SPEED_SLOW) spriteListStart = a;
						}
					}
				}
			}
		}
		
		public void drawPaused(Canvas canvas) {
			synchronized (surfaceHolder) {
				canvas.drawRect(new Rect(ZERO, ZERO, screenWidth, screenHeight), pausePaint);
				canvas.drawText(PAUSED, halfScreenWidth, screenHeight / FOUR, fontPaint);
				canvas.drawText(TSTP, halfScreenWidth, screenHeight / THREE, fontPaint2);
				canvas.drawText(PBTE, halfScreenWidth, screenHeight / TWO, fontPaint2);
			}
		}
		
		public void updateGraphics2(Canvas canvas) {
			synchronized (surfaceHolder) {
				canvas.drawRect(new Rect(ZERO, drawSkyEnd, screenWidth, screenHeight), backPaint);
				canvas.drawBitmap(sky, ZERO, ZERO, null);
				canvas.drawBitmap(earth, ZERO, drawEarthPos, null);
				if (spriteListStart <= spriteListEnd) {
					for (g = spriteListStart; g < spriteListEnd; g++) {
						tempSpriteG = sprites[g];
						if (!tempSpriteG.touched && !tempSpriteG.under) {canvas.drawBitmap(bitmaps[tempSpriteG.drawableID], tempSpriteG.x, tempSpriteG.y, null);}
					}
				}
				else {
					for (g = spriteListStart; g < sprites.length; g++) {
						tempSpriteG = sprites[g];
						if (!tempSpriteG.touched && !tempSpriteG.under) {canvas.drawBitmap(bitmaps[tempSpriteG.drawableID], tempSpriteG.x, tempSpriteG.y, null);}
					}
					for (g = ZERO; g < spriteListEnd; g++) {
						tempSpriteG = sprites[g];
						if (!tempSpriteG.touched && !tempSpriteG.under) {canvas.drawBitmap(bitmaps[tempSpriteG.drawableID], tempSpriteG.x, tempSpriteG.y, null);}
					}
				}
				if (textStopCtr != TEXT_STOP && state != STATE_PAUSED) canvas.drawText(toastTxt, 0, toastTxt.length(), halfScreenWidth, halfScreenHeight, paint);
				if (state == STATE_PAUSED) {drawPaused(canvas);}
			}
		}
		
		public void updateInput(int num, MotionEvent e) {
			if (state != STATE_RUNNING) return;
			if (spriteListStart <= spriteListEnd) {
				for (i2 = spriteListEnd - ONE; i2 >= spriteListStart; i2--) {
					tempSpriteIn = sprites[i2];
					for (int h = ZERO; h < num; h++) {
						x = e.getX(h);
						y = e.getY(h);
						if (!tempSpriteIn.under && !tempSpriteIn.touched) {
							if (((x-tempSpriteIn.x <= spriteWidth) && (x-tempSpriteIn.x >= ZERO)) 
								 && 
								 ((y-tempSpriteIn.y <= spriteWidth) && (y-tempSpriteIn.y >= ZERO))) {
								sprites[i2].touched = TRUE;
								if (tempSpriteIn.drawableID == INSTANCE_BUBBLE) {
									score += TEN;
									playAlien = ONE;
									return;
								}
								else if (tempSpriteIn.drawableID == INSTANCE_ROCK) {
									if (lives != ZERO) lives--;
									playLoseLife = ONE;
									toastTxt = this.sLOSELIFE; return;
								}
								else if (tempSpriteIn.drawableID == INSTANCE_EXTRA_ITEM) {
									playItem = ONE;
									double a = Math.random();
									if (a < SLOW_CHANCE) {
										if (STATUS_SLOW != ONE) {
											STATUS_SLOW = ONE; slowCtr = itemLength; holdSpawnRate = spawnRate; spawnRate += NINE; holdGameSpeed = gameSpeed; gameSpeed /= THREE;
											toastTxt = sSLOW; textStopCtr = ZERO; return;
										}
										else {
											if (STATUS_SMALL != ONE) {STATUS_SMALL = ONE; smallCtr = itemLength; holdSpriteWidth = spriteWidth; toastTxt = sSHRINK; textStopCtr = ZERO; return;}
											else { score += HUNDRED; toastTxt = sPOINTS; textStopCtr = ZERO; return;}
										}
									}
									else if (a < SHRINK_CHANCE) {
										if (STATUS_SMALL != ONE) {
											STATUS_SMALL = ONE; smallCtr = itemLength; holdSpriteWidth = spriteWidth; toastTxt = sSHRINK; textStopCtr = ZERO; return;
										}
										else {
											if (STATUS_SLOW != ONE) {
												STATUS_SLOW = ONE; slowCtr = itemLength; holdSpawnRate = spawnRate; spawnRate += NINE; holdGameSpeed = gameSpeed; gameSpeed /= THREE;
												toastTxt = sSLOW; textStopCtr = ZERO; return;
											}
											else {score += HUNDRED; toastTxt = sPOINTS; textStopCtr = ZERO; return;}
										}
									}
									else {score += HUNDRED; toastTxt = sPOINTS; textStopCtr = ZERO; return;}
								}
								else if (tempSpriteIn.drawableID == INSTANCE_BOMB) {lives = ZERO; playLoseLife = ONE; return;}
								else if (tempSpriteIn.drawableID == INSTANCE_EXTRA_LIFE && lives <= TWO) {
									lives++;
									toastTxt = sLIFE;
									textStopCtr = ZERO;
									playLife = ONE;
								}
								return;
							}
						}
					}
				}
			}
			else {
				for (i2 = spritesLength - ONE; i2 >= spriteListStart; i2--) {
					tempSpriteIn = sprites[i2];
					for (i3 = ZERO; i3 < num; i3++) {
						x = e.getX(i3);
						y = e.getY(i3);
						if (!tempSpriteIn.under && !tempSpriteIn.touched) {
							if (((x-tempSpriteIn.x <= spriteWidth) && (x-tempSpriteIn.x >= ZERO)) 
								 && 
								 ((y-tempSpriteIn.y <= spriteWidth) && (y-tempSpriteIn.y >= ZERO))) {
								sprites[i2].touched = TRUE;
								if (tempSpriteIn.drawableID == INSTANCE_BUBBLE) {
									score += TEN;
									playAlien = ONE;
									return;
								}
								else if (tempSpriteIn.drawableID == INSTANCE_ROCK) {
									if (lives != ZERO) lives--;
									playLoseLife = ONE;
									toastTxt = this.sLOSELIFE; return;
								}
								else if (tempSpriteIn.drawableID == INSTANCE_EXTRA_ITEM) {
									playItem = ONE;
									double a = Math.random();
									if (a < SLOW_CHANCE) {
										if (STATUS_SLOW != ONE) {
											STATUS_SLOW = ONE; slowCtr = itemLength; holdSpawnRate = spawnRate; spawnRate += NINE; holdGameSpeed = gameSpeed; gameSpeed /= THREE;
											toastTxt = sSLOW; textStopCtr = ZERO; return;
										}
										else {
											if (STATUS_SMALL != ONE) {STATUS_SMALL = ONE; smallCtr = itemLength; holdSpriteWidth = spriteWidth; toastTxt = sSHRINK; textStopCtr = ZERO; return;}
											else { score += HUNDRED; toastTxt = sPOINTS; textStopCtr = ZERO; return;}
										}
									}
									else if (a < SHRINK_CHANCE) {
										if (STATUS_SMALL != ONE) {
											STATUS_SMALL = ONE; smallCtr = itemLength; holdSpriteWidth = spriteWidth; toastTxt = sSHRINK; textStopCtr = ZERO; return;
										}
										else {
											if (STATUS_SLOW != ONE) {
												STATUS_SLOW = ONE; slowCtr = itemLength; holdSpawnRate = spawnRate; spawnRate += NINE; holdGameSpeed = gameSpeed; gameSpeed /= THREE;
												toastTxt = sSLOW; textStopCtr = ZERO; return;
											}
											else {score += HUNDRED; toastTxt = sPOINTS; textStopCtr = ZERO; return;}
										}
									}
									else {score += HUNDRED; toastTxt = sPOINTS; textStopCtr = ZERO; return;}
								}
								else if (tempSpriteIn.drawableID == INSTANCE_BOMB) {lives = ZERO; playLoseLife = ONE; return;}
								else if (tempSpriteIn.drawableID == INSTANCE_EXTRA_LIFE && lives <= TWO) {
									lives++;
									toastTxt = sLIFE;
									textStopCtr = ZERO;
									playLife = ONE;
								}
								return;
							}
						}
					}
				}
				for (i2 = spriteListEnd - ONE; i2 >= ZERO; i2--) {
					tempSpriteIn = sprites[i2];
					for (i3 = ZERO; i3 < num; i3++) {
						x = e.getX(i3);
						y = e.getY(i3);
						if (!tempSpriteIn.under && !tempSpriteIn.touched) {
							if (((x-tempSpriteIn.x <= spriteWidth) && (x-tempSpriteIn.x >= ZERO)) 
								 && 
								 ((y-tempSpriteIn.y <= spriteWidth) && (y-tempSpriteIn.y >= ZERO))) {
								sprites[i2].touched = TRUE;
								if (tempSpriteIn.drawableID == INSTANCE_BUBBLE) {
									score += TEN;
									playAlien = ONE;
									return;
								}
								else if (tempSpriteIn.drawableID == INSTANCE_ROCK) {
									if (lives != ZERO) lives--;
									playLoseLife = ONE;
									toastTxt = this.sLOSELIFE; return;
								}
								else if (tempSpriteIn.drawableID == INSTANCE_EXTRA_ITEM) {
									playItem = ONE;
									double a = Math.random();
									if (a < SLOW_CHANCE) {
										if (STATUS_SLOW != ONE) {
											STATUS_SLOW = ONE; slowCtr = itemLength; holdSpawnRate = spawnRate; spawnRate += NINE; holdGameSpeed = gameSpeed; gameSpeed /= THREE;
											toastTxt = sSLOW; textStopCtr = ZERO; return;
										}
										else {
											if (STATUS_SMALL != ONE) {STATUS_SMALL = ONE; smallCtr = itemLength; holdSpriteWidth = spriteWidth; toastTxt = sSHRINK; textStopCtr = ZERO; return;}
											else { score += HUNDRED; toastTxt = sPOINTS; textStopCtr = ZERO; return;}
										}
									}
									else if (a < SHRINK_CHANCE) {
										if (STATUS_SMALL != ONE) {
											STATUS_SMALL = ONE; smallCtr = itemLength; holdSpriteWidth = spriteWidth; toastTxt = sSHRINK; textStopCtr = ZERO; return;
										}
										else {
											if (STATUS_SLOW != ONE) {
												STATUS_SLOW = ONE; slowCtr = itemLength; holdSpawnRate = spawnRate; spawnRate += NINE; holdGameSpeed = gameSpeed; gameSpeed /= THREE;
												toastTxt = sSLOW; textStopCtr = ZERO; return;
											}
											else {score += HUNDRED; toastTxt = sPOINTS; textStopCtr = ZERO; return;}
										}
									}
									else {score += HUNDRED; toastTxt = sPOINTS; textStopCtr = ZERO; return;}
								}
								else if (tempSpriteIn.drawableID == INSTANCE_BOMB) {lives = ZERO; playLoseLife = ONE; return;}
								else if (tempSpriteIn.drawableID == INSTANCE_EXTRA_LIFE && lives <= TWO) {
									lives++;
									toastTxt = sLIFE;
									textStopCtr = ZERO;
									playLife = ONE;
								}
								return;
							}
						}
					}
				}
			}
		}
		public Bitmap getBitmapForIV() {
			return btt;
		}
		public void doStart() {synchronized (surfaceHolder) {setState(STATE_RUNNING); setRunning(TRUE);}}
		public void setRunning(boolean b) {run = b;}
	}	//end embedded thread class
	
	static PopThread thread;
	Typeface type;
	int lal;
	Dialog id1;
	Dialog id3;
	Dialog id4;
	Button next1;
	Button back1;
	Button back3;
	Button next3;
	Button back4;
	Button play4;
	Button buttons[] = {next1, back3, next3, back4, play4};
	CheckBox cb4;
	Vibrator vibrator;
	View layout;
	static final int ZERO = 0;
	static final int ONE = 1;
	static final int TWO = 2;
	static final int X300 = 300;
	static final int AD = MotionEvent.ACTION_DOWN;
	static final int APD = MotionEvent.ACTION_POINTER_DOWN;
	static final int AM = MotionEvent.ACTION_MASK;
	static final int V = View.VISIBLE;
	static final int IV = View.INVISIBLE;
	int action, actionCode;
	static final String SCORE = "score";
	static final String LIVES = "lives";
	static final String YESSCORE = "yesscore";
	static final String YESLIVES = "yeslives";
	static final String HF = "hf";
	static final String VL = "vl";
	static final String TST = "tst";
	static final String TSTSTRING = "tstString";
	static final CharSequence X = "";
	
	public PopView(Context context, AttributeSet attrs) {
		super(context, attrs);
		SurfaceHolder holder = getHolder();
		holder.setKeepScreenOn(TRUE);
		holder.addCallback(this);
		this.contextForView = context;
		type = Typeface.createFromAsset(getContext().getAssets(), "fonts/font.ttf");
		vibrator = (Vibrator)contextForView.getSystemService(Context.VIBRATOR_SERVICE);
		this.setHapticFeedbackEnabled(TRUE);
		thread = new PopThread(context, holder, ZERO, new Handler() {
			@Override
			public void handleMessage(Message m) {
				b2 = m.getData();
				if (b2.getBoolean(YESSCORE, TRUE)) scoreView.setText(b2.getCharSequence(SCORE));
				if (b2.getBoolean(YESLIVES, TRUE)) {
					lal = b2.getInt(LIVES, TWO);
					if (lal <= TWO) {
						if (lal < ZERO) imageViews[(lal * -ONE) - ONE].setVisibility(V);
						else imageViews[lal].setVisibility(IV);
					}
				}
			}
		}, new Handler() {
			@Override
			public void handleMessage(Message m) {
				bShit = m.getData();
				if (bShit.getBoolean(HF)) {vibrator.vibrate(bShit.getInt(VL, X300));}
			}
		}, this);
		playSound = Preferences.getSound(contextForView);
	}
	public void setTextViews(TextView tv1) {scoreView = tv1;}
	public void setImageViews(ImageView[] iv) {imageViews = iv; setImageViewImage(thread.getBitmapForIV());}
	public void setImageViewImage(Bitmap bm) {for (int i = 0; i < 3; i++) imageViews[i].setImageBitmap(bm);}
	public void endGame() {this.activity.finish();}
	public void setActivity(Activity a) {this.activity = a;}
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}
	public void surfaceDestroyed(SurfaceHolder holder) {
		thread.gone = true;
		boolean retry = TRUE;
		while (retry) {
			thread.setState(PopThread.STATE_PAUSED);
			retry = FALSE;
		}
	}
	public void surfaceCreated(SurfaceHolder holder) {
		thread.gone = false;
		if (thread.state == PopThread.STATE_PAUSED);
		else {
			if (Preferences.getInstructions(contextForView)) {
				id1 = new Dialog(contextForView) {
					@Override
					public boolean onSearchRequested() {
						this.dismiss();
						id3.show();
						return TRUE;
					}
				};
				id1.requestWindowFeature(Window.FEATURE_NO_TITLE);
				id1.setCancelable(FALSE);
				id1.requestWindowFeature(Window.FEATURE_NO_TITLE);
				id1.setContentView(R.layout.instructions_dialog1);
				id3 = new Dialog(contextForView) {
					@Override
					public boolean onSearchRequested() {
						this.dismiss();
						id4.show();
						return TRUE;
					}
				};
				id3.requestWindowFeature(Window.FEATURE_NO_TITLE);
				id3.setCancelable(FALSE);
				id3.requestWindowFeature(Window.FEATURE_NO_TITLE);
				id3.setContentView(R.layout.instructions_dialog3);
				id4 = new Dialog(contextForView) {
					@Override
					public boolean onSearchRequested() {
						this.dismiss();
						thread.doStart();
						thread.start();
						return TRUE;
					}
				};
				id4.requestWindowFeature(Window.FEATURE_NO_TITLE);
				id4.setCancelable(FALSE);
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
						endGame();
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
						Preferences.setInstructions(contextForView, !cb4.isChecked());
						thread.doStart();
						thread.start();
					}
				});
				id1.show();
			}
			else {
				thread.doStart();
				thread.start();
			}
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		action = e.getAction();
		actionCode = action & AM;
		if (actionCode == AD || actionCode == APD) {
			if (thread.state == PopThread.STATE_RUNNING) {
				thread.updateInput(e.getPointerCount(), e);
				if (playSound) {post(new Runnable() {
					public void run() {
						thread.updateSound();
					}
				});}
			}
			else if (thread.state == PopThread.STATE_PAUSED) thread.state = PopThread.STATE_RUNNING;
			return TRUE;
		}
		return FALSE;
	}
	public PopThread getThread() {return thread;}
}