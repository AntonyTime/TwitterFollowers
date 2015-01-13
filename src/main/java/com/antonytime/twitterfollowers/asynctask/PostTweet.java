package com.antonytime.twitterfollowers.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import com.antonytime.twitterfollowers.activitys.ProfileActivity;
import twitter4j.TwitterException;

public class PostTweet extends AsyncTask<String, String, String> {

    private Context context;
    private ProgressDialog progress;
    private String tweetText;

    public PostTweet(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(context);
        progress.setMessage("Posting tweet ...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        tweetText = ProfileActivity.getTweet_text().getText().toString();
        progress.show();
    }

    @Override
    protected String doInBackground(String... args) {
        try {
            twitter4j.Status response = GettingToken.getTwitter().updateStatus(tweetText);
            return response.toString();
        } catch (TwitterException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String res) {
        if (res != null) {
            progress.dismiss();
            Toast.makeText(context, "Tweet Sucessfully Posted", Toast.LENGTH_SHORT).show();
            ProfileActivity.getDialog().dismiss();
        } else {
            progress.dismiss();
            Toast.makeText(context, "Error while tweeting!", Toast.LENGTH_SHORT).show();
            ProfileActivity.getDialog().dismiss();
        }
    }
}
