package com.example.diabetes;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class SignActivity extends Activity {

	String result="";
	private String NameSpace = "http://140.127.22.4/DiabetesAPP/";
    private String MethodName = "SignAccountData";
    private String MethodName2 = "CheckAccountRepeat";
    private String url = "http://140.127.22.4/DiabetesAPP/WebService.asmx";
    private String soapAction1 = NameSpace + MethodName;
    private String soapAction2 = NameSpace + MethodName2;
    
	private Button signbtn;
	private EditText accountedittext,passwordedittext1,passwordedittext2,nameedittext,ageedittext,idedittext,emailedittext,teledittext,familynameedittext,familyteledittext;
	private RadioGroup sexradiogroup;
	private RadioButton manradiobutton,womanradiobutton;
	private String account,password,checkpassword,name,sex = "男",age,id,email,tel,familyname,familytel;
	private SimpleAdapter adapter;
	Toast toast;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign);
		
		signbtn = (Button)findViewById(R.id.SignButton);
		accountedittext = (EditText)findViewById(R.id.AccountEditText);
		passwordedittext1 = (EditText)findViewById(R.id.PasswordEditText);
		passwordedittext2 = (EditText)findViewById(R.id.CheckpasswordEditText);
		nameedittext = (EditText)findViewById(R.id.NameEditText);
		ageedittext = (EditText)findViewById(R.id.AgeEditText);
		idedittext = (EditText)findViewById(R.id.IdEditText);
		emailedittext = (EditText)findViewById(R.id.EmailEditText);
		teledittext = (EditText)findViewById(R.id.PhoneEditText);
		familynameedittext = (EditText)findViewById(R.id.FamilynameEditText);
		familyteledittext = (EditText)findViewById(R.id.FamilytelEditText);
		sexradiogroup = (RadioGroup)findViewById(R.id.SexRadioGroup);
		manradiobutton = (RadioButton)findViewById(R.id.BefRadio);
		womanradiobutton = (RadioButton)findViewById(R.id.AftRadio);
		
		signbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				account = String.valueOf(accountedittext.getText());
				password = String.valueOf(passwordedittext1.getText());
				checkpassword = String.valueOf(passwordedittext2.getText());
				name = String.valueOf(nameedittext.getText());
				sexradiogroup.setOnCheckedChangeListener(checkradio);
				age = String.valueOf(ageedittext.getText());
				id = String.valueOf(idedittext.getText());
				email = String.valueOf(emailedittext.getText());
				tel = String.valueOf(teledittext.getText());
				familyname = String.valueOf(familynameedittext.getText());
				familytel = String.valueOf(familyteledittext.getText());
				if(password.equals(checkpassword) )
				{
					if(checkaccount(account))
					{
						if(Insert_into(account,password,name,sex,age,id,email,tel,familyname,familytel))
						{
							toast = Toast.makeText(SignActivity.this,"註冊成功", Toast.LENGTH_SHORT);
							toast.show();
						}
						else 
						{
							toast = Toast.makeText(SignActivity.this,"註冊失敗", Toast.LENGTH_SHORT);
							toast.show();
						}
					}
					else 
					{
						toast = Toast.makeText(SignActivity.this,"帳號重複", Toast.LENGTH_SHORT);
						toast.show();
					}
				}
				else 
				{
					//利用Toast的靜態函式makeText來建立Toast物件
					toast = Toast.makeText(SignActivity.this,"請確認密碼", Toast.LENGTH_SHORT);
					//顯示Toast
					toast.show();
				}
				
			}
		});
	}
	
	public boolean checkaccount(String checkrepeat)
	{
		boolean msg = false;
		SoapObject request = new SoapObject(NameSpace, MethodName2);
		
		request.addProperty("Account",checkrepeat); //類似post概念
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);	
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		HttpTransportSE ht = new HttpTransportSE(url);
		Log.e("request", String.valueOf(request));
		try
		{
			ht.call(soapAction2, envelope);
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
			Log.e("response", String.valueOf(response));
            result = response.toString();
            Log.e("result", result);
            if(result.equals("true"))
            {
            	msg = true;
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
        return msg;
	}
	
	public boolean Insert_into(String inaccount,String inpassword,String inname,String insex,String inage,String inid,String inemail,String intel,String infamilyname,String infamilytel)
	{
		boolean msg = false;
		SoapObject request = new SoapObject(NameSpace, MethodName);  
        
		request.addProperty("Account",account); //類似post概念
        request.addProperty("Password",inaccount);
        request.addProperty("Name",inname);
        request.addProperty("Sex",insex);
        request.addProperty("Age",inage);
        request.addProperty("Id",inid);
        request.addProperty("Email",inemail);
        request.addProperty("Tel",intel);
        request.addProperty("FamilyName",infamilyname);
        request.addProperty("FamilyTel",infamilytel);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE ht = new HttpTransportSE(url);
        
        try
        {
        	ht.call(soapAction1, envelope);
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
			Log.e("response", String.valueOf(response));
			result = response.toString();
			Log.e("result", result);
			
            if(result.equals("true"))
            {
            	msg = true;
            }
        }catch(IOException e){
        	// TODO Auto-generated catch block
			e.printStackTrace();
        }catch(XmlPullParserException e){
        	// TODO Auto-generated catch block
			e.printStackTrace();
        }catch (ClassCastException e) {
        	
		}
        return msg;
	}
	
	public RadioGroup.OnCheckedChangeListener checkradio = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			switch (checkedId) {
			case R.id.BefRadio:
				sex = "男";
				break;
			case R.id.AftRadio:
				sex = "女";
				break;
			default:
				break;
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign, menu);
		return true;
	}

}
