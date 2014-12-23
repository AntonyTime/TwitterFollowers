package com.antonytime.twitterfollowers.activitys;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.antonytime.twitterfollowers.DBHelper;
import com.antonytime.twitterfollowers.R;
import com.antonytime.twitterfollowers.adapter.UnfollowersAdapter;
import com.antonytime.twitterfollowers.asynctask.GettingFollowersList;
import com.antonytime.twitterfollowers.asynctask.GettingID;
import com.antonytime.twitterfollowers.asynctask.GettingName;
import com.antonytime.twitterfollowers.pojo.Followers;
import twitter4j.IDs;
import twitter4j.TwitterException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class UnfollowersActivity extends Activity  {

    ListView listView;
    private DBHelper dbHelper;
    public static final String FOLLOWERS_TABLE = "followers";
    public static final String UNFOLLOWERS_TABLE = "unfollowers";
    public static SQLiteDatabase db;
    public static long id;
    UnfollowersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unfollowers_layout);

        listView = (ListView) findViewById(R.id.listView);

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        try {
            adapter = new UnfollowersAdapter(this, initListData());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        listView.setAdapter(adapter);
    }

    private List<Followers> initListData() throws ExecutionException, InterruptedException {
        Cursor c = db.query("unfollowers", null, null, null, null, null, null);
        List<Followers> list = new ArrayList<Followers>();

        if(c.getCount() == 0){
            list.add(new Followers("No unfollowers"));
        } else {
            for (int i = 0; c.moveToNext(); i++) {
                id = c.getLong(i);
                list.add(new Followers(new GettingName().execute().get()));
            }
        }

        return list;
    }

    public void onUpdateUnfollowers(View view) throws ExecutionException, InterruptedException {
        try {
            List<Long> followersListFromDataBase = getFollowersFromDB();
            List<Long> followersListFromServer = getFollowersFromServer();

            findUnfollowers(followersListFromDataBase, followersListFromServer);

            saveToDB(followersListFromServer, FOLLOWERS_TABLE);
            saveToDB(followersListFromDataBase, UNFOLLOWERS_TABLE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        GettingFollowersList gettingFollowersList = new GettingFollowersList();
        gettingFollowersList.execute();

        adapter = new UnfollowersAdapter(this, initListData());
        listView.setAdapter(adapter);
    }

    public IDs getFollewersIDs() throws TwitterException, ExecutionException, InterruptedException {

        GettingID loadList = new GettingID();
        loadList.execute();

        return loadList.get();
    }

    public List<Long> getFollowersFromServer() throws Exception {
        IDs followersIDs = getFollewersIDs();
        List<Long> followersListFromServer = new ArrayList<Long>();

        for (long i : followersIDs.getIDs())
        {
            followersListFromServer.add(i);
        }

        return followersListFromServer;
    }

    public List<Long> getFollowersFromDB() throws SQLException, ClassNotFoundException {
        Cursor c = db.query("followers", null, null, null, null, null, null);

        List<Long> followersListFromDataBase = new ArrayList<Long>();

        while (c.moveToNext()) {
            followersListFromDataBase.add(c.getLong(0));
        }

        return followersListFromDataBase;
    }

    public void findUnfollowers(List<Long> followersListFromDataBase, List<Long> followersListFromServer) throws Exception {
        followersListFromDataBase.removeAll(followersListFromServer);
    }

    public void saveToDB(List<Long> dataList, String tableName) throws Exception {
        String deleteQuery = String.format("DELETE FROM UNFOLLOWERS");
        db.execSQL(deleteQuery);

        String query = String.format("replace INTO %s VALUES(?)", tableName);
        SQLiteStatement stmt = db.compileStatement(query);

        for (long i : dataList)
        {
            stmt.bindLong(1, i);
            stmt.execute();
        }

        Toast toast = Toast.makeText(this, "Done!", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}
