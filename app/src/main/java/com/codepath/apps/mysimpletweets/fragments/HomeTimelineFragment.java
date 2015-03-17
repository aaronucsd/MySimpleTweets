package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by long on 3/15/15.
 */
public class HomeTimelineFragment extends TweetsListFragment {//TweetsListFragment is the base fragment

    private int count = 15;
    private long max_id = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Send an API request to the timeline json api/endpoint
    // Then fill the listview (this view) by creating the tweet objects (models) from the json
    @Override
    public void populateTimeline(int startPos) {
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



}
