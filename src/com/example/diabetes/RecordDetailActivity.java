package com.example.diabetes;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RecordDetailActivity extends Activity {

	private TextView UpdatetimeTV,Blood_diastolicTV,Blood_valueTV,Insulin_doseTV,Drug_nameTV,WeightTV,FoodTV,SportTV;
	private String photo;
	private ImageView food_p; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_detail);
		
		UpdatetimeTV = (TextView)findViewById(R.id.UpdatetimeTextView);
		Blood_diastolicTV = (TextView)findViewById(R.id.Blood_diastolicTextView);
		Blood_valueTV = (TextView)findViewById(R.id.Blood_valueTextView);
		Insulin_doseTV = (TextView)findViewById(R.id.Insulin_doseTextView);
		Drug_nameTV = (TextView)findViewById(R.id.Drug_nameTextView);
		WeightTV = (TextView)findViewById(R.id.WeightTextView);
		FoodTV = (TextView)findViewById(R.id.FoodTextView);
		SportTV = (TextView)findViewById(R.id.SportTextView);
		food_p = (ImageView)findViewById(R.id.Food_photoImageView);
		
		final Bundle b = this.getIntent().getExtras();
		UpdatetimeTV.setText(b.getString("DetailUpdatetime"));
		Blood_diastolicTV.setText("�αi���G"+b.getString("DetailBlood_diastolic")+"/���Y���G"+b.getString("DetailBlood_systole"));
		//Blood_systoleTV.setText("���Y���G"+b.getString("DetailBlood_systole"));
		Blood_valueTV.setText("��}�ȡG("+b.getString("Eat_time")+")  "+b.getString("DetailBlood_value"));
		Insulin_doseTV.setText("�خq�����q�G"+b.getString("DetailInsulin_dose"));
		Drug_nameTV.setText("�Ī��W�١G"+b.getString("DetailDrug_name"));
		WeightTV.setText("�魫�G"+b.getString("DetailWeight"));
		FoodTV.setText("�����G"+b.getString("DetailFood"));
		SportTV.setText("�B�ʡG"+b.getString("DetailSport"));
		photo = b.getString("DetailFood_photo");
		
		judge(b.getString("Eat_time"),b.getString("DetailBlood_value"),b.getString("DetailBlood_diastolic"),b.getString("DetailBlood_systole"));
		
		try {
			/*-----��ܹ���-----*/
	   	 	URL url = new URL(photo);
	        URLConnection conn = url.openConnection();

	        HttpURLConnection httpConn = (HttpURLConnection)conn;
	        httpConn.setRequestMethod("GET");
	        httpConn.connect();

	        if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
	            InputStream inputStream = httpConn.getInputStream();
	            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
	            inputStream.close();
	            food_p.setImageBitmap(bitmap);
	        }
	   	 	/*-----END-----*/
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void judge(String time,String Blood_value,String Blood_diastolic,String Blood_systole)
	{
		TextView JudgeBlood_diastolicTV = (TextView)findViewById(R.id.JudgeBlood_diastolicTextView);
		TextView JudgeBlood_valueTV = (TextView)findViewById(R.id.JudgeBlood_valueTextView);
		
		if(!Blood_diastolic.equals("") || !Blood_systole.equals(""))
		{
		if(Integer.valueOf(Blood_diastolic) <= 80 && Integer.valueOf(Blood_systole) <= 130)
		{
			JudgeBlood_diastolicTV.setText("�������`");
			JudgeBlood_diastolicTV.setTextColor(android.graphics.Color.GREEN);
		}
		else if(Integer.valueOf(Blood_diastolic) == 0 && Integer.valueOf(Blood_systole) == 0){
			JudgeBlood_diastolicTV.setText("");
		}
		else {
			JudgeBlood_diastolicTV.setText("�����L��");
			JudgeBlood_diastolicTV.setTextColor(android.graphics.Color.RED);
		}
		}
		
		if(!time.equals("") || !Blood_value.equals(""))
		{
		if(time.equals("���e"))
		{
			if(Integer.valueOf(Blood_value) >= 80 && Integer.valueOf(Blood_value) <=120)
			{
				JudgeBlood_valueTV.setText("��}���`");
				JudgeBlood_valueTV.setTextColor(android.graphics.Color.GREEN);
			}
			else if (Integer.valueOf(Blood_value) == 0) {
				JudgeBlood_valueTV.setText("");
			}
			else if (Integer.valueOf(Blood_value) < 80 && Integer.valueOf(Blood_value) >0) {
				JudgeBlood_valueTV.setText("��}���C");
				JudgeBlood_valueTV.setTextColor(android.graphics.Color.BLUE);
			}
			else {
				JudgeBlood_valueTV.setText("��}�L��");
				JudgeBlood_valueTV.setTextColor(android.graphics.Color.RED);
			}
		}
		else {
			if(Integer.valueOf(Blood_value) <= 140)
			{
				JudgeBlood_valueTV.setText("��}���`");
				JudgeBlood_valueTV.setTextColor(android.graphics.Color.GREEN);
			}
			else if (Integer.valueOf(Blood_value) == 0) {
				JudgeBlood_valueTV.setText("");
			}
			else {
				JudgeBlood_valueTV.setText("��}�L��");
				JudgeBlood_valueTV.setTextColor(android.graphics.Color.RED);
			}
		}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.record_detail, menu);
		return true;
	}

}
