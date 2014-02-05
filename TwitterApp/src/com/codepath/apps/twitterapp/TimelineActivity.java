package com.codepath.apps.twitterapp;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.twitterapp.models.Tweet;
import com.codepath.apps.twitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class TimelineActivity extends Activity {
	private PullToRefreshListView lvTweets;
	private long lastTweetId;
	private TweetsAdapter tweetAdapter;
	private ArrayList<Tweet> tweets;
	
	private final int TWEET_REQUEST_CODE = 20;
	public static final String REQUEST_USER = "user";
	private User user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		
		lvTweets = (PullToRefreshListView) findViewById(R.id.lvTweets);
		lastTweetId = TwitterClient.TWITTER_NO_ID;
		
		tweets = new ArrayList<Tweet>();
		tweetAdapter = new TweetsAdapter(getBaseContext(), tweets);
		lvTweets.setAdapter(tweetAdapter);
		
		createMoreDataFromApi(25);
		
		setupListeners();
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
	
	private void setupListeners() {
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				createMoreDataFromApi(16);
				
			}
		});
		
		lvTweets.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				if(tweetAdapter.getCount() > 0) {
					fetchTimelineAsync(tweetAdapter.getItem(0).getId());
				}
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
			
			@Override
			public void onFailure(Throwable e, JSONObject err) {
				Log.e("ERROR", "Error getting timeline: " + e.toString());
			}
		});
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
				Log.e("ERROR", "Getting user error " + e.toString());
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
				Log.e("ERROR", "Error composing tweet" + e.toString());
				e.printStackTrace();
			}
			Tweet myTweet = Tweet.fromJson(jsonRes);
			tweetAdapter.insert(myTweet, 0);
		}
	}
	
	private void fetchTimelineAsync(long newestTweetId) {
		TwitterApp.getRestClient().getNewestTimeline(newestTweetId, new JsonHttpResponseHandler() {
            public void onSuccess(JSONArray json) {
            	ArrayList<Tweet> tweets = Tweet.fromJson(json);
            	while(tweets.size() > 0) {
            		int tweetSize = tweets.size()-1;
            		tweetAdapter.insert(tweets.get(tweetSize), 0);
            		tweets.remove(tweetSize);
            	}
                lvTweets.onRefreshComplete();
            }

            public void onFailure(Throwable e) {
                Log.e("ERROR", "Fetch timeline error: " + e.toString());
            }
        });
	}
}
