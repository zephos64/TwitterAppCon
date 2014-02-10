package com.codepath.apps.twitterapp;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterapp.fragments.UserTimelineFragment;
import com.codepath.apps.twitterapp.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends FragmentActivity {
	private User user;
	private ImageView ivProfileImage;
	private TextView tvName;
	private TextView tvTagLine;
	private TextView tvFollowers;
	private TextView tvFollowing;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(0xFF33B5E5));
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
		tvName = (TextView) findViewById(R.id.tvName);
		tvTagLine = (TextView)findViewById(R.id.tvTagLine);
		tvFollowers = (TextView)findViewById(R.id.tvFollowers);
		tvFollowing = (TextView)findViewById(R.id.tvFollowing);

		loadProfileInfo();
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		onBackPressed();
		return true;
    }
	
	private void loadProfileInfo() {
		Intent i = getIntent();
		Bundle extras = i.getExtras();
		user = (User)extras.getSerializable(TimelineActivity.REQUEST_USER);
		setTitle("@"+user.getScreenName());
		populateProfileHeader(user);
		
		Bundle bundle = new Bundle();
		bundle.putSerializable(TimelineActivity.REQUEST_USER, user);
		Fragment fragInfo = new UserTimelineFragment();
        fragInfo.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.flUserTimeline, fragInfo);
		ft.commit();
	}
	
	private void populateProfileHeader(User u) {
		ImageLoader.getInstance().displayImage(u.getProfileImageUrl(), ivProfileImage);
		tvName.setText(u.getName());
		tvTagLine.setText(u.getTagline());
		tvFollowers.setText(u.getFollowersCount() + " Followers");
		tvFollowing.setText(u.getFriendsCount() + " Following");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

}
