package edu.uw.tessa.forcedfocus;

import android.app.*;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.*;

import com.facebook.AccessToken;
import com.facebook.Profile;

public class FBPostFragment extends DialogFragment {
    private AccessToken userToken = AccessToken.getCurrentAccessToken();
    private Profile userProfile = Profile.getCurrentProfile();

    public final static String TAG = "FBPostFragment";

    public FBPostFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Posting to " + this.userProfile.getName() + "'s Facebook page")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }
}
