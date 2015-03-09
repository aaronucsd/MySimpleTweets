package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.listeners.EndlessScrollListener;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.codepath.apps.restclienttemplate.ComposeTweetActivity;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimelineActivity extends ActionBarActivity {
    public static final int FORM_REQUEST_CODE = 23;
    private TwitterClient client;
    private ArrayList<Tweet> tweets;//array of tweet models
    private TweetsArrayAdapter aTweets;//adapter to display the tweets
    private ListView lvTweets;
    private User user;
    private int count = 15;
    private long max_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // 1. Find the listview
        lvTweets = (ListView) findViewById(R.id.lvTweets);
        // 2. Create the arrayList (data source) - contains the models
        tweets = new ArrayList<>();
        // 3. Construct the adapter from the (data source)
        aTweets = new TweetsArrayAdapter(this, tweets);
        // 4. Connect the adapter to the listview
        lvTweets.setAdapter(aTweets);

        //infinite scroll
        // Attach the listener to the AdapterView onCreate
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                Tweet tweet = aTweets.getItem(totalItemsCount-1);
                max_id = tweet.getUid()+1;
                customLoadMoreDataFromApi(page);
                // or customLoadMoreDataFromApi(totalItemsCount);
            }
        });

        // 5. Get the client: get a singleton client and get the data to display in this view
        // use the same client across all our activities.
        client = TwitterApplication.getRestClient();

        //6 . Create Shared Preferences and store verified user's info for retrieval later.
        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor edit = pref.edit();

        // Call TweetClient's verifyUser endpoint
        client.verifyUser(new JsonHttpResponseHandler(){
            // SUCCESS
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {//root is JSONObJECT
                if(json != null) {
                    Log.d("DEBUG", json.toString());
                    user = User.fromJSON(json);//create and save user's info to shared prefs
                    if (user != null) {
                        edit.putString("name", user.getName());
                        edit.putString("profile_image_url", user.getProfileImageUrl());
                        edit.putString("screen_name", user.getScreenName());
                        edit.putLong("id", user.getUid());
                        edit.commit();
                    }
                }
            }

            // FAILURE
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                  Log.d("DEBUG", statusCode+"");
            }
        });

        populateTimeline(0);// Send api req and get Timeline data and fill listView (this view)


    }

    // Send an API request to the timeline json api/endpoint
    // Then fill the listview (this view) by creating the tweet objects (models) from the json
    private void populateTimeline(int startPos) {
        final int innerStartPos = startPos;
        client.getHomeTimeLine(new JsonHttpResponseHandler(){
            // SUCCESS
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {//root is JSONARRAY not JSONObJECT

                if(json != null) {
                    Log.d("DEBUG", json.toString());
                    // JSON coming in here for us to DO SOMETHING

                    // 1. Deserialize json
                    // 2. Create models and add them to the adapter
                    // 3. Load the model data into the listview (this needs an adaptor)
                    ArrayList<Tweet> tweets = Tweet.fromJSONArray(json);//return us a list of tweets
                    if (innerStartPos == 0) {
                        aTweets.clear();
                    }
                    aTweets.addAll(tweets);
                }
            }

            // FAILURE
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", statusCode+"");
            }
        }, count, max_id);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //on compose actionbar clicked
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Compose clicked", Toast.LENGTH_SHORT).show();
            //Tell it where to go on success authenticated (got access_token)
            Intent i = new Intent(this, ComposeTweetActivity.class);

            // Pass user object to the compose view
            i.putExtra("user", user);

            // Launch the child view (compose tweet)
            startActivityForResult(i, FORM_REQUEST_CODE);//child to send back result, requestCode - tells which action from child was requesting
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if( requestCode == FORM_REQUEST_CODE ){
            if (resultCode == RESULT_OK){
               //refresh timeline
               //populateTimeline(0);
                JSONObject json;
                try {
                    json = new JSONObject(data.getStringExtra("jsonResult"));
                    Tweet tweet = Tweet.fromJSON(json);
                    aTweets.insert(tweet, 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    // Append more data into the adapter - for infinitescroll
    public void customLoadMoreDataFromApi(int offset) {
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter
        int startPos = offset;
        populateTimeline(startPos);
    }
}
