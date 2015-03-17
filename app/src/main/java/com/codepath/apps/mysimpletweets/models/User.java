package com.codepath.apps.mysimpletweets.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by long on 3/8/15.
 */

/*
    "user": {
      "name": "OAuth Dancer",
      "profile_sidebar_fill_color": "DDEEF6",
      "profile_background_tile": true,
      "profile_sidebar_border_color": "C0DEED",
      "profile_image_url": "http://a0.twimg.com/profile_images/730275945/oauth-dancer_normal.jpg",
      },
* */
public class User  implements Serializable {
    private static final long serialVersionUID = -2893089570992474768L;
    // list the attributes
    private String name;
    private long uid;
    private String screenName;
    private String profileImageUrl;
    private int tweetsCount;
    private String tagline;
    private int followersCount;
    private String profileBackgroundImageUrl;
    private String profileBackgroundColor;
    private int followingCount;

    public int getTweetsCount() {
        return tweetsCount;
    }

    public String getTagline() {
        return tagline;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getProfileBackgroundImageUrl() {
        return profileBackgroundImageUrl;
    }

    public String getProfileBackgroundColor() {
        return profileBackgroundColor;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    // deserialize the user json => User object
    public static User fromJSON(JSONObject json){
        // Extract and fill the values to User
        User user = new User();

        try {
            user.name = json.getString("name");
            user.uid = json.getLong("id");
            user.screenName = json.getString("screen_name");
            user.profileImageUrl = json.getString("profile_image_url");
            user.tagline = json.getString("description");
            user.followersCount = json.getInt("followers_count");
            user.followingCount = json.getInt("friends_count");
            user.profileBackgroundImageUrl = json.getString("profile_background_image_url");
            user.profileBackgroundColor = json.getString("profile_background_color");
            user.tweetsCount = json.getInt("statuses_count");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Return a user
        return user;
    }
}
