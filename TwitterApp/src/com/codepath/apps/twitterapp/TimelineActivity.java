package com.codepath.apps.twitterapp;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.twitterapp.fragments.HomeTimelineFragment;
import com.codepath.apps.twitterapp.fragments.MentionsFragment;
import com.codepath.apps.twitterapp.models.Tweet;
import com.codepath.apps.twitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends FragmentActivity implements TabListener {
	private final int TWEET_REQUEST_CODE = 20;
	public static final String REQUEST_USER = "user";
	
	private HomeTimelineFragment homeTL;
	private MentionsFragment mentions;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		setupNavigationTabs();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}
	
	public void onComposeTweet(MenuItem mi) {
		Intent i = new Intent(this, ComposeTweet.class);
		startActivityForResult(i, TWEET_REQUEST_CODE);
	}
	
	public void onProfileView(MenuItem mi) {
		TwitterApp.getRestClient().getAccountDetails(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject response) {
				User user = User.fromJson(response);
				Intent i = new Intent(getBaseContext(), ProfileActivity.class);
				i.putExtra(REQUEST_USER, user);
				startActivity(i);
			}
			
			@Override
			public void onFailure(Throwable e, JSONObject err) {
				Log.e("err", "Getting user error in profile " + e.toString());
			}
		});
	}
	
	private void setupNavigationTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(0xFF33B5E5));
		
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		
		Tab tabHome = actionBar.newTab()
				.setText("Home")
				.setTag("HomeTimelineFragment")
				.setIcon(R.drawable.ic_home)
				.setTabListener(this);
		Tab tabMentions = actionBar.newTab()
				.setText("Mentions")
				.setTag("MentionsTimelineFragment")
				.setIcon(R.drawable.ic_mentions)
				.setTabListener(this);
		
		actionBar.addTab(tabHome);
		actionBar.addTab(tabMentions);
		actionBar.selectTab(tabHome);
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
			if(getActionBar().getSelectedTab().getTag() == "HomeTimelineFragment") {
				Tweet myTweet = Tweet.fromJson(jsonRes);
				homeTL.getAdapter().insert(myTweet, 0);
			}
		}
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// Unused
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		FragmentManager manager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction fts = manager.beginTransaction();
		if(tab.getTag() == "HomeTimelineFragment") {
			if(homeTL == null) {
				homeTL = new HomeTimelineFragment();
			}
			fts.replace(R.id.flList, homeTL);
		} else if (tab.getTag() == "MentionsTimelineFragment") {
			if(mentions == null) {
				mentions = new MentionsFragment();
			}
			fts.replace(R.id.flList, new MentionsFragment());
		}
		fts.commit();
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// Unused
	}
}
