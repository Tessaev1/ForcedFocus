package edu.uw.tessa.forcedfocus;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;

public class PreferencesActivity extends Activity {
    private AccessToken userToken = AccessToken.getCurrentAccessToken();
    public static final String TAG = "PreferencesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        Log.i(TAG, "permission: " + this.userToken.getPermissions());

        final LoginButton logout = (LoginButton) findViewById(R.id.login_button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(PreferencesActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
