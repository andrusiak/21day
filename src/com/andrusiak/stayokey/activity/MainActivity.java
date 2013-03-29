package com.andrusiak.stayokey.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.andrusiak.stayokey.R;
import com.andrusiak.stayokey.controller.Controller;
import com.andrusiak.stayokey.preference.PreferenceManager;

public class MainActivity extends Activity {

	private boolean isFailed = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(Controller.getInstance(getApplicationContext()).isChallengeStarted()){
			Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			this.finish();
		}
		setContentView(R.layout.activity_main);
		
		//get status of a previous game - it is failed if you angry these days
		Intent intent = getIntent();
		if (intent!=null){
			isFailed = intent.getBooleanExtra("failed",false);
		}
		
		//make text linked with web URL and clickable 
		TextView textMore = (TextView)findViewById(R.id.bowel_more);
		textMore.setMovementMethod(LinkMovementMethod.getInstance());
		
		//init button actions
		Button startButton = (Button)findViewById(R.id.start_button);
		if(isFailed){
			startButton.setText(R.string.try_again);
		}else{
			startButton.setText(R.string.start);
		}
		startButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				
				//create singletone controller to start a functionality
				Controller.getInstance(getApplicationContext()).startChallenge();
				Toast.makeText(getApplicationContext(), 
						String.format(MainActivity.this.getString(R.string.challenge_start), PreferenceManager.getInstance(MainActivity.this).getNotificationTime()), 
						Toast.LENGTH_SHORT).show();
				
				MainActivity.this.finish();
			}});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_settings:
	            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
	            startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

}
