package com.example.kindom;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Objects;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public DatePickerFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        HelpMePostAddActivity activity = (HelpMePostAddActivity) getActivity();
        assert activity != null;

        // Get the previous date input or the current date
        int year, month, day;
        if (activity.getmDate() == null) {
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        } else {
            year = activity.getmDate()[0];
            month = activity.getmDate()[1];
            day = activity.getmDate()[2];
        }

        // Create a new instance of DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getActivity()), this, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        HelpMePostAddActivity activity = (HelpMePostAddActivity) getActivity();
        assert activity != null;
        activity.processDatePickerResult(year, month, dayOfMonth);
    }
}