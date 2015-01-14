package com.antonytime.twitterfollowers.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.antonytime.twitterfollowers.R;
import com.antonytime.twitterfollowers.asynctask.GettingFollowers;

public class FollowersActivity extends Activity {

    private ListView listView;
    private TextView count;
    private GettingFollowers gettingFollowers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.followers_layout);

        listView = (ListView) findViewById(R.id.listView);
        count = (TextView) findViewById(R.id.countFollowers);

        gettingFollowers = new GettingFollowers(this, listView, count);
        gettingFollowers.updateListView();
    }

    public void onUpdateFollowers(View view) throws Exception {
        gettingFollowers.execute();
    }

}