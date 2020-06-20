package com.example.kindom;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

/**
 * Encapsulate logic for handling alert dialogs
 */
public class Alert {

    /**
     * Show alert dialog
     *
     * @param context the context
     * @param message the message to be displayed
     */
    public static void showAlertDialog(Context context, String message) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked OK button
                    }
                })
                .show();
    }

    /**
     * Show alert dialog and finish activity
     *
     * @param activity the activity to be finished
     * @param context  the context
     * @param message  the message to be displayed
     */
    public static void showAlertDialogAndFinish(final Activity activity, Context context, String message) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                    }
                })
                .show();
    }
}