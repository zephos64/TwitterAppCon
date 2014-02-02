package com.codepath.apps.twitterapp;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ComposeTweet extends Activity {
	
	private EditText etComposeTweet;
	private TextView tvCharLeftCounter;
	private Button button1;
	
	private int defaultCharLimit = 140;
	private boolean disableComp = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose_tweet);
		
		etComposeTweet = (EditText) findViewById(R.id.etComposeTweet);
		tvCharLeftCounter = (TextView) findViewById(R.id.tvCharLeftCounter);
		button1 = (Button) findViewById(R.id.button1);
		
		setupViewListener();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose_tweet, menu);
		return true;
	}

	private void setupViewListener() {
		etComposeTweet.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				tvCharLeftCounter.setText(String.valueOf(defaultCharLimit - etComposeTweet.length()));
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(disableComp == false &&
						(etComposeTweet.length() > defaultCharLimit)) {
					//Disable functionality, tweet too long
					button1.setEnabled(false);
					tvCharLeftCounter.setTextColor(
							android.graphics.Color.RED);
					disableComp = true;
				} else if (disableComp == true &&
						(etComposeTweet.length() <= defaultCharLimit)) {
					tvCharLeftCounter.setTextColor(
							android.graphics.Color.DKGRAY);
					
					button1.setEnabled(true);
				}
			}
		});
	}
}
