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
import com.antonytime.twitterfollowers.asynctask.GettingUnfollowers;
import java.util.ArrayList;

public class UnfollowersActivity extends Activity  {

    public ListView listView;
    public TextView count;
    public FollowerAdapter adapter;
    public Cursor c = ProfileActivity.db.query("unfollowers", null, null, null, null, null, null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unfollowers_layout);

        listView = (ListView) findViewById(R.id.listView);
        count = (TextView) findViewById(R.id.count);

        count.setText("Number of unfollowers: " + c.getCount());

        adapter = new FollowerAdapter(this, initDataListView());
        listView.setAdapter(adapter);
    }

    private ArrayList<Follower> initDataListView(){
        ArrayList<Follower> unfollowers = new ArrayList<Follower>();

        while (c.moveToNext()) {
            unfollowers.add(new Follower(c.getLong(0), c.getString(1)));
        }

        count.setText("Number of unfollowers: " + c.getCount());

        return unfollowers;
    }

    public void onUpdateUnfollowers(View view) throws Exception {
        GettingUnfollowers gettingUnfollowers = new GettingUnfollowers();
        gettingUnfollowers.setContext(this);
        gettingUnfollowers.execute();
    }

}