package com.codepath.apps.twitterapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterapp.models.User;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ComposeTweet extends Activity {
	
	private EditText etComposeTweet;
	private TextView tvCharLeftCounter;
	private Button button1;
	private ImageView ivProfile;
	private TextView tvName;
	private TextView tvScreenName;
	
	private User user;
	private int defaultCharLimit = 140;
	private boolean disableComp = false;
	public static final String COMPOSE_KEY = "composedTweet";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose_tweet);
		
		etComposeTweet = (EditText) findViewById(R.id.etComposeTweet);
		tvCharLeftCounter = (TextView) findViewById(R.id.tvCharLeftCounter);
		button1 = (Button) findViewById(R.id.button1);
		ivProfile = (ImageView) findViewById(R.id.ivProfile);
		tvName = (TextView) findViewById(R.id.tvName);
		tvScreenName = (TextView) findViewById(R.id.tvScreenName);
		
		Intent i = getIntent();
		user = (User)i.getExtras().getSerializable(TimelineActivity.REQUEST_USER);
		ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), ivProfile);
		tvName.setText(user.getName());
		tvScreenName.setText("@"+user.getScreenName());
		
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
	
	public void sendTweet(View v) {
		TwitterApp.getRestClient().postTweet(etComposeTweet.getText().toString(),
				new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(String response) {
		         Intent i = new Intent();
		         i.putExtra(COMPOSE_KEY , response);
		         setResult(RESULT_OK, i);
		         finish();
		     }
		});
	}
}
