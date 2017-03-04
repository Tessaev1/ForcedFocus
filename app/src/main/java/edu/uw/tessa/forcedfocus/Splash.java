package edu.uw.tessa.forcedfocus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import java.util.concurrent.TimeUnit;


public class Splash extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        SystemClock.sleep(TimeUnit.SECONDS.toMillis(3));

        Intent i = new Intent(Splash.this,
                MainActivity.class);
        startActivity(i);
        finish();
    }

}
