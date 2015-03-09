package com.codepath.apps.mysimpletweets.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by long on 3/8/15.
 */
    /*
   [

     {
         "text": "just another test",
    "contributors": null,
    "id": 240558470661799936,
    "retweet_count": 0,
    "in_reply_to_status_id_str": null,
    "geo": null,
    "retweeted": false,
    "in_reply_to_user_id": null,
    "place": null,
    "source": "<a href="//realitytechnicians.com\"" rel="\"nofollow\"">OAuth Dancer Reborn</a>",
    "user": {
      "name": "OAuth Dancer",
      "profile_sidebar_fill_color": "DDEEF6",
      "profile_background_tile": true,
      "profile_sidebar_border_color": "C0DEED",
      "profile_image_url": "http://a0.twimg.com/profile_images/730275945/oauth-dancer_normal.jpg",
      },
      {
      ...
      }

      ]
    * */

// Parse (Deserialize and turn into POJO) the json + store the data, and encapsulate state of the logic or display logic
 public class Tweet {
    // list out the attributes
    private String body;
    private long uid;//unique id for each tweet - DB's id
    private User user;
    private String createAt;


    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreateAt() {
        return createAt;
    }

    public User getUser() {
        return user;
    }

    // Deserialize the JSON and build tweet objects
    // Tweet.fromJSON("{...}"} => <TWEET>
    public  static Tweet fromJSON(JSONObject jsonObject){
        Tweet tweet = new Tweet();
        // Extract the values from the json, and store them
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createAt = jsonObject.getString("created_at");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //return the tweet object
        return tweet;
    }

    //Return an arraylist of <Tweets>
    // Tweet.fromJSONArray([{...}, {...}]) => List<Tweet>
    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray){
        ArrayList<Tweet> tweets = new ArrayList<>();
        // Iterate the raw json array and create tweets
        for(int i = 0; i<jsonArray.length(); i++){
            try {
                JSONObject tweetJson = jsonArray.getJSONObject(i);//getting each of the JSONObject
                Tweet tweet = Tweet.fromJSON(tweetJson);// create the tweet (POJO) form the JSONObject
                if(tweet != null){
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;//if one fail, continue to next and others
            }

        }
        return tweets;//finished list
    }
}
