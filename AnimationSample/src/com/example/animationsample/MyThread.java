package com.example.animationsample;

import android.util.Log;

public class MyThread extends Thread {

	private MyView mv;
	private int sleepMilles;
	private int framepos;
	private boolean runFlag;
	private boolean runState;
	
	public MyThread(MyView mv) {
		this.mv = mv;
		sleepMilles = 1000;
		framepos = 0;
		runState = false;
	}
	
	
	public void setSleepMilles(int sleepMilles) {
		this.sleepMilles = sleepMilles;
	}
	
	public void setRunFlag(boolean runFlag) {
		this.runFlag = runFlag;
	}
	
	public void stopThread(){
		runFlag = false;
	}
	public void setRunState(boolean runState) {
		this.runState = runState;
	}
	
	@Override
	public void run() {
		while (runFlag) {
			mv.setCurPos(framepos);
			
			mv.postInvalidate();
			//Log.d("mythread.run", framepos+"");
			
			if (runState){
				framepos++;
			}
			
			try {
				Thread.sleep(sleepMilles);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
