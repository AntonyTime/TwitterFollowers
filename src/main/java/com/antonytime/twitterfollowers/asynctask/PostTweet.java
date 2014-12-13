package com.antonytime.twitterfollowers.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import com.antonytime.twitterfollowers.SharedData;
import com.antonytime.twitterfollowers.activitys.ProfileActivity;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

public class PostTweet extends AsyncTask<String, String, String> {

    private Context mContext;
    private ProgressDialog progress;
    private String tweetText;

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(getContext());
        progress.setMessage("Posting tweet ...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        tweetText = ProfileActivity.getTweet_text().getText().toString();
        progress.show();
    }

    @Override
    protected String doInBackground(String... args) {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(SharedData.getConsumerKey());
        builder.setOAuthConsumerSecret(SharedData.getConsumerSecret());

        AccessToken accessToken = new AccessToken(GettingAccessToken.getToken(), GettingAccessToken.getSecretToken());
        Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);

        try {
            twitter4j.Status response = twitter.updateStatus(tweetText);
            return response.toString();
        } catch (TwitterException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String res) {
        if(res != null){
            progress.dismiss();
            Toast.makeText(getContext(), "Tweet Sucessfully Posted", Toast.LENGTH_SHORT).show();
            ProfileActivity.gettDialog().dismiss();
        }else{
            progress.dismiss();
            Toast.makeText(getContext(), "Error while tweeting !", Toast.LENGTH_SHORT).show();
            ProfileActivity.gettDialog().dismiss();
        }
    }
}
