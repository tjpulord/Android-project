package com.example.animationsample;

import android.util.Log;

public class ComputeThread extends Thread {
	public interface InformationListener{
		public float getInformation();
	}
	
	private static final float RATIO = 10;
	
	private int  threshold[] = {800,  600,  400, 300, 200, 100,  70,  50,   40, 20};
	private int speedArray[] = {5,    10,   20,   30,  50,  80,  100, 130, 160, 200};
	private int span = 10;
	private boolean flag = true;
	private float speed, acc, miles;
	private MyView mv;
	private InformationListener listener;
	
	public ComputeThread(MyView mv) {
		this.mv = mv;
		miles = (float) 0.0;
		speed = (float) 0.0;
		acc = (float) 0.0;
	}
	
	public void setListener(InformationListener listener) {
		this.listener = listener;
	}
	
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
	@Override
	public void run() {
		while (flag) {
			// get the acceleration
			if (listener != null){
				acc = listener.getInformation();
			}
			float tt = (float) (span/1000.0);
			if(acc > 1){
				miles += speed * tt + acc*tt*tt/2;
				speed += acc * tt;
			}else {
				miles += speed * tt;
			}
			
			mv.setStrInfo("当前速度："+speed, "行驶距离："+miles);
			Log.d("compute thread", "acc:"+acc+"speed:"+speed+"miles:"+miles);

			for(int i=0; i<speedArray.length; i++){
				if(speed < speedArray[i]){
					mv.mThread.setSleepMilles(threshold[i]);
					break;
				}
			}
			if (speed > speedArray[speedArray.length-1]){
				mv.mThread.setSleepMilles(10);
			}
			if (speed < 1){
				mv.mThread.setRunState(false);
			}else {
				mv.mThread.setRunState(true);
			}
			
			try {
				Thread.sleep(span);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}
