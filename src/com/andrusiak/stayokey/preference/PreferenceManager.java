package com.andrusiak.stayokey.preference;

import com.andrusiak.stayokey.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

public class PreferenceManager{
	private static final String PREF_DAYS = "pref_days";
	private static final String PREF_TIME = "pref_time";
	private static final String PREF_RINGTONE = "pref_ringtone";
	private static final String PREF_VIBRATE = "pref_vibrate";

	private SharedPreferences sp;
	private static PreferenceManager mPrefsManager;
	
	public static synchronized PreferenceManager getInstance(Context context){
		if(mPrefsManager==null){
			mPrefsManager = new PreferenceManager(context);
		}
		return mPrefsManager;
	}
	
	private PreferenceManager(){
	}
	
	private PreferenceManager(Context context){
		sp = android.preference.PreferenceManager.getDefaultSharedPreferences(context);//.context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
	}
	
	/**********************************/
	
	public SharedPreferences getSharedPreference(){
		return sp;
	}
	
	public void setDayCount(int dayCount){
		sp.edit().
		putInt(PREF_DAYS, dayCount).
		commit();
	}
	
	public int getDayCount(){
		return sp.getInt(PREF_DAYS, -1);
	}

	public void setNotificationTime(String time){
		
		sp.edit()
		.putString(PREF_TIME, time)
		.commit();
	}
	
	public String getNotificationTime(){
		String time = sp.getString(PREF_TIME, "23:00");
		
		//prevent returning empty time
		if(time.equals("")){
			return Utils.DEFAULT_TIME;
		}
		return time;
	}
	
	public void clearAll() {
		sp.edit().
		clear().
		commit();
	}

	public Uri getRingtone() {
		return Uri.parse(sp.getString(PREF_RINGTONE, "default ringtone"));
	}
	
	public Boolean getVibrate() {
		return sp.getBoolean(PREF_VIBRATE, false);
	}

	public void clearAttempt() {
		sp.edit()
		.remove(PREF_DAYS)
		.commit();		
	}
}
