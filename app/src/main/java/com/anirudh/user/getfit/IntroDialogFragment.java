package com.anirudh.user.getfit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;

import static android.content.Context.MODE_PRIVATE;

public class IntroDialogFragment extends DialogFragment {
    public boolean done = false;
    public IntroDialogFragment ()
    {

    }



    public static IntroDialogFragment getInstance (String title)
    {
        IntroDialogFragment introDialogFragment = new IntroDialogFragment();
        Bundle args = new Bundle();
        args.putString("title",title);
        introDialogFragment.setArguments(args);
        return introDialogFragment;

    }
    public Dialog onCreateDialog (Bundle savedInstanceState)
    {
        String title= getArguments().getString("title");


        Bundle bundle = this.getArguments();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(title);
        builder.setMessage(R.string.welcome_msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                done = true;
                    dialog.dismiss();

            }
        });
        return builder.create();
    }
}
