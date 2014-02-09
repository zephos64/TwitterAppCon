package com.codepath.apps.twitterapp;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = "hs7VtAmaZVXOjM811dotAg";       // Change this
    public static final String REST_CONSUMER_SECRET = "LSpSxI3xL9qWRImYskcXzBOsGGT9FM8cK6ecPgdAdc"; // Change this
    public static final String REST_CALLBACK_URL = "oauth://twitterapp"; // Change this (here and in manifest)
    
    public static final int TWITTER_NO_ID = -1;
    
    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }
    
    public void getHomeTimeline(long from, int max, AsyncHttpResponseHandler handler) {
    	String url = getApiUrl("statuses/home_timeline.json");
    	Log.d("debug", "Getting home timeline with url: " + url
    			+ " and from ID: " + from + " with this many records: " + max);
    	
    	RequestParams params = new RequestParams();
    	params.put("count", String.valueOf(max));
    	if(from != TWITTER_NO_ID) {
    		params.put("max_id", String.valueOf(from));
    	}
    	
    	getClient().get(url, params, handler);	
    }
    
    public void getNewestTimeline(long after, AsyncHttpResponseHandler handler) {
    	String url = getApiUrl("statuses/home_timeline.json");
    	Log.d("debug", "Getting newest timeline with url: " + url);
    	
    	RequestParams params = new RequestParams();
    	params.put("since_id", String.valueOf(after+1));
    	
    	getClient().get(url, params, handler);	
    }
    
    public void postTweet(String body, AsyncHttpResponseHandler handler) {
    	String apiUrl = getApiUrl("statuses/update.json");
    	RequestParams params = new RequestParams();
    	params.put("status", body);
    	getClient().post(apiUrl, params, handler);
    }
    
    public void getAccountDetails(AsyncHttpResponseHandler handler) {
    	String apiUrl = getApiUrl("account/verify_credentials.json");
    	getClient().get(apiUrl, handler);
    }
    
    public void getUserTimeline(int count, long userId, long lastTweetId, AsyncHttpResponseHandler handler) {
    	String apiUrl = getApiUrl("statuses/user_timeline.json");
    	Log.d("debug", "Getting user with url: " + apiUrl + " with count " + count + " with userId "
    			+ userId + " lastTweetId " + lastTweetId);
    	
    	RequestParams params = new RequestParams();
    	params.put("count", String.valueOf(count));
    	params.put("user_id", String.valueOf(userId));
    	if(lastTweetId != -1) {
    		params.put("max_id", String.valueOf(lastTweetId));
    	}
    	
    	getClient().get(apiUrl, params, handler);
    }
    
    public void getNewestUser(long after, long userId, AsyncHttpResponseHandler handler) {
    	String url = getApiUrl("statuses/mentions_timeline.json");
    	Log.d("debug", "Getting newest user with url: " + url);
    	
    	RequestParams params = new RequestParams();
    	params.put("since_id", String.valueOf(after));
    	params.put("user_id", String.valueOf(userId));
    	
    	getClient().get(url, params, handler);	
    }
    
    public void getMentionsTimeline(long from, int max, AsyncHttpResponseHandler handler) {
    	String url = getApiUrl("statuses/mentions_timeline.json");
    	Log.d("debug", "Getting mentions timeline with url: " + url
    			+ " and from ID: " + from + " with this many records: " + max);
    	
    	RequestParams params = new RequestParams();
    	params.put("count", String.valueOf(max));
    	if(from != TWITTER_NO_ID) {
    		params.put("max_id", String.valueOf(from));
    	}
    	
    	getClient().get(url, params, handler);
    }
    
    public void getNewestMentions(long after, AsyncHttpResponseHandler handler) {
    	String url = getApiUrl("statuses/mentions_timeline.json");
    	Log.d("debug", "Getting newest mentions with url: " + url);
    	
    	RequestParams params = new RequestParams();
    	params.put("since_id", String.valueOf(after+1));
    	
    	getClient().get(url, params, handler);	
    }
    
    /* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
     * 	  i.e getApiUrl("statuses/home_timeline.json");
     * 2. Define the parameters to pass to the request (query or body)
     *    i.e RequestParams params = new RequestParams("foo", "bar");
     * 3. Define the request method and make a call to the client
     *    i.e client.get(apiUrl, params, handler);
     *    i.e client.post(apiUrl, params, handler);
     */
}