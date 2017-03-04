package edu.uw.tessa.forcedfocus;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by catherinejohnson on 3/4/17.
 */

public class SetVolume {
    private Context context;
    private Activity activity;
    private Random r;
    private boolean setMax;

    public SetVolume(Context context, Activity activity) {
        r = new Random();
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

        List<Uri> ringtoneList = listRingtones();

        Log.i("setvolume", "ringtone list size:" + ringtoneList.size());

        try {
            RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE,
                    ringtoneList.get(r.nextInt(ringtoneList.size())));
        } catch (Throwable t) {

        }

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
    }

    public List<Uri> listRingtones() {
        List<Uri> list = new ArrayList<Uri>();

        RingtoneManager manager = new RingtoneManager(context);
        manager.setType(RingtoneManager.TYPE_RINGTONE);
        Cursor cursor = manager.getCursor();
        while (cursor.moveToNext()) {
            String title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            Uri ringtoneURI = manager.getRingtoneUri(cursor.getPosition());
            list.add(ringtoneURI);
            // Do something with the title and the URI of ringtone
        }
        return list;
    }
}
