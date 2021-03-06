package com.antonytime.twitterfollowers.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.antonytime.twitterfollowers.Follower;
import com.antonytime.twitterfollowers.activitys.ProfileActivity;
import com.antonytime.twitterfollowers.adapter.FollowerAdapter;
import twitter4j.IDs;
import twitter4j.ResponseList;
import twitter4j.TwitterException;
import twitter4j.User;

import java.sql.SQLException;
import java.util.ArrayList;

public class GettingUnfollowers extends AsyncTask<Void, Void, ResponseList<User>> {

    private final ListView listView;
    private final TextView count;
    private final String FOLLOWERS_TABLE = "followers";
    private final String UNFOLLOWERS_TABLE = "unfollowers";
    private final Context context;
    private ProgressDialog progress;
    private FollowerAdapter adapter;

    public GettingUnfollowers(Context context, ListView listView, TextView count){
        this.context = context;
        this.listView = listView;
        this.count = count;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(context);
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
            ArrayList<Follower> followersListFromDataBase = getFollowersFromDB();

            saveToDB(followersListFromServer, FOLLOWERS_TABLE, context);
            saveToDB(findUnfollowers(followersListFromDataBase, followersListFromServer), UNFOLLOWERS_TABLE, context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateListView();

        progress.dismiss();
    }

    public ArrayList getFollowersFromDB() throws SQLException, ClassNotFoundException {
        Cursor c = ProfileActivity.db.query("followers", null, null, null, null, null, null);

        ArrayList<Follower> followersListFromDataBase = new ArrayList<Follower>();

        while (c.moveToNext()) {
            Follower follower = new Follower();

            follower.setId(c.getLong(0));
            follower.setName(c.getString(1));

            followersListFromDataBase.add(follower);
        }

        return followersListFromDataBase;
    }

    public ArrayList<Follower> findUnfollowers(ArrayList<Follower> followersListFromDataBase, ArrayList<Follower> followersListFromServer) throws Exception {
        ArrayList<Follower> result = new ArrayList<Follower>();

        ArrayList<Long> db = new ArrayList<Long>();
        ArrayList<Long> sr = new ArrayList<Long>();

        for (Follower follower : followersListFromDataBase) {
            long longId = follower.getId();
            db.add(longId);
        }
        for (Follower follower : followersListFromServer) {
            long longId = follower.getId();
            sr.add(longId);
        }

        db.removeAll(sr);

        for (Long ld : db) {
            for (Follower follower : followersListFromDataBase) {
                if (ld.equals(follower.getId())) {
                    result.add(follower);
                }
            }
        }

        return result;
    }

    public void saveToDB(ArrayList<Follower> dataList, String tableName, Context context) throws Exception {
        String deleteQuery = String.format("DELETE FROM %s", tableName);
        ProfileActivity.db.execSQL(deleteQuery);

        String query = String.format("INSERT INTO %s (id, name) VALUES(?,?)", tableName);
        SQLiteStatement statement = ProfileActivity.db.compileStatement(query);


        for (Follower follower : dataList) {
            statement.bindLong(1, follower.getId());
            statement.bindString(2, follower.getName());
            statement.execute();
        }

        Toast toast = Toast.makeText(context, "Done!", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void updateListView(){
        Cursor c = ProfileActivity.db.query("unfollowers", null, null, null, null, null, null);

        ArrayList<Follower> followers = new ArrayList<Follower>();

        while (c.moveToNext()) {
            followers.add(new Follower(c.getLong(0), c.getString(1)));
        }

        count.setText("" + c.getCount());

        adapter = new FollowerAdapter(context, followers);
        listView.setAdapter(adapter);
    }

}