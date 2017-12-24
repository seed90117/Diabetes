package com.example.diabetes;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.os.Bundle;
import android.R.bool;
import android.R.string;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	String result="";
	private String NameSpace = "http://140.127.22.4/DiabetesAPP/";
    private String MethodName = "Login";
    private String url = "http://140.127.22.4/DiabetesAPP/WebService.asmx";
    private String soapAction = NameSpace + MethodName;
	
	private Button signpagebtn,loginbtn;
	private EditText accountET,passwordET;
	private String Act,psw;
	private Toast toast;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        accountET = (EditText)findViewById(R.id.LoginAccountEditText);
        passwordET = (EditText)findViewById(R.id.LoginPasswordEditText);
        
        
        signpagebtn = (Button)findViewById(R.id.signpage);
        signpagebtn.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent();
				it.setClass(MainActivity.this, SignActivity.class);
				startActivity(it);
			}	
        });
        
        loginbtn = (Button)findViewById(R.id.LoginButton);
        loginbtn.setOnClickListener(new Button.OnClickListener(){
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Act = String.valueOf(accountET.getText());
				psw = String.valueOf(passwordET.getText());
				if(Login(Act, psw))
				{
					toast = Toast.makeText(MainActivity.this,"登入成功", Toast.LENGTH_SHORT);
					toast.show();
					SaveUserData(Act,psw);
					Intent it = new Intent();
					it.setClass(MainActivity.this, HomeActivity.class);
					startActivity(it);
					finish();
				}
				else 
				{
					toast = Toast.makeText(MainActivity.this,"帳號密碼錯誤", Toast.LENGTH_SHORT);
					toast.show();
				}
			}
		});
    }
	
	public boolean Login(String Account,String Password)
	{
		boolean re = false;
		SoapObject request = new SoapObject(NameSpace, MethodName);
		
		request.addProperty("Account",Account); //類似post概念
		request.addProperty("Password",Password);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);	
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		HttpTransportSE ht = new HttpTransportSE(url);
		Log.e("request", String.valueOf(request));
		try
		{
			ht.call(soapAction, envelope);
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
			Log.e("response", String.valueOf(response));
            result = response.toString();
            Log.e("result", result);
            if(result.equals("true"))
            {
            	re = true;
            }
		}
		catch(IOException e){
        	// TODO Auto-generated catch block
			e.printStackTrace();
        }catch(XmlPullParserException e){
        	// TODO Auto-generated catch block
			e.printStackTrace();
        }catch (ClassCastException e) {
        	
		}
		return re;
	}
	
	public void SaveUserData(String Account,String Password)
	{
		SharedPreferences pre =getSharedPreferences("User_Msg", MODE_WORLD_WRITEABLE);
		pre.edit().putString("Account", Account).commit();
	}
	
	public void CheckLogin()
	{
		String ch;
		SharedPreferences pre =getSharedPreferences("User_Msg", MODE_WORLD_WRITEABLE);
		ch = pre.getString("Account", null);
		if(!ch.equals(null))
		{
			accountET.setText(pre.getString("Account", null));
		}
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
