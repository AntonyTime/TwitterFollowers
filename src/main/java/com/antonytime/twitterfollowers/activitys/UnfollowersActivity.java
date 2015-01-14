package com.antonytime.twitterfollowers.activitys;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.antonytime.twitterfollowers.R;
import com.antonytime.twitterfollowers.asynctask.GettingFollowers;
import com.antonytime.twitterfollowers.asynctask.GettingUnfollowers;

public class UnfollowersActivity extends Activity {

    private ListView listView;
    private TextView count;
    private GettingUnfollowers gettingUnfollowers;

    private Cursor c = ProfileActivity.db.query("followers", null, null, null, null, null, null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unfollowers_layout);

        listView = (ListView) findViewById(R.id.listView);
        count = (TextView) findViewById(R.id.countUnfollowers);

        gettingUnfollowers = new GettingUnfollowers(this,listView,count);
        gettingUnfollowers.updateListView();
    }

    public void onUpdateUnfollowers(View view) throws Exception {
        if(c.getCount() == 0){
            GettingFollowers gettingFollowers = new GettingFollowers(this,listView,count);
            gettingFollowers.execute();
        }else {
            gettingUnfollowers.execute();
        }
    }

}