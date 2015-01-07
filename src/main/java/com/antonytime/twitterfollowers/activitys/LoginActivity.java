package com.antonytime.twitterfollowers.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.antonytime.twitterfollowers.R;
import com.antonytime.twitterfollowers.asynctask.GettingToken;

public class LoginActivity extends Activity {

    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        login = (Button) findViewById(R.id.btnLogin);
    }

    public void login(View view){
        GettingToken gettingToken = new GettingToken();
        gettingToken.setContext(this);
        gettingToken.execute();
    }

}