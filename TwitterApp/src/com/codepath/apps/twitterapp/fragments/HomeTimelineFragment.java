package com.codepath.apps.twitterapp.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.twitterapp.TwitterApp;
import com.codepath.apps.twitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class HomeTimelineFragment extends TweetsListFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public void createMoreDataFromApi(int offset) {
		TwitterApp.getRestClient().getHomeTimeline(getLastTweetId(),
				offset, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				hideProgressBar();
				ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);

				getAdapter().addAll(tweets);
				
				setLastTweetId(tweets.get(tweets.size()-1).getId()-1);
			}
			
			@Override
			public void onFailure(Throwable e, JSONObject err) {
				hideProgressBar();
				Log.e("err", "Error getting timeline: " + e.toString());
				e.printStackTrace();
			}
		});
	}
	
	public void fetchTimelineAsync(long newestTweetId) {
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
