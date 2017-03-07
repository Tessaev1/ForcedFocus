package edu.uw.tessa.forcedfocus;

import android.app.*;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.*;

public class AlertDialog extends DialogFragment {
    private static final String ARG_PARAM1 = "message";
    private String message;

    public final static String TAG = "AlertDialog";

    public AlertDialog() {}

    public static AlertDialog newInstance(String message) {
        AlertDialog fragment = new AlertDialog();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, message);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.message = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setMessage(this.message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }
}
