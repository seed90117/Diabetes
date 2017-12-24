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
        //this.recBtn.setText("����");
        this.recBtn.setBackgroundResource(R.drawable.advice_record);
        //this.playBtn.setText("����");
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
                // �}�l����
                this.mediaPlayer = new MediaPlayer();
                try {
                	//Ū�������ɦ�m
                	//SharedPreferences prerec =getSharedPreferences("doc_rec", 0);
                	//filePath = prerec.getString("Rec", null);
                	//Toast.makeText(DoctorsRecommendActivity.this, prerec.getString("Rec", null), Toast.LENGTH_SHORT).show();
                	this.mediaPlayer.setDataSource(this.filePath);
                    this.mediaPlayer.prepare();
                    this.mediaPlayer.start();
                    // ���񵲧��I�s�����
                    this.mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            stopPlay();
                        }
                    });
                    this.isPlaying = true;
                    //this.playBtn.setText("�����");
                    this.playBtn.setBackgroundResource(R.drawable.advic_stop);
                    // ���������i����
                    this.recBtn.setEnabled(false);
                    this.insert2Tv("����...");
                }
                catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
            break;
        case R.id.MediaRecorderButton:
            if (this.isRecording) {
                // �������
                this.mediaRecorder.stop();
                // �@�w�n����
                this.mediaRecorder.release();
                this.isRecording = false;
                //this.recBtn.setText("����");
                this.recBtn.setBackgroundResource(R.drawable.advice_record);
                //�x�s�����ɦ�m
                SharedPreferences prerec =getSharedPreferences("doc_rec", 0);
        		prerec.edit().putString("Rec",filePath ).commit();
                //Toast.makeText(DoctorsRecommendActivity.this, filePath, Toast.LENGTH_SHORT).show();
                // ��������i����
                this.playBtn.setEnabled(true);
                this.insert2Tv("��������");
            }
            else {
                // �}�l����
                this.mediaRecorder = new MediaRecorder();
                // �q���J������
                this.mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                // ��X 3gp �榡
                this.mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                // �s�X�榡
                this.mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                // ��X��m
                this.filePath = this.createFilePath();
                this.mediaRecorder.setOutputFile(this.filePath);
                try {
                    this.mediaRecorder.prepare();
                    this.mediaRecorder.start();
                    this.isRecording = true;
                    //this.recBtn.setText("�������");
                    this.recBtn.setBackgroundResource(R.drawable.advic_stop);
                    // ���������i����
                    this.playBtn.setEnabled(false);
                    this.insert2Tv("������...");
                }
                catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
            break;
        }
    }
	
	private void stopPlay() {
        // �����
        this.mediaPlayer.stop();
        this.mediaPlayer.release();
        this.isPlaying = false;
        //this.playBtn.setText("����");
        this.playBtn.setBackgroundResource(R.drawable.advic_play);
        // ��������i����
        this.recBtn.setEnabled(true);
        this.insert2Tv("�����");
    }

    private String createFilePath() {
        File sdCardDir = Environment.getExternalStorageDirectory();
        File cw1202 = new File(sdCardDir, "cw1202");
        if (!cw1202.exists()) {
            cw1202.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".3gp";
        String path = new File(cw1202, fileName).getAbsolutePath();
        this.insert2Tv("��X��m�G" + path, false);
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
		
		request.addProperty("Account",account); //����post����
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
