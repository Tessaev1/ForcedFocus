package edu.uw.tessa.forcedfocus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.facebook.AccessToken;

public class MainActivity extends AppCompatActivity {
    private AccessToken userToken = AccessToken.getCurrentAccessToken();

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        final TextView currentUser = (TextView) findViewById(R.id.currentUser);
        currentUser.setText("Current user id: " + this.userToken.getUserId());
    }

}
