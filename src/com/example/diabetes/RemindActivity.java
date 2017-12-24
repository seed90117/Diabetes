package com.example.diabetes;

import java.io.IOException;
import java.net.CacheRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;


import android.opengl.Visibility;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

public class RemindActivity extends Activity {

	ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
	private String NameSpace = "http://140.127.22.4/DiabetesAPP/";
    private String InquiryClockMethod = "InquiryClock";
    private String InsertClockMethod = "InsertClock";
    private String UpdateClockMethod = "UpdateClock";
    private String DeleteClockMethod = "DeleteClock";
    private String url = "http://140.127.22.4/DiabetesAPP/WebService.asmx";
    private String soapAction = NameSpace + InquiryClockMethod;
    private String soapAction1 = NameSpace + InsertClockMethod;
    private String soapAction2 = NameSpace + UpdateClockMethod;
    private String soapAction3 = NameSpace + DeleteClockMethod;
    
    private SimpleAdapter adapter;
    private ArrayAdapter<String> arradapter;
	private String result="";
	private String Account = "";
	private String SQL[];
	
	private Button insertemindButton;
	private ListView remindListView;
	private String ID ="",Title ="",Time = "",Type = "提醒",Repetes = "重複",State ="開啟";
	
	public static Activity ReActivity = null; 
	private static final String BC_AVTION = "com.example.diabetes.action.BC_ACTION";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remind);
		
		insertemindButton = (Button)findViewById(R.id.InsertRemindButton);
		SharedPreferences pre =getSharedPreferences("User_Msg", MODE_WORLD_WRITEABLE);
		Account = pre.getString("Account", null);
		Listdata(Account);
		
		insertemindButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final Dialog dialog = new Dialog(RemindActivity.this,R.style.MyDialog);
 				dialog.setContentView(R.layout.remind_insert_dialog);
 								
				Button sendbtn = (Button)dialog.findViewById(R.id.InsertDialogButton1);
				Button cancelbtn = (Button)dialog.findViewById(R.id.InsertDialogButton2);
				ToggleButton stateTbtn = (ToggleButton)dialog.findViewById(R.id.InsertDialogToggleButton);
				final EditText titleET = (EditText)dialog.findViewById(R.id.InsertDialogEditText);
				final Spinner typeSP = (Spinner)dialog.findViewById(R.id.InsertDialogSpinner);
				final TextView dateTV = (TextView)dialog.findViewById(R.id.InsertDialogDate);
				final TextView timeTV = (TextView)dialog.findViewById(R.id.InsertDialogTime);
				final RadioGroup repetesRG = (RadioGroup)dialog.findViewById(R.id.InsertDialogRadioGroup);
				
				stateTbtn.setChecked(true);
				
				arradapter = new ArrayAdapter<String>(RemindActivity.this,android.R.layout.simple_spinner_item,new String[]{"提醒","鬧鐘"});
				arradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				typeSP.setAdapter(arradapter);
				typeSP.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
									int arg2, long arg3) {
						// TODO Auto-generated method stub
								
						Type = arg0.getSelectedItem().toString();//取得選取值
						if(Type == "提醒")
						{
							dateTV.setVisibility(View.VISIBLE);
							timeTV.setVisibility(View.GONE);
						}
						if(Type == "鬧鐘")
						{
							dateTV.setVisibility(View.GONE);
							timeTV.setVisibility(View.VISIBLE);
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
								
					}
				        	
				 });
				
				dateTV.setOnTouchListener(new OnTouchListener() {
					
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						
						Calendar c = Calendar.getInstance();
						DatePickerDialog dpd = new DatePickerDialog(RemindActivity.this,new DatePickerDialog.OnDateSetListener() {
							
							@Override
							public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
								// TODO Auto-generated method stub
								dateTV.setText(arg1+"年"+(arg2+1)+"月"+arg3+"日");
								Time = arg1+"/"+(arg2+1)+"/"+arg3;
							}
						},c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
						dpd.setTitle("提醒日期");
						dpd.setMessage("選擇日期");
						dpd.setCancelable(false);
						dpd.show();
						return false;
					}
				});
				
				timeTV.setOnTouchListener(new OnTouchListener() {
					
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						
						Calendar c = Calendar.getInstance();
						TimePickerDialog tpd = new TimePickerDialog(RemindActivity.this, new TimePickerDialog.OnTimeSetListener() {
							
							@Override
							public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
								// TODO Auto-generated method stub
								timeTV.setText(hourOfDay+"點"+minute+"分");
								Time = hourOfDay+":"+minute;
							}
						}, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
						tpd.setTitle("鬧鐘時間");
						tpd.setMessage("選擇時間");
						tpd.show();
						return false;
					}
				});
				
				repetesRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// TODO Auto-generated method stub
						switch (checkedId) {
						case R.id.InsertDialogYesRadio:
							Repetes = "重複";
							break;
						case R.id.InsertDialogNoRadio:
							Repetes = "不重複";
							break;
						default:
							break;
						}
					}
				});
				
				stateTbtn.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener(){

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if(isChecked)
						{
							State = "開啟";
						}
						else {
							State = "關閉";
						}
					}});
				
				sendbtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Title = String.valueOf(titleET.getText());
						if (InsertClock(Account, Title, Time, Type, Repetes, State))
						{
							Title = "";
							Time = "";
							Type = "提醒";
							Repetes = "重複";
							State = "開啟";
							Intent it = new Intent(RemindActivity.this,RemindActivity.class);
							startActivity(it);
							finish();
							dialog.cancel();
						}
						else {
							Toast.makeText(RemindActivity.this, "發生錯誤", Toast.LENGTH_SHORT).show();
						}
					}
				});
				
				cancelbtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				});
				
				dialog.show();
			}
		});
	}

	public void Listdata(String account)
	{
		boolean listdatatype = false;
		remindListView = (ListView)findViewById(R.id.RemindListView);
		SoapObject request = new SoapObject(NameSpace, InquiryClockMethod);
        request.addProperty("Account", Account);
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
    			 
    			item.put("ID", o[0]);
    			item.put( "Data_name", o[1]);
    			item.put("Clock_time", o[2]);
    			item.put("Clock_Type", o[3]);
    			item.put("Clock_Repetes", o[4]);
    			item.put("Clock_State", o[5]);
    			list.add( item );
    			
    			/*if(o[5].equals("開啟"))
    			{
    				if (o[3].equals("鬧鐘")) 
        			{
        				String t[] = o[2].split(":");
            			Calendar c =Calendar.getInstance();
            			c.set(Calendar.HOUR_OF_DAY, Integer.valueOf(t[0]));
        				c.set(Calendar.MINUTE, Integer.valueOf(t[1]));
        				c.set(Calendar.SECOND,0);
        	            c.set(Calendar.MILLISECOND,0);
        	            Intent it = new Intent(RemindActivity.this,MyReceiver.class);
        		        it.setAction(BC_AVTION);
        		        it.putExtra("Clock_name", o[1]);
        		        it.putExtra("Clock_type", o[3]);
        		        PendingIntent pi = PendingIntent.getBroadcast(RemindActivity.this, 0, it, 0);
        		        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        		        am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
    				}
    			}*/
    		 }
            
            adapter = new SimpleAdapter( 
    				 this,list,R.layout.remind_list,
    				 new String[] { "Data_name","Clock_time","Clock_Type","Clock_Repetes","Clock_State"},
    				 new int[] { R.id.Remind_list_Title,R.id.Remind_list_Time,R.id.Remind_list_Type,R.id.Remind_list_Repetes,R.id.Remind_list_State} );
    				 
    		//ListActivity設定adapter
        	remindListView.setAdapter(adapter);
        	remindListView.setTextFilterEnabled(true);
        	remindListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					HashMap<String,String> map=(HashMap<String,String>)remindListView.getItemAtPosition(arg2);
					String Clock_ID = map.get("ID");
					String Data_name = map.get("Data_name");
					String Clock_Time = map.get("Clock_time");
					String Clock_Type = map.get("Clock_Type");
					String Clock_Repetes = map.get("Clock_Repetes");
					String Clock_State = map.get("Clock_State");
					
					final Dialog dialog = new Dialog(RemindActivity.this,R.style.MyDialog);
	 				dialog.setContentView(R.layout.remind_insert_dialog);
	 								
					Button sendbtn = (Button)dialog.findViewById(R.id.InsertDialogButton1);
					Button cancelbtn = (Button)dialog.findViewById(R.id.InsertDialogButton2);
					Button deletelbtn = (Button)dialog.findViewById(R.id.InsertDialogButton3);
					ToggleButton stateTbtn = (ToggleButton)dialog.findViewById(R.id.InsertDialogToggleButton);
					final EditText titleET = (EditText)dialog.findViewById(R.id.InsertDialogEditText);
					final Spinner typeSP = (Spinner)dialog.findViewById(R.id.InsertDialogSpinner);
					final TextView dateTV = (TextView)dialog.findViewById(R.id.InsertDialogDate);
					final TextView timeTV = (TextView)dialog.findViewById(R.id.InsertDialogTime);
					final RadioGroup repetesRG = (RadioGroup)dialog.findViewById(R.id.InsertDialogRadioGroup);
					final RadioButton yesRB = (RadioButton)dialog.findViewById(R.id.InsertDialogYesRadio);
					final RadioButton noRB = (RadioButton)dialog.findViewById(R.id.InsertDialogNoRadio);
					
					arradapter = new ArrayAdapter<String>(RemindActivity.this,android.R.layout.simple_spinner_item,new String[]{"提醒","鬧鐘"});
					arradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					typeSP.setAdapter(arradapter);
					
					sendbtn.setText("修改");
					deletelbtn.setVisibility(View.VISIBLE);
					titleET.setText(Data_name);
					if(Clock_Type.equals("提醒"))
					{
						dateTV.setVisibility(View.VISIBLE);
						timeTV.setVisibility(View.GONE);
						dateTV.setText(Clock_Time);
						typeSP.setSelection(0);
					}
					if(Clock_Type.equals("鬧鐘"))
					{
						dateTV.setVisibility(View.GONE);
						timeTV.setVisibility(View.VISIBLE);
						timeTV.setText(Clock_Time);
						typeSP.setSelection(1);
					}
					
					if(Clock_Repetes.equals("重複"))
					{
						yesRB.setChecked(true);
					}
					if(Clock_Repetes.equals("不重複"))
					{
						noRB.setChecked(true);
					}
					
					if(Clock_State.equals("開啟"))
					{
						stateTbtn.setChecked(true);
					}
					if(Clock_State.equals("關閉"))
					{
						stateTbtn.setChecked(false);
					}
					
					ID = Clock_ID;
					Title = Data_name;
					Time = Clock_Time;
					Type = Clock_Type;
					State = Clock_Type;
					Repetes = Clock_Repetes;
					
					typeSP.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

						@Override
						public void onItemSelected(AdapterView<?> arg0, View arg1,
										int arg2, long arg3) {
							// TODO Auto-generated method stub
									
							Type = arg0.getSelectedItem().toString();//取得選取值
							if(Type == "提醒")
							{
								dateTV.setVisibility(View.VISIBLE);
								timeTV.setVisibility(View.GONE);
							}
							if(Type == "鬧鐘")
							{
								dateTV.setVisibility(View.GONE);
								timeTV.setVisibility(View.VISIBLE);
							}
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub
									
						}
					        	
					 });
					
					dateTV.setOnTouchListener(new OnTouchListener() {
						
						@Override
						public boolean onTouch(View v, MotionEvent event) {
							// TODO Auto-generated method stub
							
							Calendar c = Calendar.getInstance();
							DatePickerDialog dpd = new DatePickerDialog(RemindActivity.this,new DatePickerDialog.OnDateSetListener() {
								
								@Override
								public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
									// TODO Auto-generated method stub
									dateTV.setText(arg1+"年"+(arg2+1)+"月"+arg3+"日");
									Time = arg1+"/"+(arg2+1)+"/"+arg3;
								}
							},c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
							dpd.setTitle("提醒日期");
							dpd.setMessage("選擇日期");
							dpd.setCancelable(false);
							dpd.show();
							return false;
						}
					});
					
					timeTV.setOnTouchListener(new OnTouchListener() {
						
						@Override
						public boolean onTouch(View v, MotionEvent event) {
							// TODO Auto-generated method stub
							
							Calendar c = Calendar.getInstance();
							TimePickerDialog tpd = new TimePickerDialog(RemindActivity.this, new TimePickerDialog.OnTimeSetListener() {
								
								@Override
								public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
									// TODO Auto-generated method stub
									timeTV.setText(hourOfDay+"點"+minute+"分");
									Time = hourOfDay+":"+minute;
								}
							}, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
							tpd.setTitle("鬧鐘時間");
							tpd.setMessage("選擇時間");
							tpd.show();
							return false;
						}
					});
					
					repetesRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						
						@Override
						public void onCheckedChanged(RadioGroup group, int checkedId) {
							// TODO Auto-generated method stub
							switch (checkedId) {
							case R.id.InsertDialogYesRadio:
								Repetes = "重複";
								break;
							case R.id.InsertDialogNoRadio:
								Repetes = "不重複";
								break;
							default:
								break;
							}
						}
					});
					
					stateTbtn.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener(){

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							// TODO Auto-generated method stub
							if(isChecked)
							{
								State = "開啟";
							}
							else {
								State = "關閉";
							}
						}});
					
					sendbtn.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Title = String.valueOf(titleET.getText());
							if (updateclock(ID, Title, Time, Type, Repetes, State))
							{
								Intent it = new Intent(RemindActivity.this,RemindActivity.class);
								startActivity(it);
								finish();
								dialog.cancel();
							}
							else {
								Toast.makeText(RemindActivity.this, "發生錯誤", Toast.LENGTH_SHORT).show();
							}
						}
					});
					
					deletelbtn.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							if (deleteclock(ID))
							{
								Intent it = new Intent(RemindActivity.this,RemindActivity.class);
								startActivity(it);
								finish();
								dialog.cancel();
							}
							else {
								Toast.makeText(RemindActivity.this, "發生錯誤", Toast.LENGTH_SHORT).show();
							}
						}
					});
					
					cancelbtn.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialog.cancel();
						}
					});
					
					dialog.show();
				}
			});
        }
	}
	
	public boolean InsertClock(String a,String dn,String ct,String t,String R,String S)
	{
		boolean msg = false;
		SoapObject request = new SoapObject(NameSpace, InsertClockMethod);  
        
		request.addProperty("Account",a); //類似post概念
        request.addProperty("Data_name",dn);
        request.addProperty("Clock_time",ct);
        request.addProperty("Type",t);
        request.addProperty("Repetes",R);
        request.addProperty("State",S);
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
	
	public boolean updateclock(String ID,String Data_name,String Clock_time,String Type,String Repetes, String State)
	{
		boolean re = false;
		SoapObject request = new SoapObject(NameSpace, UpdateClockMethod);  
        
		request.addProperty("ID",ID); //類似post概念
		request.addProperty("Account","1");
        request.addProperty("Data_name",Data_name);
        request.addProperty("Clock_time",Clock_time);
        request.addProperty("Type",Type);
        request.addProperty("Repetes",Repetes);
        request.addProperty("State",State);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE ht = new HttpTransportSE(url);
        
        try
        {
        	ht.call(soapAction2, envelope);
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
			Log.e("response", String.valueOf(response));
			result = response.toString();
			Log.e("result", result);
			
            if(result.equals("true"))
            {
            	re = true;
            }
        }catch(IOException e){
        	// TODO Auto-generated catch block
			e.printStackTrace();
        }catch(XmlPullParserException e){
        	// TODO Auto-generated catch block
			e.printStackTrace();
        }catch (ClassCastException e) {
        	
		}
		return re;
	}
	
	public boolean deleteclock(String ID)
	{
		boolean re = false;
		SoapObject request = new SoapObject(NameSpace, DeleteClockMethod);  
        
		request.addProperty("ID",ID); //類似post概念
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE ht = new HttpTransportSE(url);
        
        try
        {
        	ht.call(soapAction3, envelope);
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
			Log.e("response", String.valueOf(response));
			result = response.toString();
			Log.e("result", result);
			
            if(result.equals("true"))
            {
            	re = true;
            }
        }catch(IOException e){
        	// TODO Auto-generated catch block
			e.printStackTrace();
        }catch(XmlPullParserException e){
        	// TODO Auto-generated catch block
			e.printStackTrace();
        }catch (ClassCastException e) {
        	
		}
        return re;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.remind, menu);
		return true;
	}

}
