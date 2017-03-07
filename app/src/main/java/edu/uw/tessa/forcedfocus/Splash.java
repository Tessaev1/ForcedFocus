package edu.uw.tessa.forcedfocus;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;


public class Splash extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SystemClock.sleep(TimeUnit.SECONDS.toMillis(3));

        Intent i = new Intent(Splash.this,
                MainActivity.class);
        startActivity(i);
        finish();
    }

}
