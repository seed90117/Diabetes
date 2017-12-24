package com.example.diabetes;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {

	private ImageButton recordimgbtn;
	private Button logoutbtn,remindbtn,doctorsrecommendbtn,inquirybtn;
	private Toast toast;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		recordimgbtn = (ImageButton)findViewById(R.id.RecordPageImageButton);
		recordimgbtn.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it = new Intent();
				it.setClass(HomeActivity.this, RecordActivity.class);
				startActivity(it);
			}
        });
		
		logoutbtn = (Button)findViewById(R.id.LogoutButton);
		logoutbtn.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences pre = getSharedPreferences("User_Msg", MODE_WORLD_WRITEABLE);
				pre.edit().remove("Account").commit();
				toast = Toast.makeText(HomeActivity.this,"µn¥X¦¨¥\", Toast.LENGTH_SHORT);
				toast.show();
				Intent it = new Intent();
				it.setClass(HomeActivity.this, MainActivity.class);
				startActivity(it);
				finish();
			}});
		
		remindbtn = (Button)findViewById(R.id.RemindPageButton);
		remindbtn.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it = new Intent();
				it.setClass(HomeActivity.this, RemindActivity.class);
				startActivity(it);
			}
		});
		
		doctorsrecommendbtn = (Button)findViewById(R.id.DoctorsRecommendButton);
		doctorsrecommendbtn.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent();
				it.setClass(HomeActivity.this, DoctorsRecommendActivity.class);
				startActivity(it);
			}
		});
		
		inquirybtn = (Button)findViewById(R.id.InquiryPageButton);
		inquirybtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent();
				it.setClass(HomeActivity.this, InquiryActivity.class);
				startActivity(it);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

}
