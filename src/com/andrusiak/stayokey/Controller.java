package com.andrusiak.stayokey;

import java.util.Calendar;
import java.util.Date;

import com.andrusiak.stayokey.activity.QuestionActivity;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

public class Controller {

	private static Controller mController;
	private static PreferenceManager prefs;
	private Context mContext;

	public static synchronized Controller getInstance(Context context){
		if(mController==null){
			mController = new Controller(context);
		}
		return mController;
	}

	private Controller(){
	}
	
	private Controller(Context context){
		mContext = context;
		prefs = PreferenceManager.getInstance(context);
	}
	
	/*
	 * Set alarm notification and start to count days
	 */
	public void startChallenge() {
		prefs.setDayCount(1);
		setEveryDayNotification();
	}
	
	Notification buildNotification(){
		PreferenceManager pm = PreferenceManager.getInstance(mContext);
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(mContext)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setContentTitle(mContext.getText(R.string.app_name))
		        .setContentText(mContext.getText(R.string.question_have_you_been_angry_today))
		        .setAutoCancel(true);
		
		//set sound and vibrate to notification
		if(pm.getRingtone()!=null)
		        mBuilder.setSound(pm.getRingtone());
		if (pm.getVibrate()){
			mBuilder.setVibrate(new long[]{100, 200, 100, 500}); 
		}
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(mContext, QuestionActivity.class);

		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
		// Adds the back stack for the Intent (but not the Intent itself)
//		stackBuilder.addParentStack(QuestionActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =
		    (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		int mId = 1;
		Notification notification = mBuilder.build();
		mNotificationManager.notify(mId, mBuilder.build());
		return notification;
	}
	
	
	@SuppressWarnings("deprecation")
	private void setEveryDayNotification(){
		//define AlarmManager to start our notifications later
        AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        
        Intent intent = new Intent(mContext, QuestionNotification.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        // Just in case we have already set up AlarmManager,
        // we do cancel.
        am.cancel(pendingIntent);

        //Some simple code to define time of notification:
        Calendar cal = Calendar.getInstance();
        Date stamp =  cal.getTime();
        String notifyTime = PreferenceManager.getInstance(mContext).getNotificationTime();
		int pos = notifyTime.indexOf(":");
        int hour = Integer.parseInt(notifyTime.substring(0, pos));
        int minute = Integer.parseInt(notifyTime.substring(pos+1));
        stamp.setHours(hour);
        stamp.setMinutes(minute);
        stamp.setSeconds(0);

        // In case it's too late notify user today
        if(stamp.getTime() < System.currentTimeMillis())
            stamp.setTime(stamp.getTime() + AlarmManager.INTERVAL_DAY);
                
        // Set one-time alarm
        am.setRepeating(AlarmManager.RTC_WAKEUP, stamp.getTime(), AlarmManager.INTERVAL_DAY, pendingIntent);
        //am.set(AlarmManager.RTC_WAKEUP, stamp.getTime(), pendingIntent);
	}
	
	public void finishChallenge() {
		prefs.clearAttempt();
	}

	public void resumeChallenge() {

		int day = prefs.getDayCount();
		if(day>=21){
			Toast.makeText(mContext, R.string.challenge_congrat, Toast.LENGTH_LONG)
			.show();
		}
		if(day!= -1){
			prefs.setDayCount(++day);
		}
	}

}
