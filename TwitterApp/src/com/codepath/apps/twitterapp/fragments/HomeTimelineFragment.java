package com.codepath.apps.twitterapp.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.twitterapp.EndlessScrollListener;
import com.codepath.apps.twitterapp.TwitterApp;
import com.codepath.apps.twitterapp.TwitterClient;
import com.codepath.apps.twitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class HomeTimelineFragment extends TweetsListFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setupListeners();
		createMoreDataFromApi(25);
	}
	
	private void setupListeners() {
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				createMoreDataFromApi(15);
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
		TwitterApp.getRestClient().getHomeTimeline(getLastTweetId(),
				offset, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
				
				if(getLastTweetId() != TwitterClient.TWITTER_NO_ID) {
					//max_id returns last element of previous list
					// so remove that tweet when next get tweets
					tweets.remove(0);
				}
				getAdapter().addAll(tweets);
				
				setLastTweetId(tweets.get(tweets.size()-1).getId());
			}
			
			@Override
			public void onFailure(Throwable e, JSONObject err) {
				Log.e("err", "Error getting timeline: " + e.toString());
				e.printStackTrace();
			}
		});
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
                Log.e("err", "Fetch timeline error: " + e.toString());
            }
        });
	}
}
