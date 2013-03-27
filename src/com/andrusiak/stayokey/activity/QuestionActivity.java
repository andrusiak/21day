package com.andrusiak.stayokey.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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

public class QuestionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);
		// Show the Up button in the action bar.
		setupActionBar();
		
		TextView textDays = (TextView)findViewById(R.id.text_days_count);
		String text = String.format(this.getText(R.string.overall_time_with_angriness).toString(), 
				PreferenceManager.getInstance(getApplicationContext()).getDayCount());
		textDays.setText(text);

		Button buttonYes = (Button)findViewById(R.id.btn_yes);
		Button buttonNo = (Button)findViewById(R.id.btn_no);
		//set actions done after clicks on the buttons
		buttonYes.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Controller.getInstance(getApplicationContext()).finishChallenge();
				QuestionActivity.this.finish();
				Intent intent = new Intent(QuestionActivity.this, MainActivity.class);
				intent.putExtra("failed", true);
				startActivity(intent);
			}});
		
		buttonNo.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Controller.getInstance(getApplicationContext()).resumeChallenge();
				Toast.makeText(getApplicationContext(),
						R.string.challenge_is_continuing, Toast.LENGTH_SHORT)
						.show();
				QuestionActivity.this.finish();
			}});
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.question, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_settings:
	            Intent intent = new Intent(QuestionActivity.this, SettingsActivity.class);
	            startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
