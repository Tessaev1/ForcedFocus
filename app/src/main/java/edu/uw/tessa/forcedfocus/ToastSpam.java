package edu.uw.tessa.forcedfocus;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Tessa on 2/25/17.
 */

public class ToastSpam {
    private Context context;
    private Activity activity;
    private Timer timer;
    private int index = 0;

    public String[] insults = {"You failed", "Failure", "Loser", "LOSER!!", "Mwahahaha!"};

    public ToastSpam(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void sendBadToasts() {
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                ToastSpam.this.activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        index++;
                        final android.widget.Toast toast = android.widget.Toast.makeText(
                                getApplicationContext(),  insults[index % insults.length],
                                android.widget.Toast.LENGTH_SHORT);
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
}
