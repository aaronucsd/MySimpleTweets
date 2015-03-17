package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.User;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.NumberFormat;

/**
 * Created by long on 3/16/15.
 */
public class UserProfileFragment extends Fragment {

    public static UserProfileFragment newInstance(User user) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_profile, container, false);
        setUserProfileView(v);
        return v;
    }

    private void setUserProfileView(View v) {
        User user = (User) getArguments().getSerializable("user");

        TextView tvTagline = (TextView) v.findViewById(R.id.tvTagline);
        TextView tvScreenName = (TextView) v.findViewById(R.id.tvScreenName);
        TextView tvNumTweets = (TextView) v.findViewById(R.id.tvNumTweets);
        TextView tvNumFollowers = (TextView) v.findViewById(R.id.tvNumFollowers);
        TextView tvNumFollowing = (TextView) v.findViewById(R.id.tvNumFollowing);
        ImageView ivProfileImage = (ImageView) v.findViewById(R.id.ivProfileImage);


        if (user == null){
            return ;
        }
        NumberFormat nf = NumberFormat.getInstance();

        tvTagline.setText(user.getTagline());
        tvScreenName.setText("@" + user.getScreenName());
        tvNumTweets.setText("" + user.getTweetsCount());

        tvNumFollowers.setText(nf.format(user.getFollowersCount()));
        tvNumFollowing.setText(nf.format(user.getFollowingCount()));

        Transformation transformation = new RoundedTransformationBuilder()
                .borderWidthDp(0)
                .cornerRadiusDp(5)
                .oval(false)
                .build();


        Picasso.with(getActivity().getApplicationContext())
                .load(user.getProfileImageUrl())
                .resize(80, 80)
                .transform(transformation)
                .into(ivProfileImage);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
