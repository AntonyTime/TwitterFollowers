package com.antonytime.twitterfollowers.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.*;
import android.os.AsyncTask;

import com.antonytime.twitterfollowers.activitys.ProfileActivity;

import java.io.InputStream;
import java.net.URL;


public class LoadProfile extends AsyncTask<String, String, Bitmap> {

    private Context mContext;
    private ProgressDialog progress;
    private Bitmap bitmap;

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
        progress.setMessage("Loading Profile ...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
    }

    @Override
    protected Bitmap doInBackground(String... args) {
        try {
            bitmap = BitmapFactory.decodeStream((InputStream) new URL(GettingAccessToken.getImage_url()).getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap image) {
        Bitmap image_circle = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        BitmapShader shader = new BitmapShader (bitmap,  Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        Canvas c = new Canvas(image_circle);
        c.drawCircle(image.getWidth() / 2, image.getHeight() / 2, image.getWidth() / 2, paint);
        ProfileActivity.getProf_img().setImageBitmap(image_circle);
        ProfileActivity.getProf_name().setText(GettingAccessToken.getName());

        progress.hide();
        progress.dismiss();
    }
}
