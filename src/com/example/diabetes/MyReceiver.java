package com.example.diabetes;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		
		Intent it = new Intent(arg0, NotificationActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("Title",arg1.getStringExtra("Clcok_name"));
		bundle.putString("Type",arg1.getStringExtra("Clcok_type"));
		it.putExtras(bundle);
		it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		arg0.startActivity(it);
		//Toast.makeText(arg0, "¦¬¨ì¼s¼½", Toast.LENGTH_LONG).show();
	}

}
