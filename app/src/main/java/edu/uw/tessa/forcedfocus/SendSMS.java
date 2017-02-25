package edu.uw.tessa.forcedfocus;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Tessa on 2/25/17.
 */

public class SendSMS {
    private Context context;

    public SendSMS(Context context) {
        this.context = context;
    }

    // Sends this text message to a specific phoneNumber
    public void sendBadText() {
        String phoneNumber = "3605846299";
        String message = "I suck at Focusing... ";

        Toast.makeText(this.context, phoneNumber + ": " + message,
                Toast.LENGTH_SHORT).show();
    }
}
