package com.antonytime.twitterfollowers.asynctask;

import android.os.AsyncTask;
import com.antonytime.twitterfollowers.activitys.ListActivity;
import twitter4j.TwitterException;

public class GettingName extends AsyncTask <Void, Void, String> {

    public GettingName() {
        super();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String name = null;

        try {
            name = GettingToken.getTwitter().showUser(ListActivity.id).getName();
        } catch (TwitterException e) {
            e.printStackTrace();
        }

        return name;
    }
}