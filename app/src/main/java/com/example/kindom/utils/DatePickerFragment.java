package com.example.kindom.utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.kindom.helpMe.HelpMePostAddActivity;
import com.example.kindom.helpMe.HelpMePostEditActivity;

import java.util.Calendar;
import java.util.Objects;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private HelpMePostAddActivity addActivity = null;
    private HelpMePostEditActivity editActivity = null;

    public DatePickerFragment(Activity activity) {
        if (activity instanceof HelpMePostAddActivity) {
            this.addActivity = (HelpMePostAddActivity) activity;
        } else {
            this.editActivity = (HelpMePostEditActivity) activity;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int year, month, day;

        // Get the previous date input or the current date
        if (addActivity != null) {
            HelpMePostAddActivity activity = addActivity;
            if (activity.getmDate() == null) {
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
            } else {
                year = activity.getmDate()[0];
                month = activity.getmDate()[1];
                day = activity.getmDate()[2];
            }
        } else {
            HelpMePostEditActivity activity = editActivity;
            if (activity.getmDate() == null) {
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
            } else {
                year = activity.getmDate()[0];
                month = activity.getmDate()[1];
                day = activity.getmDate()[2];
            }
        }

        // Create a new instance of DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getActivity()), this, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (addActivity != null) {
            HelpMePostAddActivity activity = addActivity;
            activity.processDatePickerResult(year, month, dayOfMonth);
        } else {
            HelpMePostEditActivity activity = editActivity;
            activity.processDatePickerResult(year, month, dayOfMonth);
        }
    }
}