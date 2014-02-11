package com.codepath.apps.twitterapp;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterapp.models.User;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ComposeTweet extends Activity {
	
	private EditText etComposeTweet;
	private ImageView ivProfile;
	private TextView tvName;
	private TextView tvScreenName;
	
	private User user;
	private int defaultCharLimit = 140;
	private int curCharCount = 0;
	private boolean disableComp = false;
	public static final String COMPOSE_KEY = "composedTweet";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose_tweet);
		
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(0xFF33B5E5));
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		etComposeTweet = (EditText) findViewById(R.id.etComposeTweet);
		ivProfile = (ImageView) findViewById(R.id.ivProfile);
		tvName = (TextView) findViewById(R.id.tvName);
		tvScreenName = (TextView) findViewById(R.id.tvScreenName);
		
		setUser();	
		setupViewListener();
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				break;
			case R.id.word_count:
				break;
			case R.id.send_tweet:
				sendTweet();
				break;
			default:
				break;
		}
		return true;
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose_tweet, menu);
		
		MenuItem item = menu.findItem(R.id.word_count);
	    item.setTitle(String.valueOf(defaultCharLimit - curCharCount));

	    return super.onPrepareOptionsMenu(menu);
	}
	
	private void refreshActionBar() {
		ActivityCompat.invalidateOptionsMenu(this);
	}

	private void setupViewListener() {
		etComposeTweet.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				curCharCount = etComposeTweet.length();
				refreshActionBar();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(disableComp == false &&
						(curCharCount > defaultCharLimit)) {
					//Disable functionality, tweet too long
					disableComp = true;
				} else if (disableComp == true &&
						(curCharCount <= defaultCharLimit)) {
					disableComp = false;
				}
			}
		});
	}
	
	public void sendTweet() {
		if(disableComp) {
			new AlertDialog.Builder(this)
					.setTitle("Too Many Characters")
					.setMessage("Tweets only allow up to " + defaultCharLimit +
							"characters, please shorten your tweet and try again")
					.setPositiveButton(android.R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// continue with delete
								}
							}).show();
			return;
		}
		TwitterApp.getRestClient().postTweet(etComposeTweet.getText().toString(),
				new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(String response) {
		         Intent i = new Intent();
		         i.putExtra(COMPOSE_KEY , response);
		         setResult(RESULT_OK, i);
		         finish();
		     }
			
			@Override
			public void onFailure(Throwable e, String err) {
				Log.e("err", e.toString());
			}
		});
	}
	
	private void setUser() {
		TwitterApp.getRestClient().getAccountDetails(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject response) {
				user = User.fromJson(response);
				ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), ivProfile);
				tvName.setText(user.getName());
				tvScreenName.setText("@"+user.getScreenName());
			}
			
			@Override
			public void onFailure(Throwable e, JSONObject err) {
				Log.e("err", "Getting user error in compose " + e.toString());
			}
		});
	}
}
