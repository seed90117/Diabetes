package com.example.diabetes;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;

public class NotificationActivity extends Activity {
	protected static final int REFRESH_DATA = 0x00000001;
	  private Thread thread = null;  
	  private Timer timer,timer1;
	  private String type,title;
	  SoundPool sound;
	  int clock;

	  @Override
	  protected void onCreate(Bundle savedInstanceState)
	  {
	    super.onCreate(savedInstanceState);
	    Bundle bundle = new Bundle();
	    title = bundle.getString("Title");
	    type = bundle.getString("Type");
	    //先去找音效位置
	    sound = new SoundPool(10, AudioManager.STREAM_MUSIC, 6);       
	    clock = sound.load(NotificationActivity.this, R.raw.rooster , 1);
	    
	    timer = new Timer();
	    timer.schedule(new TimerTask() {
	      public void run() {
	        Message msg = new Message();
	        msg.what = 0;
	        mHandler.sendMessage(msg);
	      }
	    }, 0, 3000);  
	    timer1 = new Timer();
	    timer1.schedule(new TimerTask() {
	      public void run() {
	        Message msg = new Message();
	        msg.what = 1;
	        mHandler.sendMessage(msg);
	      }
	    }, 0, 2000); 
	    // 跳出的鬧鈴警示 
	    new AlertDialog.Builder(NotificationActivity.this).setIcon(R.drawable.clock_icon)
	        .setTitle(title).setMessage(title).setPositiveButton(
	            "關掉他", new DialogInterface.OnClickListener()
	            {
	              public void onClick(DialogInterface dialog,
	                  int whichButton)
	              { 
	                timer.cancel();
	                timer1.cancel();
	                // 關閉Activity
	                NotificationActivity.this.finish();
	              }
	            }).show();
	  }
	//開啟副執行緒,丟出警告視窗
	 Handler mHandler = new Handler() {
	  @Override
	   public void handleMessage(Message msg) {
	     switch (msg.what) {
	     // 顯示網路上抓取的資料
	     case 0:
	     //取得震動服務
	       Vibrator myVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
	       //震動2秒
	       myVibrator.vibrate(2000);       
	       break;
	       
	     case 1:       
	       sound.play(clock, 1, 1, 0, 0, 1);
	       break;
	     }
	   }
	 };
}
