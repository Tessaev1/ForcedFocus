package edu.uw.tessa.forcedfocus;

import android.Manifest;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.*;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.facebook.AccessToken;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private AccessToken userToken = AccessToken.getCurrentAccessToken();
    EditText edtSetTimer;
    TextView tvSecond;
    TextView tvMilliSecond;
    TextView tvTimeUp;
    Button btnStart;
    CountDownTimer countDownTimer;
    SendSMS sendSMS;
    int milisUntilDone = 0;
    int startTime;

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!this.isLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        // Might need this line for SMS to work on your phones.
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 1);

        sendSMS = new SendSMS(MainActivity.this, MainActivity.this);
        edtSetTimer = (EditText) findViewById(R.id.edtSetTimer);
        tvSecond = (TextView) findViewById(R.id.tvSecond);
        tvMilliSecond = (TextView) findViewById(R.id.tvMilliSecond);
        tvTimeUp = (TextView) findViewById(R.id.tvTimeUp);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtSetTimer.getText().toString().trim().length() != 0) {
                    startTime = Integer.parseInt(edtSetTimer.getText().toString()) * 60000;
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
            milisUntilDone = (int) millisUntilFinished;
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
        Log.i(TAG, "onStop has been called");
        if (milisUntilDone != 0) {
            double timeAtStop = startTime - milisUntilDone;
            double timeRatio = (timeAtStop / ((double) startTime));

            if (timeRatio * 100 < 25) {
                sendSMS.sendBadText();
            } else {
                ToastSpam toastSpam = new ToastSpam(MainActivity.this, MainActivity.this);
                toastSpam.sendBadToasts();
            }

            countDownTimer.cancel();
            countDownTimer.onFinish();
        }
        super.onStop();
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
