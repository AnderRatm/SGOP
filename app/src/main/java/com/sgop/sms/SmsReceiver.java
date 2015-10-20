package com.sgop.sms;

import com.projeto.SGOP.R;
import com.sgop.activity.CadastroActivityConf;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;

public class SmsReceiver extends BroadcastReceiver {
	public static String cod;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		Bundle bundle = intent.getExtras();
		SmsMessage[] msgs = null;
		String str = "";
		if (bundle != null) {
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];
			for (int i = 0; i < msgs.length; i++) {
				msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
			}
			
			// Fazer algo com msgs
	          
			char[] msg = (msgs[0].getDisplayMessageBody()).toCharArray();

			//static String cod = "";
			// String cod = msgs[0].getDisplayMessageBody();
			cod = cod.copyValueOf(msg, (msg.length - 5), 5);
			String texto = cod.copyValueOf(msg, 0, msg.length - 5);
			if (texto.equals("Esse é seu código de ativação: ")) {
				// Stop it being passed to the main Messaging inbox
				abortBroadcast();
				NotificationManager notificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
				Notification notification = new Notification(R.drawable.ic_launcher, "New Message (My Messaging app)", System.currentTimeMillis());
				// Uses the default lighting scheme
				notification.defaults |= Notification.DEFAULT_LIGHTS;
				// Will show lights and make the notification disappear when the presses it
				notification.flags |= Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS;
				Intent notificationIntent = new Intent(context, CadastroActivityConf.class);
				PendingIntent pendingIntent =PendingIntent.getActivity(context, 0,
						new Intent(context, CadastroActivityConf.class), 0);
				
				Log.i("Wakeup", "Display Wakeup");
				PowerManager pm = (PowerManager)context.getApplicationContext().getSystemService(Context.POWER_SERVICE);
				WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "Phone WakeUp");
				wakeLock.acquire();
				
				notification.setLatestEventInfo(context, "My Messaging App(New Message)",(CharSequence) msgs[0], pendingIntent);
				//notification.sound.
				notification.defaults |= Notification.DEFAULT_SOUND;
				notificationManager.notify(9999, notification);
			Intent it = new Intent(context, CadastroActivityConf.class);
			Bundle params = new Bundle();
			
			//código recebido na mensagem SMS
			params.putString("cod_email", cod);
			it.putExtras(params);
			context.sendBroadcast(it);
			//startActivity(it);
			  }else{
				//Continue Broadcasting to main android app
			  }
				  
		}
	}

}
