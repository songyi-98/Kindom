package com.example.kindom.helpMe;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.kindom.utils.Alert;
import com.example.kindom.utils.DatePickerFragment;
import com.example.kindom.utils.FirebaseHandler;
import com.example.kindom.R;
import com.example.kindom.utils.TimePickerFragment;
import com.example.kindom.utils.Validation;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Objects;

public class HelpMePostEditActivity extends AppCompatActivity {

    private String[] CATEGORIES = new String[]{"Care", "Food", "Groceries", "Others"};

    private DatabaseReference mUserPostsRef;
    private HelpMePost mPost;
    private AutoCompleteTextView mCategoryField;
    private TextInputLayout mTitleField;
    private MaterialButton mDateButton;
    private TextInputLayout mDateField;
    private MaterialButton mTimeButton;
    private TextInputLayout mTimeField;
    private TextInputLayout mDescriptionField;
    private MaterialButton mSaveButton;
    private int[] mDate;
    private int[] mTime;
    private boolean isValidTitle = true;
    private boolean isValidTime = true;
    private boolean isValidDescription = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_me_post_edit);

        // Set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        // Receive intent
        if (getIntent().getExtras() != null) {
            mPost = (HelpMePost) getIntent().getSerializableExtra("Post");
        }

        // Initialize text fields and buttons
        mCategoryField = findViewById(R.id.help_me_post_edit_category_dropdown_menu);
        mTitleField = findViewById(R.id.help_me_post_edit_title);
        mDateButton = findViewById(R.id.help_me_post_edit_date_button);
        mDateField = findViewById(R.id.help_me_post_edit_date);
        mTimeButton = findViewById(R.id.help_me_post_edit_time_button);
        mTimeField = findViewById(R.id.help_me_post_edit_time);
        mDescriptionField = findViewById(R.id.help_me_post_edit_description);
        mSaveButton = findViewById(R.id.help_me_post_save_button);

        //TODO: Check if post is expired

        // Initialize user's inputs
        setCategoryDropdownMenu();
        populateInputs();
        checkDateAndTime();
        setTextChangedListeners();
        setButtonsClickListeners();

        // Initialize Firebase Database
        mUserPostsRef = FirebaseDatabase.getInstance().getReference().child("helpMe").child(FirebaseHandler.getCurrentUserUid());
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
        mCategoryField.setAdapter(categoryAdapter);
    }

    /**
     * Populate the fields with the current inputs
     */
    private void populateInputs() {
        mCategoryField.setText(mPost.getCategory(), false);
        Objects.requireNonNull(mTitleField.getEditText()).setText(mPost.getTitle());
        Objects.requireNonNull(mDateField.getEditText()).setText(mPost.getDate());
        Objects.requireNonNull(mTimeField.getEditText()).setText(mPost.getTime());
        Objects.requireNonNull(mDescriptionField.getEditText()).setText(mPost.getDescription());
    }

    /**
     * Set text changed listeners for title and description fields
     */
    private void setTextChangedListeners() {
        Objects.requireNonNull(mTitleField.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Validation.isNonEmpty(s)) {
                    isValidTitle = true;
                    mTitleField.setError(null);
                } else {
                    isValidTitle = false;
                    mTitleField.setError(getString(R.string.error_title));
                }
            }
        });

        Objects.requireNonNull(mDescriptionField.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Validation.isNonEmpty(s)) {
                    isValidDescription = true;
                    mDescriptionField.setError(null);
                } else {
                    isValidDescription = false;
                    mDescriptionField.setError(getString(R.string.error_description));
                }
            }
        });
    }

    /**
     * Set click listeners for date, time and save buttons
     */
    private void setButtonsClickListeners() {
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                DialogFragment newFragment = new DatePickerFragment(HelpMePostEditActivity.this);
                newFragment.show(getSupportFragmentManager(), "Date");
            }
        });

        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                DialogFragment newFragment = new TimePickerFragment(HelpMePostEditActivity.this);
                newFragment.show(getSupportFragmentManager(), "Time");
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isValidTitle) {
                    Alert.showAlertDialog(HelpMePostEditActivity.this, getString(R.string.error_title));
                } else if (!isValidTime) {
                    Alert.showAlertDialog(HelpMePostEditActivity.this, getString(R.string.error_time));
                } else if (!isValidDescription) {
                    Alert.showAlertDialog(HelpMePostEditActivity.this, getString(R.string.error_description));
                } else {
                    // Retrieve updated fields
                    String category = Objects.requireNonNull(mCategoryField).getText().toString();
                    String title = Objects.requireNonNull(mTitleField.getEditText()).getText().toString();
                    String date = Objects.requireNonNull(mDateField.getEditText()).getText().toString();
                    String time = Objects.requireNonNull(mTimeField.getEditText()).getText().toString();
                    String description = Objects.requireNonNull(mDescriptionField.getEditText()).getText().toString();

                    // Update in Firebase Database
                    DatabaseReference uploadRef = mUserPostsRef.child(String.valueOf(mPost.getTimeCreated()));
                    uploadRef.child("category").setValue(category);
                    uploadRef.child("title").setValue(title);
                    uploadRef.child("date").setValue(date);
                    uploadRef.child("time").setValue(time);
                    uploadRef.child("description").setValue(description);

                    onBackPressed();
                }
            }
        });
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

    /**
     * Process user's selection of date
     *
     * @param year       the year chosen
     * @param month      the date chosen
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
        Objects.requireNonNull(mDateField.getEditText()).setText(dateMessage);
        checkDateAndTime();
    }

    /**
     * Process user's selection of time
     *
     * @param hourOfDay the hour of day chosen
     * @param minute    the minute chosen
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
        Objects.requireNonNull(mTimeField.getEditText()).setText(timeMessage);
        checkDateAndTime();
    }

    /**
     * Check if the date and time inputs are in the past
     */
    private void checkDateAndTime() {
        if (mDate != null && mTime != null) {
            Calendar c = Calendar.getInstance();
            boolean matchYear = c.get(Calendar.YEAR) == mDate[0];
            boolean matchMonth = c.get(Calendar.MONTH) == mDate[1];
            boolean matchDay = c.get(Calendar.DAY_OF_MONTH) == mDate[2];
            if (matchYear && matchMonth && matchDay) {
                if (c.get(Calendar.HOUR_OF_DAY) > mTime[0] ||
                        (c.get(Calendar.HOUR_OF_DAY) == mTime[0] && c.get(Calendar.MINUTE) > mTime[1])) {
                    isValidTime = false;
                    mTimeField.setError(getString(R.string.error_time));
                    return;
                }
            }
        }
        isValidTime = true;
        mTimeField.setError(null);
    }
}