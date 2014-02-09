package com.codepath.apps.twitterapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterapp.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetsAdapter extends ArrayAdapter<Tweet> {

	public TweetsAdapter(Context context, List<Tweet> tweets) {
		super(context, 0, tweets);
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
		
		TextView nameView = (TextView) view.findViewById(R.id.tvName);
		String formattedName = "<b>" + tweet.getUser().getName()
				+ "</b>" + "<small><font color='#777777'> @" +
				tweet.getUser().getScreenName() + "</font></small>";
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
		String temp = (String) DateUtils.getRelativeTimeSpanString(
				date.getTime(),
				System.currentTimeMillis(),
				DateUtils.SECOND_IN_MILLIS,
				DateUtils.FORMAT_ABBREV_RELATIVE);
		timeView.setText(Html.fromHtml(temp));
		
		return view;
	}
}
