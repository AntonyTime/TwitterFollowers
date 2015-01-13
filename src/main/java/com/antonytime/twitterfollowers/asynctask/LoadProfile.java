package com.antonytime.twitterfollowers.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.*;
import android.os.AsyncTask;
import com.antonytime.twitterfollowers.activitys.ProfileActivity;
import twitter4j.User;

import java.io.InputStream;
import java.net.URL;

public class LoadProfile extends AsyncTask<String, String, Bitmap> {

    private Context context;
    private ProgressDialog progress;
    private Bitmap bitmap;
    private String name;

    public LoadProfile(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(context);
        progress.setMessage("Loading Profile ...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
    }

    @Override
    protected Bitmap doInBackground(String... args) {
        try {
            User user = GettingToken.getTwitter().showUser(GettingAccessToken.accessToken.getUserId());
            name = user.getName();
            bitmap = BitmapFactory.decodeStream((InputStream) new URL(user.getOriginalProfileImageURL()).getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap image) {
        Bitmap image_circle = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        Canvas c = new Canvas(image_circle);
        c.drawCircle(image.getWidth() / 2, image.getHeight() / 2, image.getWidth() / 2, paint);
        ProfileActivity.getProf_img().setImageBitmap(image_circle);
        ProfileActivity.getProf_name().setText(name);

        progress.dismiss();
    }
}
