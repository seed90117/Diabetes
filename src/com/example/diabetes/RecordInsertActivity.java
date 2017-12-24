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
	
	private String account,date,blood_d="0",blood_s="0",time="���e",blood_v="0",insulin_d="0",drug_n,weight="0",food,food_p="",sport;
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
					toast = Toast.makeText(RecordInsertActivity.this,"�������\", Toast.LENGTH_SHORT);
					toast.show();
					finish();
				}
				else
				{
					toast = Toast.makeText(RecordInsertActivity.this,"��������", Toast.LENGTH_SHORT);
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
				time = "���e";
				break;
			case R.id.AftRadio:
				time = "����";
				break;
			default:
				break;
			}
		}
	};
	
	public void onGet(View v) {
    	String dir = Environment.getExternalStoragePublicDirectory(  //���o�t�Ϊ����ι��ɸ��|
                     Environment.DIRECTORY_PICTURES).toString();
    	String fname = "p" + System.currentTimeMillis() + ".jpg";  //�Q�Υثe�ɶ��զX�X�@�Ӥ��|���ƪ��ɦW
    	imgUri = Uri.parse("file://" + dir + "/" + fname);    //�̫e�������|���ɦW�إ� Uri ����

    	Intent it = new Intent("android.media.action.IMAGE_CAPTURE");
    	it.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);    //�N uri �[���� Intent ���B�~��Ƥ�
    	startActivityForResult(it, 100);
	}
	
	public void onPick(View v) {
		Intent it = new Intent(Intent.ACTION_GET_CONTENT);    //�ʧ@�]�� "������e" 
		it.setType("image/*");     		  //�]�w�n������C���������G�Ҧ��������Ϥ�
		startActivityForResult(it, 101);  //�ҰʷN��, �ín�D�Ǧ^���������	
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if(resultCode == Activity.RESULT_OK) { //�n�D���N�Ϧ��\�F
        	switch(requestCode) {
        	case 100: //���        		
        		Intent it = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imgUri);//�]���t�Φ@�ɴC����
        		sendBroadcast(it);
        		break;
        	case 101: //����ۤ�
        		imgUri = convertUri(data.getData());  //���o����ۤ��� Uri �ð� Uri �榡�ഫ
        		break;
        	}
        	showImg();  //��� imgUri �ҫ������ۤ�
        }
        else { //�n�D���N�ϨS�����\
        	Toast.makeText(this, requestCode==100? "�S�����Ӥ�": "�S������ۤ�", 
        						 Toast.LENGTH_LONG).show();
        }

    }

    
    Uri convertUri(Uri uri) {
   		if(uri.toString().substring(0, 7).equals("content")) {  //�p�G�O�H "content" �}�Y
			String[] colName = { MediaColumns.DATA };    //�ŧi�n�d�ߪ����
			Cursor cursor = getContentResolver().query(uri, colName,  //�H imgUri �i��d��
					                                   null, null, null); 
			cursor.moveToFirst();      //����d�ߵ��G���Ĥ@���O��
			uri = Uri.parse("file://" + cursor.getString(0)); //�N���|�ର Uri   			
		}
    	return uri;   //�Ǧ^ Uri ����
    }
    
    void showImg() {
    	int iw, ih, vw, vh;
    	boolean needRotate;  //�Ψ��x�s�O�_�ݭn����

    	BitmapFactory.Options option = new BitmapFactory.Options(); //�إ߿ﶵ����
		option.inJustDecodeBounds = true;      //�]�w�ﶵ�G�uŪ�����ɸ�T�Ӥ����J����
		BitmapFactory.decodeFile(imgUri.getPath(), option);  //Ū�����ɸ�T�s�J Option ��
		iw = option.outWidth;   //�� option ��Ū�X���ɼe��
		ih = option.outHeight;  //�� option ��Ū�X���ɰ���
		vw = takephotoIV1.getWidth();    //���o ImageView ���e��
		vh = takephotoIV1.getHeight();   //���o ImageView ������
		
		int scaleFactor;		
		if(iw<ih) {    //�p�G�Ϥ����e�פp�󰪫� 
			needRotate = false;       				//���ݭn����
			scaleFactor = Math.min(iw/vw, ih/vh);   // �p���Y�p��v
		}
		else {
			needRotate = true;       				//�ݭn����
			scaleFactor = Math.min(iw/vh, ih/vw);   // �N ImageView ���e�B�������ӭp���Y�p��v			
		}
    		
        option.inJustDecodeBounds = false;  //�����u���J���ɸ�T���ﶵ
        option.inSampleSize = scaleFactor;  //�]�w�Y�p���, �Ҧp 2 �h���e���N�Y�p����Ӫ� 1/2
        option.inPurgeable = true;        	//�]�w�b�O���餣����, ���\�t�αN�Ϥ����e�R��
 		Bitmap bmp = BitmapFactory.decodeFile(imgUri.getPath(), option); //���J����

 		if(needRotate) { //�p�G�ݭn����
    		Matrix matrix = new Matrix();  //�إ� Matrix ����
    		matrix.postRotate(90);         //�]�w���ਤ��
    		bmp = Bitmap.createBitmap(bmp ,//�έ�Ӫ� Bitmap ���ͤ@�ӷs�� Bitmap 
    			  0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
 		}
 		takephotoIV1.setImageBitmap(bmp);      //��ܹϤ�
 		
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
		
		request.addProperty("Account",a); //����post����
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

	/*----�W�Ƕ}�l-----*/
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
