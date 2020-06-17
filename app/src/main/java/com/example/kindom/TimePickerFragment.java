package com.example.kindom;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    public TimePickerFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        HelpMePostAddActivity activity = (HelpMePostAddActivity) getActivity();
        assert activity != null;

        // Get the previous time input or the current time
        int hour, minute;
        if (activity.getmTime() == null) {
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
        } else {
            hour = activity.getmTime()[0];
            minute = activity.getmTime()[1];
        }

        // Create a new instance of TimePickerDialog
        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        HelpMePostAddActivity activity = (HelpMePostAddActivity) getActivity();
        assert activity != null;
        activity.processTimePickerResult(hourOfDay, minute);
    }
}