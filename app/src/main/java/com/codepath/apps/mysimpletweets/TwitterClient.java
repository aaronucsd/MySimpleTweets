package com.codepath.apps.mysimpletweets;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

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
 * https://dev.twitter.com/oauth/application-only
 *
 * WHERE we define the endpoints calls to Twitter's api and get response.
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;//FlickrApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1";//"http://api.flickr.com/services"; // Change this, base API URL
    /*
Consumer Key (API Key)	KM55it73s8Tiw6HtHssoMUp07
Consumer Secret (API Secret)	jvIYSA3cnKUaSH7CfNcBrvUPuMpw8wT4zpuTjiNdbsLlCQwv3N
    * */
	public static final String REST_CONSUMER_KEY = "KM55it73s8Tiw6HtHssoMUp07";       // Change this
	public static final String REST_CONSUMER_SECRET = "jvIYSA3cnKUaSH7CfNcBrvUPuMpw8wT4zpuTjiNdbsLlCQwv3N"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://LHTweetsApp"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	// CHANGE THIS EXAMPLE or write own
	// DEFINE METHODS for different API endpoints here
	/*public void getInterestingnessList(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("format", "json");
		client.get(apiUrl, params, handler);
	}*/

    // METHOD = ENDPOINT

    //1. FIRST ENDPOINT: HomeTimeLine - Gets us the home timeline
    // GET statuses/home_timeline.json
    // count=25
    // since_id=1 (every tweets since first one = all since beginning - good for paging)
    public void getHomeTimeLine(AsyncHttpResponseHandler handler, int count, long max_id){
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        // Specify the params to pass in the request
        RequestParams params = new RequestParams();
        //params.put("format", "json");
        if(max_id > 0)
            params.put("max_id", max_id);
        params.put("count", count);
        params.put("since_id", 1);
        // Execute the request
        getClient().get(apiUrl, params, handler);// like $.get() in jquery
    }

    public void getMentionsTimeline(JsonHttpResponseHandler handler, int count, long max_id) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", count);
        params.put("since_id", 1);
        if (max_id > 0){
            params.put("max_id", max_id);
        }
        getClient().get(apiUrl,params, handler);
    }

    public void getUserTimeline(JsonHttpResponseHandler handler, String screenName, int count, long max_id) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", count);
        params.put("since_id", 1);
        if (max_id > 0){
            params.put("max_id", max_id);
        }
        params.put("screen_name", screenName);
        getClient().get(apiUrl,params, handler);
    }

    // Compose a tweet
    //2. SECOND ENDPOINT: POST statuses/update - tweeting
    //https://dev.twitter.com/rest/reference/post/statuses/update
    // POST statuses/update.json
    // status=Maybe%20he%27ll%20finally%20find%20his%20keys.%20%23peterfalk
    public void postTweet(AsyncHttpResponseHandler handler, String msg){
        String apiUrl = getApiUrl("statuses/update.json");
        // Specify the params to pass in the request
        RequestParams params = new RequestParams();
        //params.put("format", "json");
        params.put("status", msg);
        // Execute the request
        getClient().post(apiUrl, params, handler);// like $.post() in jquery
    }

    // Compose a tweet
    // 3. THIRD ENDPOINT: Verify User
    // https://dev.twitter.com/rest/reference/get/account/verify_credentials
    // GET account/verify_credentials
    public void verifyUser(AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("account/verify_credentials.json");
        // Execute the request
        getClient().get(apiUrl, handler);// like $.get() in jquery
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