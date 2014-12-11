package com.antonytime.twitterfollowers;

import android.app.*;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;


public class LoginActivity extends Activity {

    Button login;
    Twitter twitter;
    RequestToken requestToken;
    String oauth_url;
    String oauth_verifier;
    String profile_url;
    Dialog auth_dialog;
    WebView web;
    ProgressDialog progress;
    String token;
    String secretToken;
    String name;
    String image_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        login = (Button) findViewById(R.id.btnLogin);

        twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(Constant.getConsumerKey(), Constant.getConsumerSecret());
    }

    public void login(View view){
        new TokenGet().execute();
    }

    private class TokenGet extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {

            try {
                requestToken = twitter.getOAuthRequestToken();
                oauth_url = requestToken.getAuthorizationURL();
            } catch (TwitterException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return oauth_url;
        }

        @Override
        protected void onPostExecute(String oauth_url) {
            if(oauth_url != null){
                Log.e("URL", oauth_url);
                auth_dialog = new Dialog(LoginActivity.this);
                auth_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                auth_dialog.setContentView(R.layout.auth_dialog_fragment);
                web = (WebView)auth_dialog.findViewById(R.id.webview);
                web.getSettings().setJavaScriptEnabled(true);
                web.loadUrl(oauth_url);
                web.setWebViewClient(new WebViewClient() {
                    boolean authComplete = false;
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon){
                        super.onPageStarted(view, url, favicon);
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        if (url.contains("oauth_verifier") && authComplete == false){
                            authComplete = true;
                            Log.e("Url",url);
                            Uri uri = Uri.parse(url);
                            oauth_verifier = uri.getQueryParameter("oauth_verifier");

                            auth_dialog.dismiss();
                            new AccessTokenGet().execute();
                        }else if(url.contains("denied")){
                            auth_dialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Sorry !, Permission Denied", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                auth_dialog.show();
                auth_dialog.setCancelable(true);

            }else{
                Toast.makeText(LoginActivity.this, "Sorry !, Network Error or Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class AccessTokenGet extends AsyncTask<String, String, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(LoginActivity.this);
            progress.setMessage("Please wait ...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.show();
        }

        @Override
        protected Boolean doInBackground(String... args) {
            try {
                AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, oauth_verifier);
                token = accessToken.getToken();
                secretToken = accessToken.getTokenSecret();
                User user = twitter.showUser(accessToken.getUserId());
                profile_url = user.getOriginalProfileImageURL();
                name = user.getName();
                image_url = user.getOriginalProfileImageURL();

            } catch (TwitterException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return true;
        }
        @Override
        protected void onPostExecute(Boolean response) {
            if(response){
                progress.hide();
                Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("secretToken", secretToken);
                intent.putExtra("name", name);
                intent.putExtra("image_url", image_url);
                startActivity(intent);
            }
        }
    }
}


