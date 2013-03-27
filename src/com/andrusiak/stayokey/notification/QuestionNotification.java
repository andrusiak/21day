package com.andrusiak.stayokey.notification;

import com.andrusiak.stayokey.controller.Controller;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class QuestionNotification extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Controller.getInstance(context).buildNotification();
		
		// Here we set next notification, in day interval
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				intent, PendingIntent.FLAG_CANCEL_CURRENT);
		am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
				+ AlarmManager.INTERVAL_DAY, pendingIntent);
	}
}