package com.codepath.apps.twitterapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterapp.models.Tweet;
import com.codepath.apps.twitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetsAdapter extends ArrayAdapter<Tweet> {
	Context cont;
	public TweetsAdapter(Context context, List<Tweet> tweets) {
		super(context, 0, tweets);
		cont = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if(view == null) {
			LayoutInflater inflater = 
					(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.tweet_item, null);
		}
		
		Tweet tweet = getItem(position);
		
		ImageView imageView = (ImageView) view.findViewById(R.id.ivProfile);
		ImageLoader.getInstance().displayImage(tweet.getUser().getProfileImageUrl(), imageView);
		imageView.setOnClickListener(new myOnClickListener(position));
		
		TextView nameView = (TextView) view.findViewById(R.id.tvName);
		String formattedName = "<b>";
		int maxLength = 35;
		if(tweet.getUser().getName().length() >= maxLength) {
			formattedName += tweet.getUser().getName().substring(0, maxLength) + "...</b>";
		} else if (tweet.getUser().getName().length() +
				tweet.getUser().getScreenName().length()*.75
				> maxLength) {
			formattedName += tweet.getUser().getName()
					+ "</b>" + "<small><font color='#777777'> @" +
					tweet.getUser().getScreenName().substring(0, maxLength-tweet.getUser().getName().length())
					+ "...</font></small>";
		} else {
			formattedName += tweet.getUser().getName()
				+ "</b>" + "<small><font color='#777777'> @" +
				tweet.getUser().getScreenName() + "</font></small>";
		}
		nameView.setText(Html.fromHtml(formattedName));
		
		TextView bodyView = (TextView) view.findViewById(R.id.tvBody);
		bodyView.setText(Html.fromHtml(tweet.getBody()));
		
		TextView timeView = (TextView) view.findViewById(R.id.tvTimestamp);
		Date date = new Date();
		try {
			date = new SimpleDateFormat("EEE MMM dd kk:mm:ss ZZZZZ yyyy", Locale.ENGLISH).parse(tweet.getTimestamp());
		} catch (ParseException e) {
			Log.e("err" , "Error in constructing date: " + e.toString());
			e.printStackTrace();
		}
		String time = (String) DateUtils.getRelativeTimeSpanString(
				date.getTime(),
				System.currentTimeMillis(),
				DateUtils.SECOND_IN_MILLIS,
				DateUtils.FORMAT_ABBREV_RELATIVE);
		time = time.replace("secs ago", "s");
		time = time.replace("sec ago", "s");
		time = time.replace("mins ago", "m");
		time = time.replace("min ago", "m");
		time = time.replace("hours ago", "h");
		time = time.replace("hour ago", "h");
		time = time.replace("Yesterday", "1 d");
		time = time.replace("days ago", "d");
		timeView.setText(Html.fromHtml(time));
	
		return view;
	}
	
	public class myOnClickListener implements OnClickListener{
        private int position;
        public myOnClickListener(int position){
            this.position=position;
        }
        @Override
        public void onClick(View v) {
            openProfileWithUserFromPosition(position);
        }
    }
	
	public void openProfileWithUserFromPosition(int position) {
		Log.d("Testing Click", "Getting item at position " + position + " with id "
				+ getItem(position).getId());
        TwitterApp.getRestClient().getUser(getItem(position).getUser().getId(),
        		new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject response) {
				User user = User.fromJson(response);
				Intent i = new Intent(cont, ProfileActivity.class);
				i.putExtra(TimelineActivity.REQUEST_USER, user);
				cont.startActivity(i);
			}
			
			@Override
			public void onFailure(Throwable e, JSONObject err) {
				Log.e("err", "Getting user error in profile in adapter " + e.toString());
				e.printStackTrace();
			}
		});
	}
}
