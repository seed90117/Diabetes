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

import android.R.string;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.CalendarView.OnDateChangeListener;

public class RecordActivity extends Activity {

	ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
	private String NameSpace = "http://140.127.22.4/DiabetesAPP/";
    private String InquiryReportMethod = "InquiryReport";
    private String url = "http://140.127.22.4/DiabetesAPP/WebService.asmx";
    private String soapAction = NameSpace + InquiryReportMethod;
	private CalendarView recordCV;
	private ListView lv;
	private SimpleAdapter adapter;
	private String Date,result="",Account;
	private String SQL[];
	private ImageButton insertibtnButton;
	public static Activity closerecordactivity = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);
		
		Calendar c=Calendar.getInstance();
		Date = c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH)+1) + "/" + c.get(Calendar.DAY_OF_MONTH);
		SharedPreferences pre =getSharedPreferences("User_Msg", MODE_WORLD_WRITEABLE);
		Account = pre.getString("Account", null);
		
		Listdata(Date,Account);
		
		recordCV = (CalendarView)findViewById(R.id.RecordCalendarView);
		recordCV.setOnDateChangeListener(new OnDateChangeListener(){

			@Override
			public void onSelectedDayChange(CalendarView view, int year,
					int month, int dayOfMonth) {
				// TODO Auto-generated method stub
				Date = year + "/" + (month+1) + "/" + dayOfMonth;
				list.clear();
				Listdata(Date,Account);
			}
			});
		insertibtnButton = (ImageButton)findViewById(R.id.InsertImageButton);
		insertibtnButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(RecordActivity.this, RecordInsertActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("Account", Account);
				bundle.putString("Date",Date);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}
	
	public void Listdata(String date,String account)
	{
		boolean listdatatype = false;
		lv = (ListView)findViewById(R.id.RercordList);
		SoapObject request = new SoapObject(NameSpace, InquiryReportMethod);
        request.addProperty("Account", account);
        request.addProperty("Date", date);
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
            	listdatatype = true;
            	SQL = result.split(";");
            }
            } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if(listdatatype)
        {
        	for(int i=0; i<SQL.length; i++){
        		HashMap<String,Object> item = new HashMap<String,Object>();
    			String o[]=SQL[i].split(","); 
    			
    			/*String BD = judgeJudgeBlood_diastolic(o[1],o[2]);
    			String n[] = BD.split(",");
    			String BV = judgeJudgeBlood_value(o[3],o[4]);
    			String m[] = BV.split(",");*/
    			
    			item.put("Date", "時間："+o[0]);
    			item.put( "Blood_diastolic", o[1]);
    			item.put("Blood_systole", o[2]);
    			item.put("Eat_time", o[3]);
    			item.put("Blood_value", o[4]);
    			item.put("Insulin_dose", o[5]);
    			item.put("Drug_name", o[6]);
    			item.put("Weight", o[7]);
    			item.put("Food",o[8]);
    			item.put("Food_photo",o[9]);
    			item.put("Sport",o[10]);
    			item.put("Updatetime", o[11]);
    			item.put("Blood_d",judgeJudgeBlood_diastolic(o[1],o[2]));
    			item.put("Blood_v",judgeJudgeBlood_value(o[3],o[4]));
    			list.add( item );
    		 }
        	
            adapter = new SimpleAdapter( 
    				 this,list,R.layout.recordlist,
    				 new String[] { "Updatetime","Blood_d","Blood_v"},
    				 new int[] { R.id.DateText , R.id.JudgeBlood_diastolicList , R.id.JudgeBlood_valueList} );
    				 
    		//ListActivity設定adapter
    		lv.setAdapter(adapter);
    	    lv.setTextFilterEnabled(true);
    	    lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					HashMap<String,String> map=(HashMap<String,String>)lv.getItemAtPosition(arg2);
					String DetailUpdatetime = map.get("Updatetime");
					String DetailBlood_diastolic = map.get("Blood_diastolic");
					String DetailBlood_systole = map.get("Blood_systole");
					String Eat_time = map.get("Eat_time");
					String DetailBlood_value = map.get("Blood_value");
					String DetailInsulin_dose = map.get("Insulin_dose");
					String DetailDrug_name = map.get("Drug_name");
					String DetailWeight = map.get("Weight");
					String DetailFood = map.get("Food");
					String DetailFood_photo = map.get("Food_photo");
					String DetailSport = map.get("Sport");
					
					Intent intent = new Intent();
					intent.setClass(RecordActivity.this, RecordDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("DetailUpdatetime",DetailUpdatetime);
					bundle.putString("DetailBlood_diastolic",DetailBlood_diastolic );
					bundle.putString("DetailBlood_systole",DetailBlood_systole );
					bundle.putString("Eat_time",Eat_time );
					bundle.putString("DetailBlood_value",DetailBlood_value );
					bundle.putString("DetailInsulin_dose",DetailInsulin_dose );
					bundle.putString("DetailDrug_name",DetailDrug_name);
					bundle.putString("DetailWeight",DetailWeight );
					bundle.putString("DetailFood",DetailFood );
					bundle.putString("DetailFood_photo",DetailFood_photo );
					bundle.putString("DetailSport",DetailSport );
					intent.putExtras(bundle);
					startActivity(intent);
				}
			});
        }
        else
        {
        	list.clear();
        }
	}
	
	public String judgeJudgeBlood_diastolic(String Blood_diastolic,String Blood_systole)
	{
		String JudgeBlood_diastolic="",JudgeBlood_diastolic_color="",re = "";
		if(!Blood_diastolic.equals("") || !Blood_systole.equals(""))
		{
		if(Integer.valueOf(Blood_diastolic) <= 80 && Integer.valueOf(Blood_systole)<=130)
		{
			JudgeBlood_diastolic = "血壓正常";
			JudgeBlood_diastolic_color = "android.graphics.Color.GREEN";
		}
		else if(Integer.valueOf(Blood_diastolic) == 0 && Integer.valueOf(Blood_systole) == 0){
			JudgeBlood_diastolic ="";
		}
		else {
			JudgeBlood_diastolic ="血壓過高";
			JudgeBlood_diastolic_color = "android.graphics.Color.RED";
		}
		re = JudgeBlood_diastolic;
		}
		
		return re;
	}

	public String judgeJudgeBlood_value(String time,String Blood_value)
	{
		String JudgeBlood_value = "",judgeBlood_value_colorString = "",re = "";
		if(!time.equals("") || !Blood_value.equals(""))
		{
		if(time.equals("飯前"))
		{
			if(Integer.valueOf(Blood_value) >= 80 && Integer.valueOf(Blood_value) <=120)
			{
				JudgeBlood_value = "血糖正常";
				judgeBlood_value_colorString = "android.graphics.Color.GREEN";
			}
			else if (Integer.valueOf(Blood_value) == 0) {
				JudgeBlood_value = "";
			}
			else if (Integer.valueOf(Blood_value) < 80 && Integer.valueOf(Blood_value) >0) {
				JudgeBlood_value = "血糖偏低";
				judgeBlood_value_colorString = "android.graphics.Color.BLUE)";
			}
			else {
				JudgeBlood_value = "血糖過高";
				judgeBlood_value_colorString = "android.graphics.Color.RED";
			}
		}
		else {
			if(Integer.valueOf(Blood_value) <= 140)
			{
				JudgeBlood_value = "血糖正常";
				judgeBlood_value_colorString = "android.graphics.Color.GREEN";
			}
			else if (Integer.valueOf(Blood_value) == 0) {
				JudgeBlood_value = "";
			}
			else {
				JudgeBlood_value = "血糖過高";
				judgeBlood_value_colorString = "android.graphics.Color.RED";
			}
		}
		re = JudgeBlood_value;
		}
		
		return re;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.record, menu);
		return true;
	}

}
