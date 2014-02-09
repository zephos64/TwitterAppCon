package com.codepath.apps.twitterapp.models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Tweet {
private String body;
private long uid;
private boolean favorited;
private boolean retweeted;
private String timestamp;

    private User user;

    public User getUser() {
        return user;
    }

    public String getBody() {
        return body;
    }

    public long getId() {
        return uid;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public boolean isRetweeted() {
        return retweeted;
    }
    
    public String getTimestamp() {
    	return timestamp;
    }

    public static Tweet fromJson(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
         tweet.body = jsonObject.getString("text");
         tweet.uid = jsonObject.getLong("id");
         tweet.favorited = jsonObject.getBoolean("favorited");
         tweet.retweeted = jsonObject.getBoolean("retweeted");
         tweet.timestamp = jsonObject.getString("created_at");
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        } catch (JSONException e) {
            Log.e("err", "Error in parsing Tweet " + e.toString());
            e.printStackTrace();
            return null;
        }
        return tweet;
    }

    public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
            	Log.e("err", "Error in parsing Tweet in tweet list " + e.toString());
            	e.printStackTrace();
                continue;
            }

            Tweet tweet = Tweet.fromJson(tweetJson);
            if (tweet != null) {
                tweets.add(tweet);
            }
        }

        return tweets;
    }
}