package com.example.kindom.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.kindom.helpMe.HelpMePostAddActivity;
import com.example.kindom.helpMe.HelpMePostEditActivity;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private HelpMePostAddActivity addActivity = null;
    private HelpMePostEditActivity editActivity = null;

    public TimePickerFragment(Activity activity) {
        if (activity instanceof HelpMePostAddActivity) {
            this.addActivity = (HelpMePostAddActivity) activity;
        } else {
            this.editActivity = (HelpMePostEditActivity) activity;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour, minute;

        // Get the previous time input or the current time
        if (addActivity != null) {
            HelpMePostAddActivity activity = addActivity;
            if (activity.getmTime() == null) {
                hour = c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);
            } else {
                hour = activity.getmTime()[0];
                minute = activity.getmTime()[1];
            }
        } else {
            HelpMePostEditActivity activity = editActivity;
            if (activity.getmTime() == null) {
                hour = c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);
            } else {
                hour = activity.getmTime()[0];
                minute = activity.getmTime()[1];
            }
        }

        // Create a new instance of TimePickerDialog
        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (addActivity != null) {
            HelpMePostAddActivity activity = addActivity;
            activity.processTimePickerResult(hourOfDay, minute);
        } else {
            HelpMePostEditActivity activity = editActivity;
            activity.processTimePickerResult(hourOfDay, minute);
        }
    }
}