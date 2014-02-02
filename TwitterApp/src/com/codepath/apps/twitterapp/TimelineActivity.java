package com.codepath.apps.twitterapp;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.codepath.apps.twitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends Activity {
	private ListView lvTweets;
	private long lastTweetId;
	private TweetsAdapter tweetAdapter;
	private ArrayList<Tweet> tweets;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		
		lvTweets = (ListView) findViewById(R.id.lvTweets);
		lastTweetId = TwitterClient.TWITTER_NO_ID;
		
		tweets = new ArrayList<Tweet>();
		tweetAdapter = new TweetsAdapter(getBaseContext(), tweets);
		lvTweets.setAdapter(tweetAdapter);
		
		createMoreDataFromApi(25);
		
		setupListeners();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}
	
	public void onComposeTweet(MenuItem mi) {
		Intent i = new Intent(this, ComposeTweet.class);
		startActivity(i);
	}
	
	private void setupListeners() {
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				createMoreDataFromApi(16);
				
			}
		});
	}
	
	public void createMoreDataFromApi(int offset) {
		TwitterApp.getRestClient().getHomeTimeline(lastTweetId,
				offset, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {				
				tweets = Tweet.fromJson(jsonTweets);
				if(lastTweetId != TwitterClient.TWITTER_NO_ID) {
					//max_id returns last element of previous list
					// so remove that tweet when next get tweets
					tweets.remove(0);
				}
				tweetAdapter.addAll(tweets);
				
				lastTweetId = tweets.get(tweets.size()-1).getId();
			}
		});
	}
}
