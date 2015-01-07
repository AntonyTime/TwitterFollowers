package com.antonytime.twitterfollowers.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.Gravity;
import android.widget.Toast;
import com.antonytime.twitterfollowers.Follower;
import com.antonytime.twitterfollowers.activitys.ProfileActivity;
import twitter4j.IDs;
import twitter4j.ResponseList;
import twitter4j.TwitterException;
import twitter4j.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class GettingFollowers extends AsyncTask <Void, Void, ArrayList<Follower>> {

    private ProgressDialog progress;
    private Context mContext;
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
        progress.setMessage("Please wait ...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
    }

    @Override
    protected ArrayList<Follower> doInBackground(Void... params) {
        ResponseList<User> userFollowersListFromServer = null;

        try {
            long userID = GettingToken.getTwitter().getId();
            long cursor = -1;

            IDs ids = GettingToken.getTwitter().getFollowersIDs(userID, cursor);

            userFollowersListFromServer = GettingToken.getTwitter().lookupUsers(ids.getIDs());

        } catch (TwitterException e) {
            e.printStackTrace();
        }

        ArrayList<Follower> followersListFromServer = new ArrayList<Follower>();

        for (User user : userFollowersListFromServer) {
            Follower follower = new Follower();

            follower.setId(user.getId());
            follower.setName(user.getScreenName());

            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(user.getOriginalProfileImageURL()).getContent());
            } catch (IOException e) {
                e.printStackTrace();
            }

            saveBitmap(bitmap,user.getId());

            followersListFromServer.add(follower);
        }

        return followersListFromServer;
    }

    @Override
    protected void onPostExecute(ArrayList<Follower> result) {
        super.onPostExecute(result);

        try {
            saveToDB(result, "followers", getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        progress.dismiss();
    }

    public long saveBitmap(Bitmap bitmap,long imageId){
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/TwitterFollowers/saved_profiles_images");
        myDir.mkdirs();

        String fname = "Image-"+ imageId++ +".jpg";

        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return imageId;
    }

    public void saveToDB(ArrayList<Follower> dataList, String tableName, Context context) throws Exception {
        String deleteQuery = String.format("DELETE FROM %s", tableName);
        ProfileActivity.db.execSQL(deleteQuery);

        String query = String.format("INSERT INTO %s (id, name) VALUES(?,?)", tableName);
        SQLiteStatement statement = ProfileActivity.db.compileStatement(query);


        for (Follower follower : dataList)
        {
            statement.bindLong(1, follower.getId());
            statement.bindString(2, follower.getName());
            statement.execute();
        }

        Toast toast = Toast.makeText(context, "Done!", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}
