package com.codepath.apps.twitterapp;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

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
		
		ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
		tvName = (TextView) findViewById(R.id.tvName);
		tvTagLine = (TextView)findViewById(R.id.tvTagLine);
		tvFollowers = (TextView)findViewById(R.id.tvFollowers);
		tvFollowing = (TextView)findViewById(R.id.tvFollowing);

		loadProfileInfo();
	}
	
	private void loadProfileInfo() {
		Intent i = getIntent();
		Bundle extras = i.getExtras();
		user = (User)extras.getSerializable(TimelineActivity.REQUEST_USER);
		setTitle("@"+user.getScreenName());
		populateProfileHeader(user);
		/*TwitterApp.getRestClient().getAccountDetails(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject response) {
				user = User.fromJson(response);
				setTitle("@"+user.getScreenName());
				populateProfileHeader(user);
			}
			
			@Override
			public void onFailure(Throwable e, JSONObject err) {
				Log.e("err", "Getting user error in profile " + e.toString());
			}
		});*/
	}
	
	private void populateProfileHeader(User u) {
		ImageLoader.getInstance().displayImage(u.getProfileImageUrl(), ivProfileImage);
		tvName.setText(u.getName());
		tvTagLine.setText(u.getTagline());
		tvFollowers.setText(u.getFollowersCount() + " Followers");
		tvFollowing.setText(u.getFriendsCount() + " Following");
		//test
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

}
