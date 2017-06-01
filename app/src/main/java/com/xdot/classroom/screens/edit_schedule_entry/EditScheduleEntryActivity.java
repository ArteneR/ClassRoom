package com.xdot.classroom.screens.edit_schedule_entry;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.xdot.classroom.CommonFunctionalities;
import com.xdot.classroom.DataProvider;
import com.xdot.classroom.R;
import com.xdot.classroom.screens.edit_schedule_entry.fragments.EndTimePickerFragment;
import com.xdot.classroom.screens.edit_schedule_entry.fragments.StartTimePickerFragment;
import com.xdot.classroom.screens.schedules.SchedulesActivity;
import com.xdot.classroom.university_activities.UniversityActivity;



public class EditScheduleEntryActivity extends AppCompatActivity {
        private static String LOG_TAG = "EditScheduleEntryActivity";
        private Spinner spinnerEntryType;
        private EditText etSubjectName;
        private Button btnStartTime;
        private Button btnEndTime;
        private EditText etBuildingName;
        private EditText etRoomName;
        private DataProvider dataProvider;
        private FirebaseDatabase firebaseDB;
        private DatabaseReference firebaseDBRef;
        private String selectedEntryType;
        private String selectedSubjectName;
        private String selectedStartTime;
        private String selectedEndTime;
        private String selectedBuildingName;
        private String selectedRoomName;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_edit_schedule_entry);

                connectToFirebase();
                initializeDataProviderModule();

                initializeUIElements();
                fillElementsWithFirebaseData();

                activateCustomActionBar();
                activateEntryTypeSpinner();
        }


        private void connectToFirebase() {
                firebaseDB = FirebaseDatabase.getInstance();
                firebaseDBRef = firebaseDB.getReference();
        }


        private void initializeDataProviderModule() {
                dataProvider = (DataProvider) getApplication();
        }


        private void initializeUIElements() {
                spinnerEntryType = (Spinner) findViewById(R.id.spinnerEntryType);
                etSubjectName = (EditText) findViewById(R.id.etSubjectName);
                btnStartTime = (Button) findViewById(R.id.btnStartTime);
                btnEndTime = (Button) findViewById(R.id.btnEndTime);
                etBuildingName = (EditText) findViewById(R.id.etBuildingName);
                etRoomName = (EditText) findViewById(R.id.etRoomName);
        }


        private void fillElementsWithFirebaseData() {
                String userId = "4o5JWilDQyTcrY7JyngUhzR8NGj1";
                String scheduleId = "-KaXH2rswZJJXVWrJ5dS";
                String dayOfWeek = "Monday";
                final String entryType = "Labs";
                String entryId = "-KagQdaMzoITK559_rop";

                firebaseDBRef.child("Users").child(userId).child("Schedules").child(scheduleId).child("Entries").child(dayOfWeek).child(entryType).child(entryId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot univActivitySnapshot) {
                                String className = "com.xdot.classroom.university_activities." + CommonFunctionalities.pluralToSingular(entryType);

                                try {
                                    Class univActivityClass = Class.forName(className);
                                    UniversityActivity univActivity = (UniversityActivity) univActivitySnapshot.getValue(univActivityClass);
                                    if (univActivity == null) {
                                        CommonFunctionalities.displayLongToast("Couldn't load data for the current schedule entry!", getApplicationContext());
                                        return ;
                                    }
                                    fillUniversityActivityElements(univActivity, className);
                                }
                                catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                });

        }


        private void fillUniversityActivityElements(UniversityActivity univActivity, String className) {
                String entryType = extractEntryTypeFromClassName(className);
                int spinnerEntryTypeIndex = getEntryTypeSpinnerValueIndex(entryType);
                spinnerEntryType.setSelection(spinnerEntryTypeIndex);
                etSubjectName.setText(univActivity.Subject);
                btnStartTime.setText(univActivity.StartTime);
                btnEndTime.setText(univActivity.EndTime);
                etBuildingName.setText(univActivity.Building);
                etRoomName.setText(univActivity.Room);
        }


        private String extractEntryTypeFromClassName(String className) {
                String[] classNameParts = className.split("\\.");
                String entryType = classNameParts[classNameParts.length-1];
                return entryType;
        }


        private int getEntryTypeSpinnerValueIndex(String value) {
                for (int i = 0; i < spinnerEntryType.getCount(); i++) {
                    if (spinnerEntryType.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
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

                View mCustomView = mInflater.inflate(R.layout.custom_actionbar_edit_schedule_entry, null);

                mActionBar.setCustomView(mCustomView);
                mActionBar.setDisplayShowCustomEnabled(true);
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

                    case R.id.btnSaveChanges:
                        Log.d(LOG_TAG, "Button: Save Changes");
                        saveChanges();
                        break;

                    case R.id.btnDeleteScheduleEntry:
                        Log.d(LOG_TAG, "Button: Delete Schedule Entry");
                        openDeleteScheduleEntryConfirmation();
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


        private void getUserSelectedValues() {
                selectedEntryType = spinnerEntryType.getSelectedItem().toString();
                selectedSubjectName = etSubjectName.getText().toString();
                selectedStartTime = btnStartTime.getText().toString();
                selectedEndTime = btnEndTime.getText().toString();
                selectedBuildingName = etBuildingName.getText().toString();
                selectedRoomName = etRoomName.getText().toString();
        }


        private void saveChanges() {
                getUserSelectedValues();

                String userId = "4o5JWilDQyTcrY7JyngUhzR8NGj1";
                String scheduleId = "-KaXH2rswZJJXVWrJ5dS";
                String dayOfWeek = "Monday";
                String entryId = "-KagQdaMzoITK559_rop";
                String newEntryType = CommonFunctionalities.singularToPlural(selectedEntryType);

                deleteScheduleEntry();

                DatabaseReference newScheduleEntryRef = firebaseDBRef.child("Users").child(userId).child("Schedules").child(scheduleId).child("Entries").child(dayOfWeek).child(newEntryType).child(entryId).getRef();
                UniversityActivity editedUnivActivity = new UniversityActivity("UNDEFINED_INDEX",
                                                                               selectedStartTime,
                                                                               selectedEndTime,
                                                                               selectedRoomName,
                                                                               selectedBuildingName,
                                                                               selectedSubjectName);
                Log.d(LOG_TAG, "newScheduleEntryRef: " + newScheduleEntryRef);
                newScheduleEntryRef.setValue(editedUnivActivity);
                Log.d(LOG_TAG, "newScheduleEntryRef: " + newScheduleEntryRef);

                CommonFunctionalities.displayShortToast("Changes have been successfully saved!", getApplicationContext());
                goToSchedulesActivity();
        }


        private void openDeleteScheduleEntryConfirmation() {
                new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK)
                    .setTitle("Confirmation")
                    .setMessage("Do you really want to delete this schedule entry?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                    deleteScheduleEntry();
                                    CommonFunctionalities.displayShortToast("Schedule entry has been successfully deleted!", getApplicationContext());
                                    goToSchedulesActivity();
                            }})
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        }


        private void deleteScheduleEntry() {
                String userId = "4o5JWilDQyTcrY7JyngUhzR8NGj1";
                String scheduleId = "-KaXH2rswZJJXVWrJ5dS";
                String dayOfWeek = "Monday";
                final String entryType = "Labs";
                String entryId = "-KagQdaMzoITK559_rop";

                DatabaseReference oldScheduleEntryRef = firebaseDBRef.child("Users").child(userId).child("Schedules").child(scheduleId).child("Entries").child(dayOfWeek).child(entryType).child(entryId).getRef();
                oldScheduleEntryRef.setValue(null);
        }
}
