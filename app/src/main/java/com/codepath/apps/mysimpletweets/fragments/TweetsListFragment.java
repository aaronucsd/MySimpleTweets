package com.codepath.apps.mysimpletweets.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.activity.ProfileActivity;
import com.codepath.apps.mysimpletweets.adapter.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.listeners.EndlessScrollListener;
import com.codepath.apps.mysimpletweets.models.Tweet;

import java.util.ArrayList;

/**
 * Created by long on 3/15/15.
 */
public class TweetsListFragment extends Fragment {
    protected ArrayList<Tweet> tweets;//array of tweet models
    protected TweetsArrayAdapter aTweets;//adapter to display the tweets
    protected ListView lvTweets;
    protected TwitterClient client;
    protected int count = 15;
    protected long max_id = 0;

    // Append more data into the adapter - for infinitescroll
    public void customLoadMoreDataFromApi(int offset) {
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter
        int startPos = offset;
        populateTimeline(startPos);
    }

    public void populateTimeline(int startPos) {
        //have fragment override
    }

    // 1. Inflation logic -  Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, parent, false);//not attach yet, just inflate

        // STEP A. SETPUP VIEW
        // 1. Find the listview
        lvTweets = (ListView) v.findViewById(R.id.lvTweets);

        // STEP B. SETUP LISTENERS and ADAPTERS
        setupListeners();

        return v;
    }

    private void setupListeners(){
        // 2. Create the arrayList (data source) - contains the models
        tweets = new ArrayList<>();
        // 3. Construct the adapter from the (data source)
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);

        // 4. Connect the adapter to the listview
        lvTweets.setAdapter(aTweets);
        //infinite scroll
        // Attach the listener to the AdapterView onCreate
        lvTweets.setOnScrollListener(new EndlessScrollListener() {//requires the view
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                if(totalItemsCount < count)//if only one do not get more
                    return;

                Tweet tweet = aTweets.getItem(totalItemsCount - 1);
                max_id = tweet.getUid()+1;
                customLoadMoreDataFromApi(page);

            }
        });

        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), ProfileActivity.class);


                Tweet tweet = aTweets.getItem(position);

                // Pass user object to the profile view
                i.putExtra("user", tweet.getUser());

                // Launch the child view (profile tweet)
                startActivity(i);
            }
        });

        populateTimeline(0);// Send api req and get Timeline data and fill listView (this view)
    }

    // 2. Creation lifecycle event - Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {//does not require the view
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient();
    }

    public TweetsArrayAdapter getAdapter(){
        return aTweets;
    }



}
