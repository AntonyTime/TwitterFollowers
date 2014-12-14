package com.antonytime.twitterfollowers.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import twitter4j.IDs;
import twitter4j.TwitterException;

public class GettingID extends AsyncTask <Void, Void, IDs> {

    public GettingID() {
        super();
    }

    @Override
    protected IDs doInBackground(Void... voids) {
        long cursor = -1;
        IDs id = null;

        try {
            long userID = GettingToken.getTwitter().getId();
            id = GettingToken.getTwitter().getFollowersIDs(userID, cursor);
        } catch (TwitterException e) {
            e.printStackTrace();
        }

        return id;
    }

}
