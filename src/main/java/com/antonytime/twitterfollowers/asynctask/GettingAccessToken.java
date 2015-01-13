package com.antonytime.twitterfollowers.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import com.antonytime.twitterfollowers.activitys.ProfileActivity;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class GettingAccessToken extends AsyncTask<String, String, Boolean> {

    public static AccessToken accessToken;
    private ProgressDialog progress;
    private RequestToken requestToken;
    private Activity activity;
    private String oauth_verifier;

    public GettingAccessToken(Activity activity) {
        this.activity = activity;
    }

    public void setRequestToken(RequestToken requestToken) {
        this.requestToken = requestToken;
    }

    public void setOauth_verifier(String oauth_verifier) {
        this.oauth_verifier = oauth_verifier;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(activity);
        progress.setMessage("Please wait ...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            accessToken = GettingToken.getTwitter().getOAuthAccessToken(requestToken, oauth_verifier);
        } catch (TwitterException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean response) {
        if (response) {
            progress.dismiss();

            Intent intent = new Intent(activity, ProfileActivity.class);
            activity.startActivity(intent);
            activity.finish();
        }
    }
}
