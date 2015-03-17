package com.codepath.apps.mysimpletweets.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.fragments.HomeTimelineFragment;
import com.codepath.apps.mysimpletweets.fragments.MentionsTimelineFragment;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

public class TimelineActivity extends ActionBarActivity {
    public static final int FORM_REQUEST_CODE = 23;
    private User user;
    private TwitterClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        //getSupportActionBar().setLogo();
        getSupportActionBar().setTitle("Twitter App");


        //GET USER INFO and save to prefs.
        getAuthenticatedUserInfo();

        // Get the ViewPager and set it's PagerAdapter so that it can display items in this activity
        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        vpPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), user));

        // Give the PagerSlidingTabStrip the ViewPager - see gradle
        //http://guides.codepath.com/android/Sliding-Tabs-with-PagerSlidingTabStrip
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(vpPager);
    }


    private void getAuthenticatedUserInfo() {
        // 5. Get the client: get a singleton client and get the data to display in this view
        // use the same client across all our activities.
        client = TwitterApplication.getRestClient();

        //6 . Create Shared Preferences and store verified user's info for retrieval later.
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final SharedPreferences.Editor edit = prefs.edit();

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
        if (id == R.id.action_compose) {
            Toast.makeText(this, "Compose clicked", Toast.LENGTH_SHORT).show();
            //Tell it where to go on success authenticated (got access_token)
            Intent i = new Intent(this, ComposeTweetActivity.class);

            // Pass user object to the compose view
            i.putExtra("user", user);

            // Launch the child view (compose tweet)
            startActivityForResult(i, FORM_REQUEST_CODE);//child to send back result, requestCode - tells which action from child was requesting
            return true;
        }
        else if(id == R.id.action_profile){
            Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show();
            //Tell it where to go on success authenticated (got access_token)
            Intent i = new Intent(this, ProfileActivity.class);

            // Pass user object to the profile view
            i.putExtra("user", user);

            // Launch the child view (profile tweet)
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if( requestCode == FORM_REQUEST_CODE ){
            if (resultCode == RESULT_OK){
                JSONObject json;
                //try {
                    //json = new JSONObject(data.getStringExtra("jsonResult"));
                    //Tweet tweet = Tweet.fromJSON(json);
                    //fragmentTweetsList.insert(tweet, 0);
                //} catch (JSONException e) {
                //    e.printStackTrace();
                //}
                ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
                vpPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), user));
            }
        }
    }

    /* which controls the order of the tabs, the titles and their associated content */
    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private final static int PAGE_COUNT = 2;
        private String tabTitles[] = new String[] { "Home", "Mentions" };
        private HomeTimelineFragment homeTimelineFragment;
        private MentionsTimelineFragment mentionsTimelineFragment;

        public MyPagerAdapter(FragmentManager fm, User user) {
            super(fm);
            homeTimelineFragment = new HomeTimelineFragment();
            mentionsTimelineFragment = MentionsTimelineFragment.newInstance(user);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show Home fragment
                    return homeTimelineFragment;
                case 1: // Fragment # 1 - This will show Mention fragment
                    return mentionsTimelineFragment;
                default:
                    return null;
            }
           // return AFragment.newInstance(position + 1);
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position){
            // Generate title based on item position
            return tabTitles[position];
        }

    }
}
