package com.codepath.apps.twitterapp;

import org.json.JSONArray;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.activeandroid.util.Log;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		Log.d("debug", "Created Timeline Activity");
		
		TwitterApp.getRestClient().getHomeTimeline(1, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				System.out.println("THIS IS A SUCCESS" + jsonTweets.toString());
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}

}
