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

    public SetVolume(Context context, Activity activity) {
        r = new Random();
        this.context = context;
        this.activity = activity;
    }

    public void setMaxVolume() {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int streamVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        audioManager.setStreamVolume(AudioManager.STREAM_RING, streamVolume,
                AudioManager.FLAG_ALLOW_RINGER_MODES|AudioManager.FLAG_PLAY_SOUND);

        List<Uri> ringtoneList = listRingtones();

        int ringtoneIndex = 14;
        if (ringtoneList.size() <= ringtoneIndex) {
            ringtoneIndex = r.nextInt(ringtoneList.size());
        }

        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
                ringtoneList.get(ringtoneIndex));
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
