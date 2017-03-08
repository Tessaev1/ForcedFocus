package edu.uw.tessa.forcedfocus;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.text.*;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.ProfileTracker;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private AccessToken userToken = AccessToken.getCurrentAccessToken();
    private ProfileTracker profileTracker;
    private Profile userProfile = Profile.getCurrentProfile();
    EditText edtSetTimer;
    TextView tvSecond;
    TextView tvMilliSecond;
    TextView tvTimeUp;
    TextView tvSeparator;
    Button btnStart;
    CountDownTimer countDownTimer;
    int milisUntilDone = 0;
    int startTime;
    boolean timerIsTicking;
    boolean FBPostPending = false;

    // Cheat code flag to make the consequences accure sequentially
    // Instead of based on amount of time completed.
    boolean sequential = true;

    int countSequential = 0;

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("");
        setSupportActionBar(myToolbar);

        if (!this.isLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        this.profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                updateUI();
            }
        };
        this.profileTracker.startTracking();

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 1);

        edtSetTimer = (EditText) findViewById(R.id.edtSetTimer);
        tvSecond = (TextView) findViewById(R.id.tvSecond);
        tvMilliSecond = (TextView) findViewById(R.id.tvMilliSecond);
        tvSeparator = (TextView) findViewById(R.id.tvSeparator);
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
                            timerIsTicking = true;
                            countDownTimer = new MyCountDownTimer(startTime, 10);
                            btnStart.setVisibility(View.INVISIBLE);
                            tvTimeUp.setVisibility(View.INVISIBLE);
                            tvSeparator.setVisibility(View.VISIBLE);
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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String sec = edtSetTimer.getText().toString();
                if (sec != null && sec != "" && sec.length() > 0) {
                    tvTimeUp.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
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
            tvTimeUp.setVisibility(View.VISIBLE);
            timerIsTicking = false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (FBPostPending) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("Posting to " + this.userProfile.getName() + "'s Facebook page")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                }
            });
            alert.show();
        }
        FBPostPending = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop has been called");
        if (milisUntilDone != 0 && timerIsTicking) {
            Toast.makeText(this, "Uh oh, looks like you didn't meet your " +
                    "focus goal. Hope you learn your lesson.", Toast.LENGTH_SHORT).show();

            // To calculate punishment based on time spent focusing.
            double timeAtStop = startTime - milisUntilDone;
            double timeRatio = (timeAtStop / ((double) startTime));

            if ((timeRatio * 100 < 20 && !sequential) || (sequential && countSequential % 4 == 0)) {
                SendSMS sendSMS = new SendSMS(getApplicationContext(), MainActivity.this);
                sendSMS.sendBadText();
            } else if ((timeRatio * 100 < 40 && !sequential) ||
                    (sequential && countSequential % 4 == 1)) {
                FBPostPending = true;
            } else if ((timeRatio * 100 >= 70 && !sequential) ||
                    (sequential && countSequential % 4 == 2)) {
                ToastSpam toastSpam = new ToastSpam(MainActivity.this, MainActivity.this);
                toastSpam.sendBadToasts();
            } else if ((timeRatio * 100 < 70 && !sequential) ||
                (sequential && countSequential % 4 == 3)) {
                SetVolume setVolume = new SetVolume(getApplicationContext(), MainActivity.this);
                setVolume.setMaxVolume();
            }

            if (sequential) {
                countSequential++;
            }

            countDownTimer.cancel();
            countDownTimer.onFinish();
            tvTimeUp.setVisibility(View.INVISIBLE);
        }
    }

    private void updateUI() {
        boolean enableButtons = this.userToken != null;

        Profile profile = Profile.getCurrentProfile();
        if (profile == null) {
            Log.e("Profile", "null");
        }
        if (enableButtons && profile != null) {
            Log.e("Access Token", AccessToken.getCurrentAccessToken().toString());
        }
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
