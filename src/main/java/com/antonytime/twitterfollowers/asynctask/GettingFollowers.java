package com.antonytime.twitterfollowers.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.Toast;
import com.antonytime.twitterfollowers.Follower;
import com.antonytime.twitterfollowers.activitys.ProfileActivity;
import twitter4j.IDs;
import twitter4j.ResponseList;
import twitter4j.TwitterException;
import twitter4j.User;

import java.util.ArrayList;

public class GettingFollowers extends AsyncTask <Void, Void, ResponseList<User>> {

    private ProgressDialog progress;
    private Context mContext;

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
    protected ResponseList<User> doInBackground(Void... params) {
        ResponseList<User> followersListFromServer = null;

        try {
            long userID = GettingToken.getTwitter().getId();
            long cursor = -1;

            IDs ids = GettingToken.getTwitter().getFollowersIDs(userID, cursor);

            followersListFromServer = GettingToken.getTwitter().lookupUsers(ids.getIDs());
        } catch (TwitterException e) {
            e.printStackTrace();
        }

        return followersListFromServer;
    }

    @Override
    protected void onPostExecute(ResponseList<User> result) {
        super.onPostExecute(result);

        ArrayList<Follower> followersListFromServer = new ArrayList<Follower>();

        for (User user : result) {
            Follower follower = new Follower();

            follower.setId(user.getId());
            follower.setName(user.getScreenName());

            followersListFromServer.add(follower);
        }

        try {
            saveToDB(followersListFromServer, "followers", getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        progress.dismiss();
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
