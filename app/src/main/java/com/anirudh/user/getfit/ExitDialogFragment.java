package com.anirudh.user.getfit;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.app.Dialog;
import android.support.v7.app.AlertDialog;

public class ExitDialogFragment extends DialogFragment {
    public ExitDialogFragment ()
    {
        // empty constructor required for DialogFragment
    }
    public static ExitDialogFragment newInstance (String title)
    {
        ExitDialogFragment exitDialogFragment = new ExitDialogFragment();
        Bundle args = new Bundle();
        args.putString ("title",title);
        exitDialogFragment.setArguments(args);
        return exitDialogFragment;
    }

    public Dialog onCreateDialog (Bundle savedInstanceState) {
        String title = getArguments().getString ("title");
        AlertDialog.Builder builder = new AlertDialog.Builder (getActivity());
        builder.setTitle(title);
        builder.setMessage (R.string.exit_dialog);
        builder.setPositiveButton(R.string.yes_str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((MainActivity) getActivity()).finish();
            }
        });
        builder.setNegativeButton(R.string.no_str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }

}
