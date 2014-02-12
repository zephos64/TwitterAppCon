package com.codepath.apps.twitterapp.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.codepath.apps.twitterapp.EndlessScrollListener;
import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.TweetsAdapter;
import com.codepath.apps.twitterapp.TwitterClient;
import com.codepath.apps.twitterapp.models.Tweet;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public abstract class TweetsListFragment extends Fragment {
	TweetsAdapter tweetAdapter;
	PullToRefreshListView lvTweets;
	private long lastTweetId;
	private ProgressBar progressBar;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tweetAdapter = new TweetsAdapter(getActivity(), new ArrayList<Tweet>());
		lastTweetId = TwitterClient.TWITTER_NO_ID;
		createMoreDataFromApi(25);
	}
	
	@Override
	public View onCreateView(LayoutInflater inf,
			ViewGroup parent,
			Bundle savedInstanceState) {
		View view = inf.inflate(R.layout.fragment_tweets_list, parent, false);

		progressBar = (ProgressBar) view.findViewById(R.id.pbProgess);
		lvTweets = (PullToRefreshListView) view.findViewById(R.id.lvTweets);
		
		showProgressBar();
		setupListeners();
		
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		lvTweets.setAdapter(tweetAdapter);
	}
	
	public TweetsAdapter getAdapter() {
		return tweetAdapter;
	}
	
	public long getLastTweetId() {
		return lastTweetId;
	}
	
	public void setLastTweetId(long id) {
		lastTweetId = id;
	}
	
	public void showProgressBar() {
		if(progressBar != null) {
			progressBar.setVisibility(ProgressBar.VISIBLE);
		}
    }
	
	public void hideProgressBar() {
		if(progressBar != null) {
			progressBar.setVisibility(ProgressBar.INVISIBLE);
		}
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
	
	abstract void createMoreDataFromApi(int i);
	abstract void fetchTimelineAsync(long l);
}
