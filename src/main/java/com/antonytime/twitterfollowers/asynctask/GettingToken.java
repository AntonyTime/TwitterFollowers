package com.antonytime.twitterfollowers.asynctask;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.antonytime.twitterfollowers.R;
import com.antonytime.twitterfollowers.SharedData;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class GettingToken extends AsyncTask<String, String, String> {

    private static Twitter twitter = new TwitterFactory().getInstance();
    private RequestToken requestToken;
    private String oauth_url;
    private String oauth_verifier;
    private Dialog auth_dialog;
    private Context mContext;
    private ProgressDialog progress;

    public RequestToken getRequestToken() {
        return requestToken;
    }

    public String getoauthVerifier() {
        return oauth_verifier;
    }

    public static Twitter getTwitter() {
        return twitter;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public GettingToken self(){
        return this;
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
    protected String doInBackground(String... strings) {
        try {
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(SharedData.getConsumerKey());
            builder.setOAuthConsumerSecret(SharedData.getConsumerSecret());
            Configuration configuration = builder.build();
            TwitterFactory factory = new TwitterFactory(configuration);
            twitter = factory.getInstance();

            requestToken = twitter.getOAuthRequestToken();
            oauth_url = requestToken.getAuthorizationURL();
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return oauth_url;
    }

    @Override
    protected void onPostExecute(String oauth_url) {
        if(oauth_url != null){
            Log.e("URL", oauth_url);
            auth_dialog = new Dialog(getContext());
            auth_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            auth_dialog.setContentView(R.layout.auth_dialog_fragment);

            WebView web = (WebView) auth_dialog.findViewById(R.id.webview);
            web.getSettings().setJavaScriptEnabled(true);
            web.loadUrl(oauth_url);
            web.setWebViewClient(new WebViewClient() {
                boolean authComplete = false;

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    if (url.contains("oauth_verifier") && authComplete == false) {
                        authComplete = true;
                        Log.e("Url", url);
                        Uri uri = Uri.parse(url);
                        oauth_verifier = uri.getQueryParameter("oauth_verifier");

                        auth_dialog.dismiss();

                        GettingAccessToken gettingAccessToken = new GettingAccessToken();
                        gettingAccessToken.setContext(getContext());
                        gettingAccessToken.setGettingToken(self());
                        gettingAccessToken.execute();

                    } else if (url.contains("denied")) {
                        auth_dialog.dismiss();
                        Toast.makeText(getContext(), "Sorry !, Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            progress.dismiss();
            auth_dialog.show();
            auth_dialog.setCancelable(true);
        }else{
            SystemClock.sleep(1000);
            progress.dismiss();
            Toast.makeText(getContext(), "Sorry !, Network Error or Invalid Credentials", Toast.LENGTH_SHORT).show();
        }
    }
}
