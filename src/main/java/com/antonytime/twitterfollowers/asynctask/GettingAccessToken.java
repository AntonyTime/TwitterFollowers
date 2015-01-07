package com.antonytime.twitterfollowers.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import com.antonytime.twitterfollowers.activitys.LoginActivity;
import com.antonytime.twitterfollowers.activitys.ProfileActivity;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.auth.AccessToken;

public class GettingAccessToken extends AsyncTask<String, String, Boolean> {

    private static String token;
    public static AccessToken accessToken;
    public static String secretToken;
    private static String name;
    private static String image_url;
    private ProgressDialog progress;
    private Context mContext;
    private String profile_url;

    GettingToken gettingToken = new GettingToken();
    LoginActivity loginActivity = new LoginActivity();

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public void setGettingToken(GettingToken gettingToken) {
        this.gettingToken = gettingToken;
    }

    public static String getName() {
        return name;
    }

    public static String getImage_url() {
        return image_url;
    }

    public static String getToken() {
        return token;
    }

    public static String getSecretToken() {
        return secretToken;
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
            token = accessToken.getToken();
            secretToken = accessToken.getTokenSecret();
            User user = GettingToken.getTwitter().showUser(accessToken.getUserId());
            profile_url = user.getOriginalProfileImageURL();
            name = user.getName();
            image_url = user.getOriginalProfileImageURL();
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
            intent.putExtra("token", token);
            intent.putExtra("secretToken", secretToken);
            intent.putExtra("name", name);
            intent.putExtra("image_url", image_url);
            getContext().startActivity(intent);
            loginActivity.finish();
        }
    }
}
