package edu.uw.tessa.forcedfocus;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private AccessToken userToken = AccessToken.getCurrentAccessToken();
    EditText edtSetTimer;
    TextView tvSecond;
    TextView tvMilliSecond;
    TextView tvTimeUp;
    Button btnStart;
    CountDownTimer countDownTimer;
    int MilisUntilDone = 0;

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

        edtSetTimer = (EditText) findViewById(R.id.edtSetTimer);
        tvSecond = (TextView) findViewById(R.id.tvSecond);
        tvMilliSecond = (TextView) findViewById(R.id.tvMilliSecond);
        tvTimeUp = (TextView) findViewById(R.id.tvTimeUp);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtSetTimer.getText().toString().trim().length() != 0) {
                    int startTime = Integer.parseInt(edtSetTimer.getText().toString()) * 60000;
                    Log.i("startTime", startTime + btnStart.getText().toString());
                    if (v == btnStart && startTime > 0) {
                        if (btnStart.getText().toString().equalsIgnoreCase("START")) {
                            Log.i("startTime Start", btnStart.getText().toString());
                            countDownTimer = new MyCountDownTimer(startTime, 10);
                            btnStart.setVisibility(View.INVISIBLE);
                            tvTimeUp.setVisibility(View.INVISIBLE);
                            edtSetTimer.setEnabled(false);
                            countDownTimer.start();
                        } else {
                            Log.i("startTime Stop", btnStart.getText().toString());
                            btnStart.setText("START");
                            edtSetTimer.setEnabled(true);
                            countDownTimer.cancel();
                        }
                    }
                }
            }
        });
        edtSetTimer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String sec = edtSetTimer.getText().toString();
                if (sec != null && sec != "" && sec.length() > 0) {
                    tvSecond.setText(edtSetTimer.getText());
                    tvMilliSecond.setText("0");
                    tvTimeUp.setVisibility(View.INVISIBLE);
                    tvSecond.setText("0");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
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



    class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onTick(long millisUntilFinished) {
            long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
            if (seconds % 60 < 10) {
                tvMilliSecond.setText("0" + String.valueOf(seconds % 60));
            } else {
                tvMilliSecond.setText(String.valueOf(seconds % 60));
            }
            MilisUntilDone = (int) millisUntilFinished;
            tvSecond.setText(String.valueOf(minutes));
        }
        @Override
        public void onFinish() {
            btnStart.setVisibility(View.VISIBLE);
            edtSetTimer.setEnabled(true);
        }
    }

    @Override
    public void onStop() {
        Log.v(TAG, "onStop has been called");
        if (MilisUntilDone != 0) {
            Toast toast = Toast.makeText(getApplicationContext(), "You failed", Toast.LENGTH_SHORT);
            toast.show();
            countDownTimer.cancel();
            countDownTimer.onFinish();
        }
        super.onStop();
    }
}
