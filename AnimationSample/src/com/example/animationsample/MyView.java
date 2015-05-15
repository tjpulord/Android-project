package com.example.animationsample;

import com.example.animationsample.ComputeThread.InformationListener;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

public class MyView extends View{
	public static final int TOTALFRAME = 9;
	private static final String FRONTNAM = "宋体";
	
	public interface Acceletor{
		public float getAcc();
	}
	
	private Bitmap [] runFrame;
	private int curPos;
	private String speedString;
	private String distanceString;
	private boolean isPlaying;
	private int posx, posy;
	private Bitmap bm;
	private int bm_width, bm_heigh;
	private int screenW, screenH;
	private int textSize;
	private Acceletor acceListener;
	
	MyThread mThread;
	private ComputeThread cThread;

	public MyView(Context context) {
		super(context);
		speedString = "当前速度:0";
		distanceString = "形式距离:0";
		curPos = 0;
		
		// get the width and height of the screen
		Display display=((Activity)context).getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		screenH = size.y;
		screenW = size.x;
		isPlaying = false;
		textSize = (int)screenH/30;
		
		initBitmap();
		
		this.setFocusable(true);
	}

	public void initBitmap(){
		Matrix mat = new Matrix();
		runFrame = new Bitmap[TOTALFRAME];
		Bitmap bitmap;
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.run_0);
		posx = bitmap.getWidth();
		posy = bitmap.getHeight();
		float scale = 0;
		if(screenH>100){
			scale = (float)screenW/posx;
			scale = scale < ((float)screenH/posy)?scale : ((float)screenH/posy);
		}
		mat.postScale((float)(scale*0.6), (float)(scale*0.6));
		
		runFrame[0] = Bitmap.createBitmap(bitmap, 0,0, posx, posy, mat, true);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.run_1);
		runFrame[1] = Bitmap.createBitmap(bitmap, 0,0, posx, posy, mat, true);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.run_2);
		runFrame[2] = Bitmap.createBitmap(bitmap, 0,0, posx, posy, mat, true);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.run_3);
		runFrame[3] = Bitmap.createBitmap(bitmap, 0,0, posx, posy, mat, true);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.run_4);
		runFrame[5] = Bitmap.createBitmap(bitmap, 0,0, posx, posy, mat, true);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.run_5);
		runFrame[4] = Bitmap.createBitmap(bitmap, 0,0, posx, posy, mat, true);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.run_6);
		runFrame[6] = Bitmap.createBitmap(bitmap, 0,0, posx, posy, mat, true);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.run_7);
		runFrame[7] = Bitmap.createBitmap(bitmap, 0,0, posx, posy, mat, true);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.run_8);
		runFrame[8] = Bitmap.createBitmap(bitmap, 0,0, posx, posy, mat, true);
		
		posx = runFrame[0].getWidth();
		posy = runFrame[0].getHeight();
		
		// the start button
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.start);
		bm_width = bitmap.getWidth();
		bm_heigh = bitmap.getHeight();
		bm = Bitmap.createBitmap(bitmap, 0,0, bm_width, bm_heigh, mat, true);
		bm_width = bm.getWidth();
		bm_heigh = bm.getHeight();
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		Paint pt = new Paint();
		
		canvas.drawColor(Color.WHITE);
		// Draw the picture
		canvas.drawBitmap(runFrame[curPos], screenW/2-posx/2, screenH/3-posy/2, null);
		
		// Draw start button
		if(!isPlaying){
			canvas.drawBitmap(bm, screenW/2-bm_width/2, screenH/3+posy, null);
		}
		// Draw text
		Typeface font = Typeface.create(FRONTNAM, Typeface.BOLD);
		pt.setTypeface(font);
		pt.setColor(Color.RED);
		pt.setTextSize(textSize);
		canvas.drawText(speedString, 50, textSize, pt);
		canvas.drawText(distanceString, 50, textSize*2, pt);
		
		super.onDraw(canvas);
	}
	
	
	
	public void setCurPos(int curPos) {
		this.curPos = curPos % TOTALFRAME;
	}
	
	public void setStrInfo(String speedString, String distanceString) {
		this.speedString = speedString;
		this.distanceString = distanceString;
	}
	
	public void setAcceListener(Acceletor acceListener) {
		this.acceListener = acceListener;
	}
	
	InformationListener inforListener = new InformationListener() {
		
		@Override
		public float getInformation() {
			if (acceListener != null){
				return acceListener.getAcc();
			}
			else {
				return 0;
			}
		}
	};
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int px = (int) event.getX();
		int py = (int) event.getY();
		
		if(!isPlaying){
			if(px > (screenW/2-bm_width/2) && px <(screenW/2+bm_width/2)
					&& py > (screenH/3+posy/2) && py<(screenH/3+posy+bm_heigh)){
				mThread = new MyThread(this);
				cThread = new ComputeThread(this);
				cThread.setFlag(true);
				mThread.setRunFlag(true);
				cThread.setListener(inforListener);
				cThread.start();
				mThread.start();
				isPlaying = !isPlaying;
			}
		}
		
		return super.onTouchEvent(event);
	}
	
	public void stop(){
		if(cThread != null){
			cThread.setFlag(false);
		}
		if(mThread != null){
			mThread.setRunFlag(false);
		}
	}
}
