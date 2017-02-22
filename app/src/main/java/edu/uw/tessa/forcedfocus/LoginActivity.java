package edu.uw.tessa.forcedfocus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private TextView userInfo;

    public static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.callbackManager = CallbackManager.Factory.create();

        this.userInfo = (TextView) findViewById(R.id.userInfo);
        this.loginButton = (LoginButton) findViewById(R.id.login_button);
//        this.loginButton.setReadPermissions(Arrays.asList("user_status"));

        // Callback registration
        this.loginButton.registerCallback(this.callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken.setCurrentAccessToken(loginResult.getAccessToken());

                LoginManager.getInstance().logInWithPublishPermissions(
                        LoginActivity.this, Arrays.asList("publish_actions"));

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancel() {
                LoginActivity.this.userInfo.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException exception) {
                LoginActivity.this.userInfo.setText("Login attempt failed.");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
