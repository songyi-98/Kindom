package com.example.kindom.helpMe;

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

import com.example.kindom.R;
import com.example.kindom.User;
import com.example.kindom.utils.Alert;
import com.example.kindom.utils.CalendarHandler;
import com.example.kindom.utils.DatePickerFragment;
import com.example.kindom.utils.FirebaseHandler;
import com.example.kindom.utils.TimePickerFragment;
import com.example.kindom.utils.Validation;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.Objects;

public class HelpMePostAddActivity extends AppCompatActivity {

    private String[] CATEGORIES = new String[]{"Care", "Food", "Groceries", "Others"};

    private DatabaseReference mUserRef;
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
    private String mDateString;
    private int[] mTime;
    private String mTimeString;
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
        setTextChangedListeners();
        setButtonsClickListeners();

        // Initialize Firebase Database
        mUserRef = FirebaseDatabase.getInstance().getReference("users").child(FirebaseHandler.getCurrentUserUid());
        mUploadRef = FirebaseDatabase.getInstance().getReference("helpMe");
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
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, R.layout.list_item_dropdown_menu, CATEGORIES);
        AutoCompleteTextView categoryTextView = findViewById(R.id.help_me_post_add_category_dropdown_menu);
        categoryTextView.setAdapter(categoryAdapter);
    }

    /**
     * Set text changed listeners for category, title, location and description fields
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
                    mUserRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // Get current User Java object
                            User user = dataSnapshot.getValue(User.class);
                            assert user != null;

                            // Create a HelpMePost
                            String category = Objects.requireNonNull(mCategoryField.getEditText()).getText().toString();
                            String title = Objects.requireNonNull(mTitleField.getEditText()).getText().toString();
                            String blkNo = getString(R.string.blk) + " " + user.getBlkNo();
                            String date = Objects.requireNonNull(mDateField.getEditText()).getText().toString();
                            String time = Objects.requireNonNull(mTimeField.getEditText()).getText().toString();
                            String description = Objects.requireNonNull(mDescriptionField.getEditText()).getText().toString();
                            HelpMePost post = new HelpMePost(category, title, blkNo, date, time, description);

                            // Add post to database
                            String rc = user.getRc();
                            long timeCreated = new Date().getTime();
                            mUploadRef.child(rc).child(FirebaseHandler.getCurrentUserUid()).child(String.valueOf(timeCreated)).setValue(post);
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
        mDate = new int[]{year, month, dayOfMonth};
        mDateString = CalendarHandler.getDateString(year, month, dayOfMonth);
        Objects.requireNonNull(mDateField.getEditText()).setText(mDateString);
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
        mTime = new int[]{hourOfDay, minute};
        mTimeString = CalendarHandler.getTimeString(hourOfDay, minute);
        Objects.requireNonNull(mTimeField.getEditText()).setText(mTimeString);
        checkDateAndTime();
    }

    /**
     * Check if the date and time inputs are in the past
     */
    private void checkDateAndTime() {
        if (mDateString != null && mTimeString != null) {
            if (CalendarHandler.checkIfExpired(mDateString, mTimeString)) {
                isValidTime = false;
                mTimeField.setError(getString(R.string.error_time));
            } else {
                isValidTime = true;
                mTimeField.setError(null);
            }
        }
    }
}