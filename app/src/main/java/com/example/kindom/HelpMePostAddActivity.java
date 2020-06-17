package com.example.kindom;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class HelpMePostAddActivity extends AppCompatActivity {

    private String[] CATEGORIES = new String[]{"Care", "Food", "Groceries", "Others"};

    private int[] mDate;
    private int[] mTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_me_post_add);

        // Set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        // Initialize user's inputs
        setCategoryDropdownMenu();
        fixLocation();
        setButtonsClickListeners();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public int[] getmDate() {
        return mDate;
    }

    public int[] getmTime() {
        return mTime;
    }

    /**
     * Set the list of categories shown in the dropdown menu
     */
    private void setCategoryDropdownMenu() {
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, R.layout.list_item_help_me_category, CATEGORIES);
        AutoCompleteTextView categoryTextView = findViewById(R.id.help_me_post_add_category_dropdown_menu);
        categoryTextView.setAdapter(categoryAdapter);
    }

    /**
     * Fix user's location in the text field
     */
    private void fixLocation() {
        // TODO: Fix location in text field
    }

    /**
     * Set click listeners for date and time buttons
     */
    private void setButtonsClickListeners() {
        MaterialButton dateButton = findViewById(R.id.help_me_post_add_date_button);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "Date");
            }
        });

        MaterialButton timeButton = findViewById(R.id.help_me_post_add_time_button);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "Time");
            }
        });
    }

    /**
     * Process user's selection of date
     * @param year the year chosen
     * @param month the date chosen
     * @param dayOfMonth the day of month chosen
     */
    public void processDatePickerResult(int year, int month, int dayOfMonth) {
        // Create date message
        mDate = new int[]{year, month, dayOfMonth};
        String year_string = Integer.toString(year);
        String month_string = Integer.toString(month + 1);
        String day_string = Integer.toString(dayOfMonth);
        if (month_string.length() == 1) {
            // Append a zero in front if month is 1 digit
            month_string = "0" + month_string;
        }
        if (day_string.length() == 1) {
            // Append a zero in front if day is 1 digit
            day_string = "0" + day_string;
        }
        String dateMessage = day_string + "/" + month_string + "/" + year_string;

        // Set date message
        TextInputEditText dateEditText = findViewById(R.id.help_me_post_add_date_edit_text);
        dateEditText.setText(dateMessage);
        checkDateAndTime();
    }

    /**
     * Process user's selection of time
     * @param hourOfDay the hour of day chosen
     * @param minute the minute chosen
     */
    public void processTimePickerResult(int hourOfDay, int minute) {
        // Create time message
        mTime = new int[]{hourOfDay, minute};
        int hour = hourOfDay <= 12 ? hourOfDay : hourOfDay - 12;
        String hour_string = Integer.toString(hour);
        String minute_string = Integer.toString(minute);
        if (hour_string.length() == 1) {
            // Append a zero in front if hour is 1 digit
            hour_string = "0" + hour_string;
        }
        if (minute_string.length() == 1) {
            // Append a zero in front if minute is 1 digit
            minute_string = "0" + minute_string;
        }
        String am_pm_string = hourOfDay <= 12 ? "AM" : "PM";
        String timeMessage = hour_string + ":" + minute_string + " " + am_pm_string;

        // Set time message
        TextInputEditText timeEditText = findViewById(R.id.help_me_post_add_time_edit_text);
        timeEditText.setText(timeMessage);
        checkDateAndTime();
    }

    /**
     * Check if the date and time inputs are in the past
     */
    private void checkDateAndTime() {
        TextInputLayout timeInputLayout = findViewById(R.id.help_me_post_add_time);
        if (mDate != null && mTime != null) {
            Calendar c = Calendar.getInstance();
            boolean matchYear = c.get(Calendar.YEAR) == mDate[0];
            boolean matchMonth = c.get(Calendar.MONTH) == mDate[1];
            boolean matchDay = c.get(Calendar.DAY_OF_MONTH) == mDate[2];
            if (matchYear && matchMonth && matchDay) {
                if (c.get(Calendar.HOUR_OF_DAY) > mTime[0] ||
                        (c.get(Calendar.HOUR_OF_DAY) == mTime[0] && c.get(Calendar.MINUTE) > mTime[1])) {
                    timeInputLayout.setError(getString(R.string.error_time));
                    return;
                }
            }
        }
        timeInputLayout.setError(null);
    }

    /**
     * Hide user's keyboard
     */
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

        // Find the currently focused view
        View view = getCurrentFocus();

        // Create a new view if no view currently has focus
        if (view == null) {
            view = new View(this);
        }

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}