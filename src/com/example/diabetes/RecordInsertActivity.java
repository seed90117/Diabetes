package com.example.diabetes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.example.diabetes.R.id;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class RecordInsertActivity extends Activity {

	String result="";
	private String NameSpace = "http://140.127.22.4/DiabetesAPP/";
    private String MethodName = "InsertReport";
    private String MethodName2 = "registrimageinsert";
    private String url = "http://140.127.22.4/DiabetesAPP/WebService.asmx";
    private String soapAction = NameSpace + MethodName;
    private String soapAction1 = NameSpace + MethodName2;
	
	private TextView DateTv;
	private EditText Blood_DEt,Blood_SEt,Blood_VEt,Insulin_DEt,Drug_NEt,WeightEt,FoodEt,SportEt;
	private Button insertbtn;
	private ImageButton takephotobtn;
	private ImageView takephotoIV1;
	private Uri imgUri;
	private Toast toast;
	private RadioGroup eat_timeGroup;
	private RadioButton befRadiobtn,aftRadiobtn;
	
	private String account,date,blood_d="0",blood_s="0",time="飯前",blood_v="0",insulin_d="0",drug_n,weight="0",food,food_p="",sport;
	private String uploadBuffer="",fileName="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_insert);
		
		DateTv = (TextView)findViewById(R.id.DateTextView);
		Blood_DEt = (EditText)findViewById(R.id.InsertBDEditText);
		Blood_SEt = (EditText)findViewById(R.id.InsertBSEditText);
		Blood_VEt = (EditText)findViewById(R.id.InsertBVEditText);
		Insulin_DEt = (EditText)findViewById(R.id.InsertIDEditText);
		Drug_NEt = (EditText)findViewById(R.id.InsertDNEditText);
		WeightEt = (EditText)findViewById(R.id.InsertWEditText);
		FoodEt = (EditText)findViewById(R.id.InsertFEditText);
		SportEt = (EditText)findViewById(R.id.InsertSEditText);
		insertbtn = (Button)findViewById(R.id.InsertButton);
		takephotoIV1 = (ImageView)findViewById(R.id.InsertImageView1);
		eat_timeGroup = (RadioGroup)findViewById(R.id.SexRadioGroup);
		befRadiobtn = (RadioButton)findViewById(R.id.BefRadio);
		aftRadiobtn = (RadioButton)findViewById(R.id.AftRadio);
		
		eat_timeGroup.setOnCheckedChangeListener(checkradio);
		
		
		final Bundle b = this.getIntent().getExtras();
		DateTv.setText(b.getString("Date"));
		account = b.getString("Account");
		
		insertbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				date = String.valueOf(DateTv.getText());
				blood_d = String.valueOf(Blood_DEt.getText());
				blood_s = String.valueOf(Blood_SEt.getText());
				blood_v = String.valueOf(Blood_VEt.getText());
				insulin_d = String.valueOf(Insulin_DEt.getText());
				drug_n = String.valueOf(Drug_NEt.getText());
				weight = String.valueOf(WeightEt.getText());
				food = String.valueOf(FoodEt.getText());
				food_p  = fileName;
				sport = String.valueOf(SportEt.getText());
				if(blood_d.equals("") || blood_d.equals(null))
				{
					blood_d = "0";
				}
				if(blood_s.equals("") || blood_s.equals(null))
				{
					blood_s = "0";
				}
				if(blood_v.equals("") || blood_v.equals(null))
				{
					blood_v = "0";
				}
				if(insulin_d.equals("") || insulin_d.equals(null))
				{
					insulin_d = "0";
				}
				if(weight.equals("") || weight.equals(null))
				{
					weight = "0";
				}
				
				if(InsertReport(account, date, blood_d, blood_s,time ,blood_v, insulin_d, drug_n, weight, food, food_p, sport))
				{
					toast = Toast.makeText(RecordInsertActivity.this,"紀錄成功", Toast.LENGTH_SHORT);
					toast.show();
					finish();
				}
				else
				{
					toast = Toast.makeText(RecordInsertActivity.this,"紀錄失敗", Toast.LENGTH_SHORT);
					toast.show();
				}
			}
		});
	}
	
