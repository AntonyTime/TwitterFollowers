package com.antonytime.twitterfollowers;

import android.app.*;
import android.content.Intent;
import android.graphics.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.*;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

import java.io.InputStream;
import java.net.URL;

public class ProfileActivity extends Activity {

    TextView prof_name;
    Bitmap bitmap;
    ImageView prof_img;
    Button unfollowers;
    Button postTweet;
    Button signout;
    Button postTweetDialog;
    EditText tweet_text;
    ProgressDialog progress;
    Dialog tDialog;
    String tweetText;
    String token;
    String secretToken;
    String name;
    String image_url;

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

        new LoadProfile().execute();
    }

    public void signOut(View view) {
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void showPostTweetDialog(View view) {
        // TODO Auto-generated method stub
        tDialog = new Dialog(ProfileActivity.this);
        tDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        tDialog.setContentView(R.layout.tweet_dialog_fragment);
        tweet_text = (EditText)tDialog.findViewById(R.id.tweet_text);
        postTweetDialog = (Button)tDialog.findViewById(R.id.btnPostTweetDialog);

        postTweetDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new PostTweet().execute();
            }
        });

        tDialog.show();
    }

    private class PostTweet extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(ProfileActivity.this);
            progress.setMessage("Posting tweet ...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            tweetText = tweet_text.getText().toString();
            progress.show();
        }

        protected String doInBackground(String... args) {
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(Constant.getConsumerKey());
            builder.setOAuthConsumerSecret(Constant.getConsumerSecret());

            AccessToken accessToken = new AccessToken(token, secretToken);
            Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);

            try {
                twitter4j.Status response = twitter.updateStatus(tweetText);
                return response.toString();
            } catch (TwitterException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String res) {
            if(res != null){
                progress.dismiss();
                Toast.makeText(ProfileActivity.this, "Tweet Sucessfully Posted", Toast.LENGTH_SHORT).show();
                tDialog.dismiss();
            }else{
                progress.dismiss();
                Toast.makeText(ProfileActivity.this, "Error while tweeting !", Toast.LENGTH_SHORT).show();
                tDialog.dismiss();
            }
        }
    }

    private class LoadProfile extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(ProfileActivity.this);
            progress.setMessage("Loading Profile ...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.show();
        }

        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(image_url).getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {
            Bitmap image_circle = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

            BitmapShader shader = new BitmapShader (bitmap,  Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            Paint paint = new Paint();
            paint.setShader(shader);
            Canvas c = new Canvas(image_circle);
            c.drawCircle(image.getWidth()/2, image.getHeight()/2, image.getWidth()/2, paint);
            prof_img.setImageBitmap(image_circle);
            prof_name.setText(name);

            progress.hide();

        }
    }
}
