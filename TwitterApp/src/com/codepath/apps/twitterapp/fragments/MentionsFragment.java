package com.codepath.apps.twitterapp.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.twitterapp.EndlessScrollListener;
import com.codepath.apps.twitterapp.TwitterApp;
import com.codepath.apps.twitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class MentionsFragment extends TweetsListFragment {
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
				if(getLastTweetId() != -1) {
					// this was getting called before onActivityCreated,
					// so adding checking to ensure this is always done after
					// data populated
					createMoreDataFromApi(15);
				}
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
		TwitterApp.getRestClient().getMentionsTimeline(getLastTweetId(),
				offset, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
				
				getAdapter().addAll(tweets);
				
				//test2
				if(tweets.size() > 0) {
					setLastTweetId(tweets.get(tweets.size()-1).getId()-1);
				}
			}
			
			@Override
			public void onFailure(Throwable e, JSONObject err) {
				Log.e("err", "Error getting mentions timeline: " + e.toString());
				e.printStackTrace();
			}
		});
	}
	
	private void fetchTimelineAsync(long newestTweetId) {
		TwitterApp.getRestClient().getNewestMentions(newestTweetId, new JsonHttpResponseHandler() {
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
                Log.e("err", "Fetch mentions timeline error: " + e.toString());
            }
        });
	}
}
