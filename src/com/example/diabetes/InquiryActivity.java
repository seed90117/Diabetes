package com.example.diabetes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.os.Bundle;
import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class InquiryActivity extends Activity {

	ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
	private String NameSpace = "http://140.127.22.4/DiabetesAPP/";
    private String InquiryOutputMethod = "InquiryOutput";
    private String url = "http://140.127.22.4/DiabetesAPP/WebService.asmx";
    private String soapAction = NameSpace + InquiryOutputMethod;
	
	private WebView outputwv;
	private Spinner valuelist;
	private ArrayAdapter<String> adapter;
	private TextView datetv1,datetv2;
	private Button drawbtn;
	private String weburl;// = "http://chart.apis.google.com/chart?cht=lc&chs=500x500&chd=t:51,49,90,100&chtt=test&chxt=x,y&chxl=0:|10/1|10/2|10/3|10/4";
	private String d1,d2,value,Account;
	private String result="";
	private String SQL[];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inquiry);
		
		drawbtn = (Button)findViewById(R.id.DrawButton);
		valuelist = (Spinner)findViewById(R.id.InquirySpinner);
		outputwv = (WebView)findViewById(R.id.InquiryOutputWebview);
		datetv1 = (TextView)findViewById(R.id.InquiryDateTextView1);
		datetv2 = (TextView)findViewById(R.id.InquiryDateTextView2);
		
		SharedPreferences pre =getSharedPreferences("User_Msg", MODE_WORLD_WRITEABLE);
		Account = pre.getString("Account", null);

		///選擇日期--------------------------------------------------------------------------
		
		datetv1.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				Calendar c = Calendar.getInstance();
				DatePickerDialog dpd = new DatePickerDialog(InquiryActivity.this,datedialog1,c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
				dpd.setTitle("開始時間");
				dpd.setMessage("選擇日期");
				dpd.setCancelable(false);
				dpd.show();
				return false;
			}
		});
		
		datetv2.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				Calendar c = Calendar.getInstance();
				DatePickerDialog dpd = new DatePickerDialog(InquiryActivity.this,datedialog2,c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
				dpd.setTitle("結束時間");
				dpd.setMessage("選擇日期");
				dpd.setCancelable(false);
				dpd.show();
				return false;
			}
		});
		
		//Spinner------------------------------------------------------------------
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,new String[]{"血壓","血糖","胰島素劑量","體重"});
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		valuelist.setAdapter(adapter);
		valuelist.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
				// TODO Auto-generated method stub
						
				value = arg0.getSelectedItem().toString();//取得選取值
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
						
			}
		        	
		 });
		
		drawbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(d1 != null && d2 != null && Account != null && value != null)
				{
					SoapObject request = new SoapObject(NameSpace, InquiryOutputMethod);
					request.addProperty("Account", Account);
					request.addProperty("StarDate", d1);
			        request.addProperty("EndDate", d2);
			        request.addProperty("InquiryValue", value);
			        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
			                SoapEnvelope.VER11);
			        envelope.dotNet = true;
			        envelope.setOutputSoapObject(request);
			        HttpTransportSE ht = new HttpTransportSE(url);
			        try {
						ht.call(soapAction, envelope);
						SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
			            result = response.toString();
			            Log.e("--", result);
			            if(!result.equals("No Data"))
			            {
			            	SQL = result.split(";");
			            	weburl = APIoutput(SQL,value);
					        WebSettings webSettings = outputwv.getSettings();
							webSettings.setJavaScriptEnabled(true);
							outputwv.setWebChromeClient(new WebChromeClient());
							outputwv.loadUrl(weburl);
			            }
			            else
			            {
			            	Toast.makeText(InquiryActivity.this, "沒有資料", Toast.LENGTH_SHORT).show();
			            }
			            
			        } catch (IOException e) {
			            // TODO Auto-generated catch block
			            e.printStackTrace();
			        } catch (XmlPullParserException e) {
			            // TODO Auto-generated catch block
			            e.printStackTrace();
			        }
			        
				}
			}
		});
				
	}
	
	private DatePickerDialog.OnDateSetListener datedialog1 = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			datetv1.setText(year+"月"+(monthOfYear+1)+"月"+dayOfMonth+"日");
			d1 = year+"/"+(monthOfYear+1)+"/"+dayOfMonth;
		}
	};
	
	private DatePickerDialog.OnDateSetListener datedialog2 = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			datetv2.setText(year+"月"+(monthOfYear+1)+"月"+dayOfMonth+"日");
			d2 = year+"/"+(monthOfYear+1)+"/"+dayOfMonth;
		}
	};
	
	public String APIoutput(String sql[],String ty)
	{
		String u = "";//"http://chart.apis.google.com/chart?cht=lc&chs=600x500&chd=t:51,49,90,100|20,30,40,20&chtt=test&chxt=x,y&chxl=0:|10/1|10/2|10/3|10/4&chco=FF0000,FF9900";
		String v1="",v2="";
		String update = "";
		int l = 0;
		
		if(sql.length > 20)
		{
			l = 10;
		}
		else
		{
			l=sql.length;
		}
		
		if(ty.equals("血壓"))
		{
			for(int i =0;i<l;i++)
			{
				String o[] = sql[i].split(",");
				if(i==0)
				{
					v1 += Double.valueOf(o[0])*100/200;
					v2 += Double.valueOf(o[1])*100/200;
					update += "|"+o[2];
				}
				else
				{
					v1 += ","+Double.valueOf(o[0])*100/200;
					v2 += ","+Double.valueOf(o[1])*100/200;
					update += "|"+o[2];
				}
			}
			u = "http://chart.apis.google.com/chart?cht=lc&chs=1000x300&chd=t:"+ v1+"|"+v2+"&chtt="+ty+"&chxt=x,y&chxl=0:"+update+"|1:"+y(ty)+"&chco=FF0000,FF9900";
		}
		else if(ty.equals("血糖"))
		{
			for(int i =0;i<l;i++)
			{
				String o[] = sql[i].split(",");
				if(i==0)
				{
					v1 += Integer.valueOf(o[0])*100/300;
					update += "|"+o[1];
				}
				else
				{
					v1 += ","+Integer.valueOf(o[0])*100/300;
					update += "|"+o[1];
				}
			}
			u = "http://chart.apis.google.com/chart?cht=lc&chs=1000x300&chd=t:"+ v1+"&chtt="+ty+"&chxt=x,y&chxl=0:"+update+"|1:"+y(ty)+"&chco=FF0000";
		}
		else if(ty.equals("胰島素劑量"))
		{
			for(int i =0;i<l;i++)
			{
				String o[] = sql[i].split(",");
				if(i==0)
				{
					v1 += Double.valueOf(o[0])*100/2;
					update += "|"+o[1];
				}
				else
				{
					v1 += ","+Double.valueOf(o[0])*100/2;
					update += "|"+o[1];
				}
			}
			u = "http://chart.apis.google.com/chart?cht=lc&chs=1000x300&chd=t:"+ v1+"&chtt="+ty+"&chxt=x,y&chxl=0:"+update+"|1:"+y(ty)+"&chco=FF0000";
		}
		else if(ty.equals("體重"))
		{
			for(int i =0;i<l;i++)
			{
				String o[] = sql[i].split(",");
				if(i==0)
				{
					v1 += Double.valueOf(o[0])*100/200;
					update += "|"+o[1];
				}
				else
				{
					v1 += ","+Double.valueOf(o[0])*100/200;
					update += "|"+o[1];
				}
			}
			u = "http://chart.apis.google.com/chart?cht=lc&chs=1000x300&chd=t:"+ v1+"&chtt="+ty+"&chxt=x,y&chxl=0:"+update+"|1:"+y(ty)+"&chco=FF0000";
		}
		
		return u;
	}
	
	public String y(String t)
	{
		String re="";
		int n = 0;
		if(t.equals("血壓"))
		{
			re += "|"+n;
			for(int i = 0;i<20;i++)
			{
				n += 10;
				re += "|"+n;
			}
		}
		else if(t.equals("血糖"))
		{
			re += "|"+n;
			for(int i = 0;i<20;i++)
			{
				n += 15;
				re += "|"+n;
			}
		}
		else if(t.equals("體重"))
		{
			re += "|"+n;
			for(int i = 0;i<20;i++)
			{
				n += 10;
				re += "|"+n;
			}
		}
		else if(t.equals("胰島素劑量"))
		{;
			for(int i = 0;i<=20;i++)
			{
				if(i<10)
				{
					re += "|0."+i;
				}
				else if(i<20){
					re += "|1."+(i%10);
				}
				else {
					re += "|2."+(i%20);
				}
			}
		}
		return re;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.inquiry, menu);
		return true;
	}

}
