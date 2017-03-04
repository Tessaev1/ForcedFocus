package edu.uw.tessa.forcedfocus;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;

/**
 * Created by catherinejohnson on 3/4/17.
 */

public class SetVolume {
    private Context context;
    private Activity activity;
    private boolean setMax;

    public SetVolume(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        setMax = true;
    }

    public void setMaxVolume() {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int streamVolume = 0;
        if (setMax) {
            streamVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        }
        audioManager.setStreamVolume(AudioManager.STREAM_RING, streamVolume,
                AudioManager.FLAG_ALLOW_RINGER_MODES|AudioManager.FLAG_PLAY_SOUND);
        setMax = !setMax;
    }
}
