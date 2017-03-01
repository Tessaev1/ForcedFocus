package edu.uw.tessa.forcedfocus;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.media.CamcorderProfile.get;

/**
 * Created by Tessa on 2/25/17.
 */

public class SendSMS {
    private Context context;
    private Activity activity;
    private Random r;

    public SendSMS(Context context, Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            activity.requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 100);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        }
        r = new Random();
        this.context = context;
        this.activity = activity;
    }

    // Sends this text message to a specific phoneNumber
    public void sendBadText() {
        String message = "I suck at focusing... ";
        List<String> numbers = getNumbers();
        String phoneNumber = numbers.get(r.nextInt(numbers.size()));

        Toast.makeText(this.context, phoneNumber + ": " + message,
                Toast.LENGTH_SHORT).show();
    }

    // Gets the phone contacts
    public List<String> getNumbers() {
        List<String> numbers = new ArrayList<String>();
        ContentResolver cr = activity.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        String id = null, name = null, phone = null;
        int size = cur.getCount();
        if (size > 0) {
            Log.i("contacts", "Have at least one contact");
            while (cur.moveToNext()) {
                name = cur
                        .getString(cur
                                .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                Log.i("contacts", "name: " + name);
                if (Integer.parseInt(cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Log.i("contacts", "phone: " + phone);
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        phone = pCur
                                .getString(pCur
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Log.i("contacts", "phone: " + phone);
                        break;
                    }
                    numbers.add(phone);
                    pCur.close();
                }
            }
        }
        return numbers;
    }
}
