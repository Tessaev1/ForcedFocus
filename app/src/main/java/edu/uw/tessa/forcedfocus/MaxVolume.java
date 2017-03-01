package edu.uw.tessa.forcedfocus;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by catherinejohnson on 2/28/17.
 */

public class MaxVolume {
    private Context context;
    private Activity activity;

    public MaxVolume(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void setMaxVolume() {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int streamMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        Toast.makeText(context, Integer.toString(streamMaxVolume), Toast.LENGTH_LONG).show();
        audioManager.setStreamVolume(AudioManager.STREAM_RING, streamMaxVolume,
                AudioManager.FLAG_ALLOW_RINGER_MODES|AudioManager.FLAG_PLAY_SOUND);
    }
}
