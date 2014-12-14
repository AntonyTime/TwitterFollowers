package com.antonytime.twitterfollowers.activitys;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.antonytime.twitterfollowers.R;
import com.antonytime.twitterfollowers.adapter.UnfollowersAdapter;
import com.antonytime.twitterfollowers.asynctask.GettingID;
import com.antonytime.twitterfollowers.asynctask.GettingName;
import com.antonytime.twitterfollowers.pojo.Unfollowers;
import twitter4j.IDs;
import twitter4j.TwitterException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ListActivity extends Activity  {

    ListView listView;
    final String LOG_TAG = "myLogs";
    private DBHelper dbHelper;
    public static final String FOLLOWERS_TABLE = "followers";
    public static final String UNFOLLOWERS_TABLE = "unfollowers";
    public static SQLiteDatabase db;
    public static long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);

        listView = (ListView) findViewById(R.id.listView);

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

    }

    private List<Unfollowers> initListData() throws ExecutionException, InterruptedException {
        Cursor c = db.query("unfollowers", null, null, null, null, null, null);

        List<Unfollowers> list = new ArrayList<Unfollowers>();

        for (int i = 0; c.moveToNext(); i++) {
            id = c.getLong(i);
            list.add(new Unfollowers(new GettingName().execute().get()));
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

        UnfollowersAdapter adapter = new UnfollowersAdapter(this, initListData());

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
        String deleteQuery = String.format("DELETE FROM %s", tableName);
        db.execSQL(deleteQuery);

        String query = String.format("INSERT INTO %s VALUES(?)", tableName);
        SQLiteStatement stmt = db.compileStatement(query);

        for (long i : dataList)
        {
            stmt.bindLong(1, i);
            stmt.execute();
        }

        Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
    }

    public class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            // конструктор суперкласса
            super(context, "DataBase", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "--- onCreate database ---");
            // создаем таблицу с полями
            db.execSQL("CREATE TABLE if not exists 'followers' ('id' INTEGER PRIMARY KEY NOT NULL );");
            db.execSQL("CREATE TABLE if not exists 'unfollowers' ('id' INTEGER PRIMARY KEY NOT NULL );");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
