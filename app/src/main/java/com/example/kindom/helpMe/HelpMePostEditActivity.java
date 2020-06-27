package com.example.kindom.helpMe;

import androidx.annotation.NonNull;
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

import com.example.kindom.User;
import com.example.kindom.utils.Alert;
import com.example.kindom.utils.CalendarHandler;
import com.example.kindom.utils.DatePickerFragment;
import com.example.kindom.R;
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

public class HelpMePostEditActivity extends AppCompatActivity {

    private String[] CATEGORIES = new String[]{"Care", "Food", "Groceries", "Others"};

    private DatabaseReference mUserRef;
    private DatabaseReference mUserPostRef;
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
    private String mDateString;
    private int[] mTime;
    private String mTimeString;
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
        mUserRef = FirebaseDatabase.getInstance().getReference("users").child(FirebaseHandler.getCurrentUserUid());
        mUserPostRef = FirebaseDatabase.getInstance().getReference("helpMe");
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
                    final String category = Objects.requireNonNull(mCategoryField).getText().toString();
                    final String title = Objects.requireNonNull(mTitleField.getEditText()).getText().toString();
                    final String date = Objects.requireNonNull(mDateField.getEditText()).getText().toString();
                    final String time = Objects.requireNonNull(mTimeField.getEditText()).getText().toString();
                    final String description = Objects.requireNonNull(mDescriptionField.getEditText()).getText().toString();

                    mUserRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // Get current User Java object
                            User user = dataSnapshot.getValue(User.class);
                            assert user != null;

                            // Update in Firebase Database
                            String rc = user.getRc();
                            DatabaseReference uploadRef = mUserPostRef.child(rc).child(FirebaseHandler.getCurrentUserUid()).child(String.valueOf(mPost.getTimeCreated()));
                            uploadRef.child("category").setValue(category);
                            uploadRef.child("title").setValue(title);
                            uploadRef.child("date").setValue(date);
                            uploadRef.child("time").setValue(time);
                            uploadRef.child("description").setValue(description);

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
        mTime = new int[]{hourOfDay, minute};
        mTimeString = CalendarHandler.getTimeString(hourOfDay, minute);
        Objects.requireNonNull(mTimeField.getEditText()).setText(mTimeString);
        checkDateAndTime();
    }

    /**
     * Check if the date and time inputs are in the past
     */
    private void checkDateAndTime() {
        if (CalendarHandler.checkIfExpired(mDateString, mTimeString)) {
            isValidTime = false;
            mTimeField.setError(getString(R.string.error_time));
        } else {
            isValidTime = true;
            mTimeField.setError(null);
        }
    }
}