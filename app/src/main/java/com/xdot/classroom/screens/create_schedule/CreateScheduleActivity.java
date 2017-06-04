package com.xdot.classroom.screens.create_schedule;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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



public class CreateScheduleActivity extends AppCompatActivity {
        private static String LOG_TAG = "CreateScheduleActivity";
        private FirebaseDatabase firebaseDB;
        private DatabaseReference firebaseDBRef;
        private FirebaseAuth firebaseAuth;
        private EditText etScheduleName;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_create_schedule);

                connectToFirebase();

                etScheduleName = (EditText) findViewById(R.id.etScheduleName);

                activateCustomActionBar();
        }


        private void connectToFirebase() {
                firebaseDB = FirebaseDatabase.getInstance();
                firebaseDBRef = firebaseDB.getReference();
                firebaseAuth = FirebaseAuth.getInstance();
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

                View mCustomView = mInflater.inflate(R.layout.custom_actionbar_create_schedule, null);

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

                    case R.id.btnCreateSchedule:
                        Log.d(LOG_TAG, "Button: Create Schedule");
                        createNewSchedule();
                        break;
                }
        }


        private void goToPreviousActivity() {
                super.onBackPressed();
        }


        private void createNewSchedule() {
                String userId = firebaseAuth.getCurrentUser().getUid();

                final String newScheduleName = etScheduleName.getText().toString();

                firebaseDBRef.child("Users").child(userId).child("Schedules").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot userSchedulesSnapshot) {
                                if (existsScheduleInFirebase(newScheduleName, userSchedulesSnapshot)) {
                                        CommonFunctionalities.displayShortToast("Schedule name already exists! Please select another name.", getApplicationContext());
                                }
                                else {
                                        createNewScheduleInFirebase(newScheduleName);
                                        CommonFunctionalities.displayShortToast("Schedule has been created!", getApplicationContext());
                                        goToPreviousActivity();
                                }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                });
        }


        private boolean existsScheduleInFirebase(String newScheduleName, DataSnapshot userSchedulesSnapshot) {
                boolean scheduleExists = false;

                for (DataSnapshot scheduleSnapshot: userSchedulesSnapshot.getChildren()) {
                        Schedule schedule = scheduleSnapshot.getValue(Schedule.class);
                        if (schedule.Name.equals(newScheduleName)) {
                                scheduleExists = true;
                                break;
                        }
                }

                return scheduleExists;
        }


        private void createNewScheduleInFirebase(String newScheduleName) {
                String userId = firebaseAuth.getCurrentUser().getUid();

                String newKey = firebaseDBRef.child("Users").child(userId).child("Schedules").push().getKey();
                firebaseDBRef.child("Users").child(userId).child("Schedules").child(newKey).child("Name").setValue(newScheduleName);
        }

}
