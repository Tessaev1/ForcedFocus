package edu.uw.tessa.forcedfocus;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.facebook.*;
import com.facebook.login.*;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class LoginActivity extends Activity {
    private CallbackManager callbackManager;
    private LoginButton loginButton;

    public static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.callbackManager = CallbackManager.Factory.create();

        final TextView loginError = (TextView) findViewById(R.id.loginError);
        this.loginButton = (LoginButton) findViewById(R.id.login_button);
        this.loginButton.setPublishPermissions(Arrays.asList("publish_actions"));

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
                loginError.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException exception) {
                loginError.setText("Login attempt failed.");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
