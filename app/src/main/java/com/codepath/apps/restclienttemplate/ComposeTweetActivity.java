package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;


public class ComposeTweetActivity extends ActionBarActivity {

    private TwitterClient client;
    private EditText etMsg;
    private TextView tvUserName;
    private TextView tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_tweet);

        //ON LOAD OF THIS COMPOSE VIEW - POPULATE DEFAULTS IN SUBVIEWS
        //1. Pull out the passed result object from intent (parent passed)
        User user = (User) getIntent().getSerializableExtra("user");
        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(this);

        //2.  Find the image view
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        //3.  Load the image url into the ImageView from the Picasso
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvName = (TextView) findViewById(R.id.tvName);
        if(user != null) {
            Picasso.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);
            // 4. Load default for username and name
            tvUserName.setText(user.getScreenName());
            tvName.setText(user.getName());
        }
        else{
            Picasso.with(this).load(pref.getString("profile_image_url", "n/a")).into(ivProfileImage);
            // 4. Load default for username
            tvUserName.setText(pref.getString("username", "n/a"));
            tvName.setText(pref.getString("name", "n/a"));
        }
        // 5. Get the client: get a singleton client and get the data to display in this view
        // use the same client across all our activities.
        client = TwitterApplication.getRestClient();
        etMsg = (EditText) findViewById(R.id.etMsg);

        //on Tweet clicked
        Button b = (Button) findViewById(R.id.btnTweet);
        // Set the result
        final Intent res = new Intent();
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.postTweet(new JsonHttpResponseHandler(){
                    // SUCCESS
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject json) {//root is JSONObJECT
                        Log.d("DEBUG", json.toString());
                        res.putExtra("jsonResult", json.toString());
                        setResult(RESULT_OK, res);//RESULT_OK=200
                        // Dismiss
                        finish();
                    }

                    // FAILURE
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("DEBUG", statusCode+"");
                        setResult(RESULT_CANCELED, res);
                        // Dismiss
                        finish();
                    }
                }, etMsg.getText().toString());


            }
        });

        //Cancel
        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose_tweet, menu);
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
