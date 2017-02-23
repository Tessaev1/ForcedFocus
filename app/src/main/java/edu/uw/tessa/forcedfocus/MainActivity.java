package edu.uw.tessa.forcedfocus;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.telephony.SmsManager;

import com.facebook.AccessToken;

import java.util.Timer;
import java.util.TimerTask;

import static android.R.attr.inset;
import static android.R.attr.phoneNumber;
import static android.R.id.message;

public class MainActivity extends AppCompatActivity {
    private AccessToken userToken = AccessToken.getCurrentAccessToken();

    public static final String TAG = "MainActivity";

    public int index = 0;

    public String[] insults = {
      "You failed", "Failure", "Loser", "LOSER!!", "Mwahahaha!"
    };
    public Timer timer;

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
        //sendBadToasts();
        //sendBadText();
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

    // Sends mean toasts every 2 seconds for one minute.
    public void sendBadToasts() {
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        index++;
                        final Toast toast = Toast.makeText(
                                getApplicationContext(),  insults[index % insults.length],
                                Toast.LENGTH_SHORT);
                        toast.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                toast.cancel();
                            }

                        }, 2000);

                    }
                });
            }
        }, 0, 1000);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                timer.cancel();
            }
        }, 60000);
    }


    // Sends this text message to a specific phoneNumber
    // get Conacts is to be added soon.
    public void sendBadText() {
        String phoneNumber = "3605846299";
        String message = "I'm suck at Focusing... ";

        //SmsManager sms = SmsManager.getDefault();
        //sms.sendTextMessage(phoneNumber, null, message, null, null);

        Toast toast = Toast.makeText(
                getApplicationContext(),  phoneNumber + ": " + message,
                Toast.LENGTH_SHORT);
        toast.show();
    }

}
