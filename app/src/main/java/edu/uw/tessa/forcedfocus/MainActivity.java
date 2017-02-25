package edu.uw.tessa.forcedfocus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;

public class MainActivity extends AppCompatActivity {
    private AccessToken userToken = AccessToken.getCurrentAccessToken();

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!this.isLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            final TextView currentUser = (TextView) findViewById(R.id.currentUser);
            currentUser.setText("Current user id: " + this.userToken.getUserId());
        }
    }

    public boolean isLoggedIn() {
        return this.userToken != null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        return true;
    }

    public void getPreferences(MenuItem item) {
        Intent intent = new Intent(MainActivity.this, PreferencesActivity.class);
        startActivity(intent);
    }
}
