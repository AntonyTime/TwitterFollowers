package com.antonytime.twitterfollowers.activitys;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.antonytime.twitterfollowers.DBHelper;
import com.antonytime.twitterfollowers.R;
import com.antonytime.twitterfollowers.adapter.UnfollowersAdapter;
import com.antonytime.twitterfollowers.asynctask.GettingName;
import com.antonytime.twitterfollowers.pojo.Followers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FollowersActivity extends Activity {

    ListView listView;
    private DBHelper dbHelper;
    public static SQLiteDatabase db;
    public static long id;
    UnfollowersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.followers_layout);

        listView = (ListView) findViewById(R.id.listView);

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

//        try {
//            adapter = new UnfollowersAdapter(this, initListData());
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        listView.setAdapter(adapter);

    }

    private List<Followers> initListData() throws ExecutionException, InterruptedException {
        Cursor c = db.query("followers", null, null, null, null, null, null);
        List<Followers> list = new ArrayList<Followers>();

        if(c.getCount() == 0){
            list.add(new Followers("Please click update followers"));
        } else {
            while (c.moveToFirst()) {
                list.add(new Followers(new GettingName().execute().get()));
            }
        }

        return list;
    }

    public void onUpdateFollowers(View view) throws ExecutionException, InterruptedException {

//        adapter = new UnfollowersAdapter(this, initListData());
//        listView.setAdapter(adapter);
    }

}
