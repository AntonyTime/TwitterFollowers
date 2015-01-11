package com.antonytime.twitterfollowers.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import com.antonytime.twitterfollowers.activitys.ProfileActivity;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;

public class GettingAccessToken extends AsyncTask<String, String, Boolean> {

    public static AccessToken accessToken;
    private ProgressDialog progress;
    private Context mContext;

    GettingToken gettingToken = new GettingToken();

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public void setGettingToken(GettingToken gettingToken) {
        this.gettingToken = gettingToken;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(getContext());
        progress.setMessage("Please wait ...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            accessToken = GettingToken.getTwitter().getOAuthAccessToken(gettingToken.getRequestToken(), gettingToken.getoauthVerifier());
        } catch (TwitterException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean response) {
        if(response){
            progress.dismiss();

            Intent intent = new Intent(getContext(), ProfileActivity.class);
            getContext().startActivity(intent);
        }
    }
}