public RadioGroup.OnCheckedChangeListener checkradio = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			switch (checkedId) {
			case R.id.BefRadio:
				time = "飯前";
				break;
			case R.id.AftRadio:
				time = "飯後";
				break;
			default:
				break;
			}
		}
	};
	
	public void onGet(View v) {
    	String dir = Environment.getExternalStoragePublicDirectory(  //取得系統的公用圖檔路徑
                     Environment.DIRECTORY_PICTURES).toString();
    	String fname = "p" + System.currentTimeMillis() + ".jpg";  //利用目前時間組合出一個不會重複的檔名
    	imgUri = Uri.parse("file://" + dir + "/" + fname);    //依前面的路徑及檔名建立 Uri 物件

    	Intent it = new Intent("android.media.action.IMAGE_CAPTURE");
    	it.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);    //將 uri 加到拍照 Intent 的額外資料中
    	startActivityForResult(it, 100);
	}
	
	public void onPick(View v) {
		Intent it = new Intent(Intent.ACTION_GET_CONTENT);    //動作設為 "選取內容" 
		it.setType("image/*");     		  //設定要選取的媒體類型為：所有類型的圖片
		startActivityForResult(it, 101);  //啟動意圖, 並要求傳回選取的圖檔	
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if(resultCode == Activity.RESULT_OK) { //要求的意圖成功了
        	switch(requestCode) {
        	case 100: //拍照        		
        		Intent it = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imgUri);//設為系統共享媒體檔
        		sendBroadcast(it);
        		break;
        	case 101: //選取相片
        		imgUri = convertUri(data.getData());  //取得選取相片的 Uri 並做 Uri 格式轉換
        		break;
        	}
        	showImg();  //顯示 imgUri 所指明的相片
        }
        else { //要求的意圖沒有成功
        	Toast.makeText(this, requestCode==100? "沒有拍到照片": "沒有選取相片", 
        						 Toast.LENGTH_LONG).show();
        }

    }

    
    Uri convertUri(Uri uri) {
   		if(uri.toString().substring(0, 7).equals("content")) {  //如果是以 "content" 開頭
			String[] colName = { MediaColumns.DATA };    //宣告要查詢的欄位
			Cursor cursor = getContentResolver().query(uri, colName,  //以 imgUri 進行查詢
					                                   null, null, null); 
			cursor.moveToFirst();      //移到查詢結果的第一筆記錄
			uri = Uri.parse("file://" + cursor.getString(0)); //將路徑轉為 Uri   			
		}
    	return uri;   //傳回 Uri 物件
    }
    
    void showImg() {
    	int iw, ih, vw, vh;
    	boolean needRotate;  //用來儲存是否需要旋轉

    	BitmapFactory.Options option = new BitmapFactory.Options(); //建立選項物件
		option.inJustDecodeBounds = true;      //設定選項：只讀取圖檔資訊而不載入圖檔
		BitmapFactory.decodeFile(imgUri.getPath(), option);  //讀取圖檔資訊存入 Option 中
		iw = option.outWidth;   //由 option 中讀出圖檔寬度
		ih = option.outHeight;  //由 option 中讀出圖檔高度
		vw = takephotoIV1.getWidth();    //取得 ImageView 的寬度
		vh = takephotoIV1.getHeight();   //取得 ImageView 的高度
		
		int scaleFactor;		
		if(iw<ih) {    //如果圖片的寬度小於高度 
			needRotate = false;       				//不需要旋轉
			scaleFactor = Math.min(iw/vw, ih/vh);   // 計算縮小比率
		}
		else {
			needRotate = true;       				//需要旋轉
			scaleFactor = Math.min(iw/vh, ih/vw);   // 將 ImageView 的寬、高互換來計算縮小比率			
		}
    		
        option.inJustDecodeBounds = false;  //關閉只載入圖檔資訊的選項
        option.inSampleSize = scaleFactor;  //設定縮小比例, 例如 2 則長寬都將縮小為原來的 1/2
        option.inPurgeable = true;        	//設定在記憶體不夠時, 允許系統將圖片內容刪除
 		Bitmap bmp = BitmapFactory.decodeFile(imgUri.getPath(), option); //載入圖檔

 		if(needRotate) { //如果需要旋轉
    		Matrix matrix = new Matrix();  //建立 Matrix 物件
    		matrix.postRotate(90);         //設定旋轉角度
    		bmp = Bitmap.createBitmap(bmp ,//用原來的 Bitmap 產生一個新的 Bitmap 
    			  0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
 		}
 		takephotoIV1.setImageBitmap(bmp);      //顯示圖片
 		
 		Date _date = new Date(System.currentTimeMillis());
		SimpleDateFormat _sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		String dateset=_sdf.format(_date);
		fileName = dateset+".jpg";
		
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    bmp.compress(CompressFormat.JPEG,100, baos);
	    byte [] bb=baos.toByteArray();
	    uploadBuffer = new String(Base64.encode(bb,  Base64.DEFAULT));
    }

    
	
	public Boolean InsertReport(String a,String D,String BD,String BS,String ET,String BV,String ID,String DN,String W,String F,String FP,String S)
	{
		boolean re = false;
		SoapObject request = new SoapObject(NameSpace, MethodName);
		
		request.addProperty("Account",a); //類似post概念
		request.addProperty("Date",D);
		request.addProperty("Blood_diastolic",BD);
		request.addProperty("Blood_systole",BS);
		request.addProperty("Eat_time",ET);
		request.addProperty("Blood_value",BV);
		request.addProperty("Insulin_dose",ID);
		request.addProperty("Drug_name",DN);
		request.addProperty("Weight",W);
		request.addProperty("Food",F);
		request.addProperty("Food_photo",FP);
		request.addProperty("Sport",S);
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
            	if(!FP.equals("") || !FP.equals(null))
            	{
            		upload();
            	}
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.record_insert, menu);
		return true;
	}

	/*----上傳開始-----*/
	public void upload(){
		
		if(!uploadBuffer.equals("")){
			SoapObject request = new SoapObject(NameSpace, MethodName2); 
	          
	 		  
			  //request.addProperty("x",account);
	          request.addProperty("in_image",uploadBuffer.toString() );
	          request.addProperty("fileName", fileName);
	        
	           

	          SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
	                  SoapEnvelope.VER11);
	   
	          envelope.dotNet = true;

	          envelope.setOutputSoapObject(request);
	           
	    
	          HttpTransportSE ht = new HttpTransportSE(url);

	          try {
	          	//new asyncTaskUpdateProgress().execute();
					ht.call(soapAction1, envelope);
					SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
	              result = response.toString();
	         
	              if(!result.equals("ERR")){
	              
	              	 
	              }else{
	            	  
	            	  upload();
	              	//Toast.makeText(IndexActivity.this, result, Toast.LENGTH_SHORT).show(); 
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
}
