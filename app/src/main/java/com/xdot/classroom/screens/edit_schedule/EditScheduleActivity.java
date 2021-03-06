package com.xdot.classroom.screens.edit_schedule;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.xdot.classroom.CommonFunctionalities;
import com.xdot.classroom.R;
import com.xdot.classroom.schedule.Schedule;
import com.xdot.classroom.screens.schedules.SchedulesActivity;



public class EditScheduleActivity extends AppCompatActivity {
        private static String LOG_TAG = "EditScheduleActivity";
        private FirebaseDatabase firebaseDB;
        private DatabaseReference firebaseDBRef;
        private FirebaseAuth firebaseAuth;
        private EditText etScheduleName;
        private CheckBox cbSetScheduleAsDefault;
        private EditText etNotificationMinutesBefore;
        private String selectedScheduleId;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_edit_schedule);

                connectToFirebase();
                selectedScheduleId = getIntent().getStringExtra("selected_schedule_id");

                initializeUIElements();
                fillElementsWithFirebaseData();

                activateCustomActionBar();
        }


        private void connectToFirebase() {
                firebaseDB = FirebaseDatabase.getInstance();
                firebaseDBRef = firebaseDB.getReference();
                firebaseAuth = FirebaseAuth.getInstance();
        }


        private void initializeUIElements() {
                etScheduleName = (EditText) findViewById(R.id.etScheduleName);
                cbSetScheduleAsDefault = (CheckBox) findViewById(R.id.cbSetScheduleAsDefault);
                etNotificationMinutesBefore = (EditText) findViewById(R.id.etNotificationMinutesBefore);
        }


        private void fillElementsWithFirebaseData() {
                final String userId = firebaseAuth.getCurrentUser().getUid();
                final String scheduleId = selectedScheduleId;

                firebaseDBRef.child("Users").child(userId).child("Schedules").child(scheduleId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot scheduleSnapshot) {
                                Schedule schedule = scheduleSnapshot.getValue(Schedule.class);
                                etScheduleName.setText(schedule.Name);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                });

                firebaseDBRef.child("Users").child(userId).child("Preferences").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot userPreferencesSnapshot) {
                                String currentlySelectedScheduleId = userPreferencesSnapshot.child("CurrentlySelectedSchedule").getValue().toString();
                                boolean scheduleIsDefaultSchedule = (currentlySelectedScheduleId.equals(scheduleId) ? true : false);
                                int notificationMinutesBefore = Integer.parseInt(userPreferencesSnapshot.child("NotificationMinutesBefore").getValue().toString());
                                cbSetScheduleAsDefault.setChecked(scheduleIsDefaultSchedule);
                                etNotificationMinutesBefore.setText(String.valueOf(notificationMinutesBefore));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                });

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

                View mCustomView = mInflater.inflate(R.layout.custom_actionbar_edit_schedule, null);

                mActionBar.setCustomView(mCustomView);
                mActionBar.setDisplayShowCustomEnabled(true);
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

                    case R.id.btnSaveChanges:
                        Log.d(LOG_TAG, "Button: Save Changes");
                        saveChanges();
                        break;

                    case R.id.btnDeleteSchedule:
                        Log.d(LOG_TAG, "Button: Delete Schedule");
                        openDeleteScheduleConfirmation();
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


        private void saveChanges() {
                String userId = firebaseAuth.getCurrentUser().getUid();
                String scheduleId = selectedScheduleId;
                String editedScheduleName = etScheduleName.getText().toString();
                String editedNotificationMinutesBefore = etNotificationMinutesBefore.getText().toString();
                boolean scheduleSetAsDefault = cbSetScheduleAsDefault.isChecked();

                DatabaseReference scheduleRef = firebaseDBRef.child("Users").child(userId).child("Schedules").child(scheduleId).child("Name").getRef();
                scheduleRef.setValue(editedScheduleName);

                DatabaseReference userPreferencesRef = firebaseDBRef.child("Users").child(userId).child("Preferences").getRef();
                userPreferencesRef.child("NotificationMinutesBefore").setValue(editedNotificationMinutesBefore);
                if (scheduleSetAsDefault) {
                        userPreferencesRef.child("CurrentlySelectedSchedule").setValue(scheduleId);
                }

                CommonFunctionalities.displayShortToast("Changes have been successfully saved!", getApplicationContext());
                goToSchedulesActivity();
        }


        private void openDeleteScheduleConfirmation() {
                new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK)
                    .setTitle("Confirmation")
                    .setMessage("Do you really want to delete this schedule?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                    deleteSchedule();
                                    CommonFunctionalities.displayShortToast("Schedule has been successfully deleted!", getApplicationContext());
                                    goToSchedulesActivity();
                            }})
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        }


        private void deleteSchedule() {
                String userId = firebaseAuth.getCurrentUser().getUid();
                String scheduleId = selectedScheduleId;

                DatabaseReference oldScheduleEntryRef = firebaseDBRef.child("Users").child(userId).child("Schedules").child(scheduleId).getRef();
                oldScheduleEntryRef.setValue(null);
        }

}
