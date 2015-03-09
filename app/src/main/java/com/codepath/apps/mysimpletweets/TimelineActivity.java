package com.codepath.apps.mysimpletweets;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimelineActivity extends ActionBarActivity {

    private TwitterClient client;
    private ArrayList<Tweet> tweets;//array of tweet models
    private TweetsArrayAdapter aTweets;//adapter to display the tweets
    private ListView lvTweets;

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

        // 5. Get the client: get a singleton client and get the data to display in this view
        // use the same client across all our activities.
        client = TwitterApplication.getRestClient();
        populateTimeline();// Send api req and get Timeline data and fill listView (this view)
    }

    // Send an API request to the timeline json api/endpoint
    // Then fill the listview (this view) by creating the tweet objects (models) from the json
    private void populateTimeline() {
        client.getHomeTimeLine(new JsonHttpResponseHandler(){
            // SUCCESS
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {//root is JSONARRAY not JSONObJECT
                Log.d("DEBUG", json.toString());
                // JSON coming in here for us to DO SOMETHING

                // 1. Deserialize json
                // 2. Create models and add them to the adapter
                // 3. Load the model data into the listview (this needs an adaptor)
                ArrayList<Tweet> tweets = Tweet.fromJSONArray(json);//return us a list of tweets
                aTweets.addAll(tweets);

            }


            // FAILURE

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
