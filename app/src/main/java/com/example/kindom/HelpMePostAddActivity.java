package com.example.kindom;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class HelpMePostAddActivity extends AppCompatActivity {

    private String[] CATEGORIES = new String[]{"Care", "Food", "Groceries", "Others"};

    private DatabaseReference mUploadRef;
    private TextInputLayout mCategoryField;
    private TextInputLayout mTitleField;
    private MaterialButton mDateButton;
    private TextInputLayout mDateField;
    private MaterialButton mTimeButton;
    private TextInputLayout mTimeField;
    private TextInputLayout mDescriptionField;
    private MaterialButton mAddButton;
    private int[] mDate;
    private int[] mTime;
    private boolean isValidCategory = false;
    private boolean isValidTitle = false;
    private boolean isValidDate = false;
    private boolean isNonEmptyTime = false;
    private boolean isValidTime = false;
    private boolean isValidDescription = false;

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

        // Initialize text fields and buttons
        mCategoryField = findViewById(R.id.help_me_post_add_category);
        mTitleField = findViewById(R.id.help_me_post_add_title);
        mDateButton = findViewById(R.id.help_me_post_add_date_button);
        mDateField = findViewById(R.id.help_me_post_add_date);
        mTimeButton = findViewById(R.id.help_me_post_add_time_button);
        mTimeField = findViewById(R.id.help_me_post_add_time);
        mDescriptionField = findViewById(R.id.help_me_post_add_description);
        mAddButton = findViewById(R.id.help_me_post_add_button);

        // Initialize user's inputs
        setCategoryDropdownMenu();
        fixLocation();
        setTextChangedListeners();
        setButtonsClickListeners();

        // Initialize Firebase Database
        mUploadRef = FirebaseDatabase.getInstance().getReference().child("helpMe").child(FirebaseHandler.getUserUid());
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
     * Set text changed listeners for category, title and description fields
     */
    private void setTextChangedListeners() {
        Objects.requireNonNull(mCategoryField.getEditText()).addTextChangedListener(new TextWatcher() {
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
                isValidCategory = true;
            }
        });

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
     * Set click listeners for date, time and add buttons
     */
    private void setButtonsClickListeners() {
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                DialogFragment newFragment = new DatePickerFragment(HelpMePostAddActivity.this);
                newFragment.show(getSupportFragmentManager(), "Date");
            }
        });

        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                DialogFragment newFragment = new TimePickerFragment(HelpMePostAddActivity.this);
                newFragment.show(getSupportFragmentManager(), "Time");
            }
        });

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isValidCategory) {
                    Alert.showAlertDialog(HelpMePostAddActivity.this, getString(R.string.error_category));
                } else if (!isValidTitle) {
                    Alert.showAlertDialog(HelpMePostAddActivity.this, getString(R.string.error_title));
                } else if (!isValidDate) {
                    Alert.showAlertDialog(HelpMePostAddActivity.this, getString(R.string.error_date));
                } else if (!isNonEmptyTime) {
                    Alert.showAlertDialog(HelpMePostAddActivity.this, getString(R.string.error_empty_time));
                } else if (!isValidTime) {
                    Alert.showAlertDialog(HelpMePostAddActivity.this, getString(R.string.error_time));
                } else if (!isValidDescription) {
                    Alert.showAlertDialog(HelpMePostAddActivity.this, getString(R.string.error_description));
                } else {
                    // Retrieve user's data from Firebase Database
                    DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference("users");
                    userDatabase.child(FirebaseHandler.getUserUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User currUser = dataSnapshot.getValue(User.class);
                            assert currUser != null;
                            int postalCode = currUser.getPostalCode();

                            // Create a HelpMePost
                            String category = Objects.requireNonNull(mCategoryField.getEditText()).getText().toString();
                            String title = Objects.requireNonNull(mTitleField.getEditText()).getText().toString();
                            String user = FirebaseHandler.getUser().getDisplayName();
                            String location = String.valueOf(postalCode);
                            // TODO: Retrieve block number from postal code
                            String date = Objects.requireNonNull(mDateField.getEditText()).getText().toString();
                            String time = Objects.requireNonNull(mTimeField.getEditText()).getText().toString();
                            String description = Objects.requireNonNull(mDescriptionField.getEditText()).getText().toString();
                            HelpMePost post = new HelpMePost(category, title, user, location, date, time, description);

                            // Add post to database
                            long timeCreated = new Date().getTime();
                            mUploadRef.child(String.valueOf(timeCreated)).setValue(post);
                            onBackPressed();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Do nothing
                        }
                    });
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
        isValidDate = true;

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
        isNonEmptyTime = true;

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