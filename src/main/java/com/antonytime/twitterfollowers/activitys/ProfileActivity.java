package com.antonytime.twitterfollowers.activitys;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.antonytime.twitterfollowers.DBHelper;
import com.antonytime.twitterfollowers.R;
import com.antonytime.twitterfollowers.asynctask.GettingAccessToken;
import com.antonytime.twitterfollowers.asynctask.LoadProfile;
import com.antonytime.twitterfollowers.asynctask.PostTweet;

public class ProfileActivity extends Activity {

    private static TextView prof_name;
    private static ImageView prof_img;
    private static EditText tweet_text;
    private static Dialog tDialog;
    private Button unfollowers;
    private Button postTweet;
    private Button signout;
    private Button postTweetDialog;
    private String token;
    private String secretToken;
    private String name;
    private String image_url;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;

    public static TextView getProf_name() {
        return prof_name;
    }

    public static ImageView getProf_img() {
        return prof_img;
    }

    public static EditText getTweet_text() {
        return tweet_text;
    }

    public ProfileActivity self(){
        return this;
    }

    public static Dialog gettDialog() {
        return tDialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);

        prof_name = (TextView) findViewById(R.id.tvProfileName);
        prof_img = (ImageView) findViewById(R.id.imageView);
        unfollowers = (Button) findViewById(R.id.btnUnfollowers);
        postTweet = (Button) findViewById(R.id.btnPostTweet);
        signout = (Button) findViewById(R.id.btnLogout);

        token = getIntent().getStringExtra("token");
        secretToken = getIntent().getStringExtra("secretToken");
        name = getIntent().getStringExtra("name");
        image_url = getIntent().getStringExtra("image_url");

        LoadProfile loadProfile = new LoadProfile();
        loadProfile.setContext(this);
        loadProfile.execute();

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
    }

    public void onFollowers(View view){
        Intent intent = new Intent(ProfileActivity.this, FollowersActivity.class);
        startActivity(intent);
    }

    public void onUnfollowers(View view){
        Intent intent = new Intent(ProfileActivity.this, UnfollowersActivity.class);
        startActivity(intent);
    }

    public void onPostTweetDialog(View view) {
        tDialog = new Dialog(ProfileActivity.this);
        tDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        tDialog.setContentView(R.layout.tweet_dialog_fragment);
        tweet_text = (EditText)tDialog.findViewById(R.id.tweet_text);
        postTweetDialog = (Button)tDialog.findViewById(R.id.btnPostTweetDialog);

        postTweetDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostTweet postTweet = new PostTweet();
                postTweet.setContext(self());
                postTweet.execute();
            }
        });

        tDialog.show();
    }

    public void onSignOut(View view) {
        GettingAccessToken.accessToken = null;
        GettingAccessToken.secretToken = null;

        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
