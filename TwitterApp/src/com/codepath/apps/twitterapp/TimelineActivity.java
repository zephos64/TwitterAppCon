package com.codepath.apps.twitterapp;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.twitterapp.fragments.HomeTimelineFragment;
import com.codepath.apps.twitterapp.models.Tweet;
import com.codepath.apps.twitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends FragmentActivity {
	private HomeTimelineFragment fragmentTweets;
	
	private final int TWEET_REQUEST_CODE = 20;
	public static final String REQUEST_USER = "user";
	private User user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);

		
		fragmentTweets = (HomeTimelineFragment) getSupportFragmentManager().
				findFragmentById(R.id.fragmentTweets);
		
		fragmentTweets.createMoreDataFromApi(25);

		setUser();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}
	
	public void onComposeTweet(MenuItem mi) {
		Intent i = new Intent(this, ComposeTweet.class);
		i.putExtra(REQUEST_USER, user);
		startActivityForResult(i, TWEET_REQUEST_CODE);
	}
	
	private void setUser() {
		TwitterApp.getRestClient().getAccountDetails(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject response) {
				user = User.fromJson(response);
				setTitle("@"+user.getScreenName());
			}
			
			@Override
			public void onFailure(Throwable e, JSONObject err) {
				Log.e("err", "Getting user error " + e.toString());
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK && requestCode == TWEET_REQUEST_CODE) {
			String response = data.getStringExtra(ComposeTweet.COMPOSE_KEY);
			JSONObject jsonRes = new JSONObject();
			try {
				jsonRes = new JSONObject(response);
			} catch (JSONException e) {
				Log.e("err", "Error composing tweet" + e.toString());
				e.printStackTrace();
			}
			Tweet myTweet = Tweet.fromJson(jsonRes);
			fragmentTweets.getAdapter().insert(myTweet, 0);
		}
	}
}
