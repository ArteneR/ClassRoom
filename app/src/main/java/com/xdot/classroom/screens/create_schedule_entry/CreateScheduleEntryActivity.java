package com.xdot.classroom.screens.create_schedule_entry;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.xdot.classroom.CommonFunctionalities;
import com.xdot.classroom.DataProvider;
import com.xdot.classroom.R;
import com.xdot.classroom.screens.edit_schedule_entry.fragments.EndTimePickerFragment;
import com.xdot.classroom.screens.edit_schedule_entry.fragments.StartTimePickerFragment;
import com.xdot.classroom.screens.schedules.SchedulesActivity;
import com.xdot.classroom.university_activities.UniversityActivity;



public class CreateScheduleEntryActivity extends AppCompatActivity {
        private static String LOG_TAG = "CodeTemplateActivity";
        private DataProvider dataProvider;
        private FirebaseDatabase firebaseDB;
        private DatabaseReference firebaseDBRef;
        private FirebaseAuth firebaseAuth;
        private Spinner spinnerDayOfTheWeek;
        private Spinner spinnerEntryType;
        private EditText etSubjectName;
        private Button btnStartTime;
        private Button btnEndTime;
        private EditText etBuildingName;
        private EditText etRoomName;
        private String selectedDayOfTheWeek;
        private String selectedEntryType;
        private String selectedSubjectName;
        private String selectedStartTime;
        private String selectedEndTime;
        private String selectedBuildingName;
        private String selectedRoomName;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_create_schedule_entry);

                connectToFirebase();
                initializeDataProviderModule();

                selectedDayOfTheWeek = getIntent().getStringExtra("selected_day_of_week");

                initializeUIElements();

                activateCustomActionBar();
                activateDayOfTheWeekSpinner();
                activateEntryTypeSpinner();

                fillElementsWithData();
        }


        private void connectToFirebase() {
                firebaseDB = FirebaseDatabase.getInstance();
                firebaseDBRef = firebaseDB.getReference();
                firebaseAuth = FirebaseAuth.getInstance();
        }


        private void initializeDataProviderModule() {
                dataProvider = (DataProvider) getApplication();
        }


        private void initializeUIElements() {
                spinnerDayOfTheWeek = (Spinner) findViewById(R.id.spinnerDayOfTheWeek);
                spinnerEntryType = (Spinner) findViewById(R.id.spinnerEntryType);
                etSubjectName = (EditText) findViewById(R.id.etSubjectName);
                btnStartTime = (Button) findViewById(R.id.btnStartTime);
                btnEndTime = (Button) findViewById(R.id.btnEndTime);
                etBuildingName = (EditText) findViewById(R.id.etBuildingName);
                etRoomName = (EditText) findViewById(R.id.etRoomName);
        }


        private void fillElementsWithData() {
                int spinnerDayOfTheWeekIndex = getDayOfTheWeekSpinnerValueIndex(selectedDayOfTheWeek);
                spinnerDayOfTheWeek.setSelection(spinnerDayOfTheWeekIndex);
        }


        private int getDayOfTheWeekSpinnerValueIndex(String value) {
                for (int i = 0; i < spinnerDayOfTheWeek.getCount(); i++) {
                    if (spinnerDayOfTheWeek.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                        return i;
                    }
                }
                return -1;
        }


        /*
         * Setup the custom action bar for navigation
         */
        private void activateCustomActionBar() {
                android.support.v7.app.ActionBar mActionBar = getSupportActionBar();

                mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#BB266195")));
                mActionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#BB266195")));

                mActionBar.setDisplayShowHomeEnabled(false);
                mActionBar.setDisplayShowTitleEnabled(false);
                LayoutInflater mInflater = LayoutInflater.from(this);

                View mCustomView = mInflater.inflate(R.layout.custom_actionbar_create_schedule_entry, null);

                mActionBar.setCustomView(mCustomView);
                mActionBar.setDisplayShowCustomEnabled(true);
        }


        private void activateDayOfTheWeekSpinner() {
                Spinner spinner = (Spinner) findViewById(R.id.spinnerDayOfTheWeek);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                                                                                     R.array.days_of_week_array,
                                                                                     android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
        }


        private void activateEntryTypeSpinner() {
                Spinner spinner = (Spinner) findViewById(R.id.spinnerEntryType);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                                                                                     R.array.schedule_entry_types_array,
                                                                                     android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
        }


        /*
         * Handle user click events
         */
        public void clicked(View view) {
                Log.d(LOG_TAG, "Button clicked!");

                switch (view.getId()) {
                        case R.id.ivLeftActionbarButton:
                                Log.d(LOG_TAG, "Button: Back");
                                goToPreviousActivity();
                                break;

                        case R.id.btnStartTime:
                                Log.d(LOG_TAG, "Button: Start Time");
                                showStartTimePickerDialog();
                                break;

                        case R.id.btnEndTime:
                                Log.d(LOG_TAG, "Button: End Time");
                                showEndTimePickerDialog();
                                break;

                        case R.id.btnCreateScheduleEntry:
                                Log.d(LOG_TAG, "Button: Create Schedule Entry");
                                createScheduleEntry();
                                break;
                }
        }


        private void goToPreviousActivity() {
                super.onBackPressed();
        }


        private void goToSchedulesActivity() {
                Intent intent = new Intent(this, SchedulesActivity.class);
                this.startActivity(intent);
        }


        private void showStartTimePickerDialog() {
                StartTimePickerFragment newFragment = new StartTimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "startTimePicker");
        }


        private void showEndTimePickerDialog() {
                EndTimePickerFragment newFragment = new EndTimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "endTimePicker");
        }


        private void createScheduleEntry() {
                getUserSelectedValues();

                String userId = firebaseAuth.getCurrentUser().getUid();
                String scheduleId = dataProvider.getCurrentScheduleId();
                String dayOfWeek = CommonFunctionalities.capitalizeFirstLetter(selectedDayOfTheWeek);
                String newEntryType = CommonFunctionalities.singularToPlural(selectedEntryType);

                DatabaseReference newScheduleEntryRef = firebaseDBRef.child("Users").child(userId).child("Schedules").child(scheduleId).child("Entries").child(dayOfWeek).child(newEntryType).push();
                UniversityActivity newUnivActivity = new UniversityActivity("UNDEFINED_INDEX",
                                                                            selectedStartTime,
                                                                            selectedEndTime,
                                                                            selectedRoomName,
                                                                            selectedBuildingName,
                                                                            selectedSubjectName);
                newScheduleEntryRef.setValue(newUnivActivity);

                CommonFunctionalities.displayShortToast("Changes have been successfully saved!", getApplicationContext());
                goToSchedulesActivity();
        }


        private void getUserSelectedValues() {
                selectedDayOfTheWeek = spinnerDayOfTheWeek.getSelectedItem().toString();
                selectedEntryType = spinnerEntryType.getSelectedItem().toString();
                selectedSubjectName = etSubjectName.getText().toString();
                selectedStartTime = btnStartTime.getText().toString();
                selectedEndTime = btnEndTime.getText().toString();
                selectedBuildingName = etBuildingName.getText().toString();
                selectedRoomName = etRoomName.getText().toString();
        }

}
