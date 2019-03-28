package com.anirudh.user.getfit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class AboutDialogFragment extends DialogFragment {
    public AboutDialogFragment () {
        // empty constructor for DialogFragment
    }
    public static AboutDialogFragment newInstance (String title) {
        AboutDialogFragment aboutDialogFragment = new AboutDialogFragment();
        Bundle args = new Bundle();
        args.putString ("title",title);
        aboutDialogFragment.setArguments(args);
        return aboutDialogFragment;
    }

    public Dialog onCreateDialog (Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle (title);
        builder.setMessage(R.string.about_str);
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }
}
