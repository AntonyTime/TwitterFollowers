package com.antonytime.twitterfollowers.activitys;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.antonytime.twitterfollowers.Follower;
import com.antonytime.twitterfollowers.R;
import com.antonytime.twitterfollowers.adapter.FollowerAdapter;
import com.antonytime.twitterfollowers.asynctask.GettingFollowers;

import java.util.ArrayList;

public class FollowersActivity extends Activity {

    private ListView listView;
    private TextView count;
    private FollowerAdapter adapter;
    private Cursor c = ProfileActivity.db.query("followers", null, null, null, null, null, null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.followers_layout);

        listView = (ListView) findViewById(R.id.listView);
        count = (TextView) findViewById(R.id.count);

        count.setText("Number of followers: " + c.getCount());

        adapter = new FollowerAdapter(this, initDataListView());
        listView.setAdapter(adapter);

    }

    private ArrayList<Follower> initDataListView() {
        ArrayList<Follower> followers = new ArrayList<Follower>();

        while (c.moveToNext()) {
            followers.add(new Follower(c.getLong(0), c.getString(1)));
        }

        count.setText("Number of followers: " + c.getCount());

        return followers;
    }

    public void onUpdateFollowers(View view) throws Exception {
        GettingFollowers gettingFollowers = new GettingFollowers();
        gettingFollowers.setContext(this);
        gettingFollowers.execute();
    }
}