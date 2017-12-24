package com.example.diabetes;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.R.string;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DoctorsRecommendActivity extends Activity {

	private static final String TAG = "MediaRecorderActivity";
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private Button playBtn;
    private Button recBtn;
    private TextView tv;
    private boolean isRecording;
    private boolean isPlaying;
    private String filePath;
    //-------------------------------------
    String result="";
	private String NameSpace = "http://140.127.22.4/DiabetesAPP/";
    private String MethodName = "Recommend";
    private String url = "http://140.127.22.4/DiabetesAPP/WebService.asmx";
    private String soapAction1 = NameSpace + MethodName;
    
    private String Account,SQL;
    boolean typeR = false;
    private TextView reback;
    private EditText Dccommend;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doctors_recommend);
		
		this.insert2Tv("onCreate...", false);
        this.playBtn = (Button) this.findViewById(R.id.StartMediaRecorderButton);
        this.recBtn = (Button) this.findViewById(R.id.MediaRecorderButton);
        //this.recBtn.setText("錄音");
        this.recBtn.setBackgroundResource(R.drawable.advice_record);
        //this.playBtn.setText("播放");
        this.playBtn.setBackgroundResource(R.drawable.advic_play);
        this.playBtn.setEnabled(true);
        
        //------------------------------------
        SharedPreferences pre =getSharedPreferences("User_Msg", MODE_WORLD_WRITEABLE);
		Account = pre.getString("Account", null);
		Recommend(Account);
		
		SharedPreferences prerec =getSharedPreferences("doc_rec", 0);
		filePath = prerec.getString("Rec", null);
		//Toast.makeText(DoctorsRecommendActivity.this, prerec.getString("Rec", null), Toast.LENGTH_SHORT).show();
	}
	
	public void onClick(View v) {
        this.insert2Tv("onClick...", false);
        switch (v.getId()) {
        case R.id.StartMediaRecorderButton:
            if (this.isPlaying) {
                this.stopPlay();
            }
            else {
                // 開始播放
                this.mediaPlayer = new MediaPlayer();
                try {
                	//讀取錄音檔位置
                	//SharedPreferences prerec =getSharedPreferences("doc_rec", 0);
                	//filePath = prerec.getString("Rec", null);
                	//Toast.makeText(DoctorsRecommendActivity.this, prerec.getString("Rec", null), Toast.LENGTH_SHORT).show();
                	this.mediaPlayer.setDataSource(this.filePath);
                    this.mediaPlayer.prepare();
                    this.mediaPlayer.start();
                    // 播放結束呼叫停止播放
                    this.mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            stopPlay();
                        }
                    });
                    this.isPlaying = true;
                    //this.playBtn.setText("停止播放");
                    this.playBtn.setBackgroundResource(R.drawable.advic_stop);
                    // 撥播中不可錄音
                    this.recBtn.setEnabled(false);
                    this.insert2Tv("播放中...");
                }
                catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
            break;
        case R.id.MediaRecorderButton:
            if (this.isRecording) {
                // 停止錄音
                this.mediaRecorder.stop();
                // 一定要釋放
                this.mediaRecorder.release();
                this.isRecording = false;
                //this.recBtn.setText("錄音");
                this.recBtn.setBackgroundResource(R.drawable.advice_record);
                //儲存錄音檔位置
                SharedPreferences prerec =getSharedPreferences("doc_rec", 0);
        		prerec.edit().putString("Rec",filePath ).commit();
                //Toast.makeText(DoctorsRecommendActivity.this, filePath, Toast.LENGTH_SHORT).show();
                // 錄音停止可播放
                this.playBtn.setEnabled(true);
                this.insert2Tv("錄音完成");
            }
            else {
                // 開始錄音
                this.mediaRecorder = new MediaRecorder();
                // 從麥克風收音
                this.mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                // 輸出 3gp 格式
                this.mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                // 編碼格式
                this.mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                // 輸出位置
                this.filePath = this.createFilePath();
                this.mediaRecorder.setOutputFile(this.filePath);
                try {
                    this.mediaRecorder.prepare();
                    this.mediaRecorder.start();
                    this.isRecording = true;
                    //this.recBtn.setText("停止錄音");
                    this.recBtn.setBackgroundResource(R.drawable.advic_stop);
                    // 錄音中不可播放
                    this.playBtn.setEnabled(false);
                    this.insert2Tv("錄音中...");
                }
                catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
            break;
        }
    }
	
	private void stopPlay() {
        // 停止播放
        this.mediaPlayer.stop();
        this.mediaPlayer.release();
        this.isPlaying = false;
        //this.playBtn.setText("播放");
        this.playBtn.setBackgroundResource(R.drawable.advic_play);
        // 撥播停止可錄音
        this.recBtn.setEnabled(true);
        this.insert2Tv("停止播放");
    }

    private String createFilePath() {
        File sdCardDir = Environment.getExternalStorageDirectory();
        File cw1202 = new File(sdCardDir, "cw1202");
        if (!cw1202.exists()) {
            cw1202.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".3gp";
        String path = new File(cw1202, fileName).getAbsolutePath();
        this.insert2Tv("輸出位置：" + path, false);
        return path;
    }

	private void insert2Tv(String msg) {
        this.insert2Tv(msg, true);
    }
	
	private void insert2Tv(String msg, boolean ui) {
        if (this.tv == null) {
            this.tv = (TextView) this.findViewById(R.id.RecordTypeTextView);
        }
        if (ui) {
            this.tv.setText(msg);
        }
        Log.d(TAG, msg);
    }
	
	//**********************************************
	
	public boolean Recommend(String account)
	{
		boolean msg = false;
		typeR = false;
		SoapObject request = new SoapObject(NameSpace, MethodName);
		
		request.addProperty("Account",account); //類似post概念
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE ht = new HttpTransportSE(url);
        try {
			ht.call(soapAction1, envelope);
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            result = response.toString();
            Log.e("--", result);
            if(!result.equals("No Data"))
            {
            	typeR = true;
            	SQL = result;
            }
            } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if (typeR) 
        {
        	String o[]=SQL.split(",");
        	Dccommend = (EditText)findViewById(R.id.DoctorsRecommendEditText);
        	Dccommend.setText(o[0]);
        	reback = (TextView)findViewById(R.id.ReBacktimeTextView);
        	reback.setText(o[1]);
        	msg = true;
		}
        return msg;
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.doctors_recommend, menu);
		return true;
	}

}
