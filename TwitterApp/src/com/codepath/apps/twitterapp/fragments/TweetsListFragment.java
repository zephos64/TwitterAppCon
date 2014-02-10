package com.codepath.apps.twitterapp.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.TweetsAdapter;
import com.codepath.apps.twitterapp.TwitterClient;
import com.codepath.apps.twitterapp.models.Tweet;

import eu.erikw.PullToRefreshListView;

public class TweetsListFragment extends Fragment {
	TweetsAdapter tweetAdapter;
	PullToRefreshListView lvTweets;
	private long lastTweetId;
	
	@Override
	public View onCreateView(LayoutInflater inf,
			ViewGroup parent,
			Bundle savedInstanceState) {
		lastTweetId = TwitterClient.TWITTER_NO_ID;
		return inf.inflate(R.layout.fragment_tweets_list, parent, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		progressBar = (ProgressBar) getActivity().findViewById(R.id.pbProgess);
		progressBar.setVisibility(ProgressBar.INVISIBLE);
		
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		tweetAdapter = new TweetsAdapter(getActivity(), tweets);
		lvTweets = (PullToRefreshListView) getActivity().findViewById(R.id.lvTweets);
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
	
	private ProgressBar progressBar;
	public void showProgressBar() {
		progressBar.setVisibility(ProgressBar.VISIBLE);
    }
	
	public void hideProgressBar() {
		progressBar.setVisibility(ProgressBar.INVISIBLE);
	}
}
