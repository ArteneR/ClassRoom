package com.xdot.classroom.screens.schedules;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.xdot.classroom.CommonFunctionalities;
import com.xdot.classroom.DataProvider;
import com.xdot.classroom.list_views.schedules_activity.SchedulesListData;
import com.xdot.classroom.list_views.schedules_activity.SchedulesRecyclerViewAdapter;
import com.xdot.classroom.R;
import com.xdot.classroom.schedule.Schedule;
import com.xdot.classroom.screens.create_schedule.CreateScheduleActivity;
import com.xdot.classroom.screens.login.LoginActivity;
import com.xdot.classroom.university_activities.UniversityActivity;
import java.util.ArrayList;



public class SchedulesActivity extends AppCompatActivity {
        private RecyclerView schedulesRecyclerView;
        private RecyclerView.Adapter schedulesAdapter;
        private RecyclerView.LayoutManager schedulesLayoutManager;
        private static String LOG_TAG = "SchedulesActivity";
        private DataProvider dataProvider;
        private FirebaseDatabase firebaseDB;
        private DatabaseReference firebaseDBRef;
        private FirebaseAuth firebaseAuth;
        private FirebaseUser currentUser;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_schedules);

                connectToFirebase();
        }


        @Override
        protected void onStart() {
                super.onStart();

                if (!userIsLoggedIn()) {
                        goToLoginActivity();
                }
                else {
                        displaySchedulesIfUserAccountIsActivated();
                }
        }


        private boolean userIsLoggedIn() {
                currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    return true;
                }
                return false;
        }


        private void displaySchedulesIfUserAccountIsActivated() {
                String userId = currentUser.getUid();

                firebaseDBRef.child("Users").child(userId).child("AccountActivated").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                                Object snapshotValue = snapshot.getValue();

                                if (snapshotValue == null) {
                                        firebaseAuth.signOut();
                                        CommonFunctionalities.displayLongToast("Something went wrong! Please login again!", getApplicationContext());
                                        goToLoginActivity();
                                        return ;
                                }

                                boolean accountActivated = Boolean.parseBoolean(snapshotValue.toString());

                                if (accountActivated) {
                                        addListenerForUpdateDeviceRegistrationId();

                                        initializeDataProviderModule();

                                        activateCustomActionBar();
                                        getFirebaseSchedules();
                                }
                                else {
                                        CommonFunctionalities.displayLongToast("Your user account is not activated yet!", getApplicationContext());
                                        goToLoginActivity();
                                }
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                });
        }


        private void goToLoginActivity() {
                Intent intent = new Intent(this, LoginActivity.class);
                this.startActivity(intent);
        }


        private void connectToFirebase() {
                firebaseDB = FirebaseDatabase.getInstance();
                firebaseDBRef = firebaseDB.getReference();
                firebaseAuth = FirebaseAuth.getInstance();
        }



        private void addListenerForUpdateDeviceRegistrationId() {
                String userId = "4o5JWilDQyTcrY7JyngUhzR8NGj1";

                firebaseDBRef.child("Users").child(userId).child("DeviceRegistrationID").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        String firebaseDeviceRegistrationId = snapshot.getValue().toString();
                        checkForUpdateDeviceRegistrationId(firebaseDeviceRegistrationId);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
        }



        private void checkForUpdateDeviceRegistrationId(String firebaseDeviceRegistrationId) {
                String newDeviceRegistrationId = FirebaseInstanceId.getInstance().getToken();
                Log.d(LOG_TAG, "OLD Token: " + firebaseDeviceRegistrationId);
                Log.d(LOG_TAG, "NEW Token: " + newDeviceRegistrationId);
                if (!newDeviceRegistrationId.equals(firebaseDeviceRegistrationId)) {
                    updateDeviceRegistrationId(newDeviceRegistrationId);
                }
        }



        private void updateDeviceRegistrationId(String newDeviceRegistrationId) {
                String userId = "4o5JWilDQyTcrY7JyngUhzR8NGj1";
                firebaseDBRef.child("Users").child(userId).child("DeviceRegistrationID").setValue(newDeviceRegistrationId);
        }



        private void initializeDataProviderModule() {
                dataProvider = (DataProvider) getApplication();
                dataProvider.init();
                dataProvider.printSchedules();
        }



        @Override
        protected void onResume() {
                super.onResume();
        }



        private ArrayList<SchedulesListData> getDataSet() {
                ArrayList results = new ArrayList<SchedulesListData>();
                int index = 0;
                for (Schedule schedule: dataProvider.getAllSchedules()) {
                    SchedulesListData obj = new SchedulesListData(schedule.Name);
                    results.add(index, obj);
                    index++;
                }
                return results;
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

                View mCustomView = mInflater.inflate(R.layout.custom_actionbar_schedules, null);

                mActionBar.setCustomView(mCustomView);
                mActionBar.setDisplayShowCustomEnabled(true);
        }



        private void getFirebaseSchedules() {
                String userId = "4o5JWilDQyTcrY7JyngUhzR8NGj1";

                firebaseDBRef.child("Users").child(userId).child("Schedules").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            // First delete the schedules
                            dataProvider.removeAllSchedules();

                            for (DataSnapshot scheduleSnapshot: dataSnapshot.getChildren()) {
                                String scheduleId = scheduleSnapshot.getKey();
                                Schedule firebaseSchedule = scheduleSnapshot.getValue(Schedule.class);
                                firebaseSchedule.setId(scheduleId);
                                firebaseSchedule.init();

                                for (DataSnapshot entrySnapshot: scheduleSnapshot.child("Entries").getChildren()) {
                                    String dayOfWeek = entrySnapshot.getKey();

                                    for (DataSnapshot univActivityTypeSnapshot: entrySnapshot.getChildren()) {
                                        String univActivityType = univActivityTypeSnapshot.getKey();

                                        for (DataSnapshot univActivitySnapshot: univActivityTypeSnapshot.getChildren()) {
                                            String univActivityId = univActivitySnapshot.getKey();
                                            String className = "com.xdot.classroom.university_activities." + CommonFunctionalities.pluralToSingular(univActivityType);

                                            try {
                                                Class univActivityClass = Class.forName(className);
                                                UniversityActivity univActivity = (UniversityActivity) univActivitySnapshot.getValue(univActivityClass);
                                                univActivity.setId(univActivityId);
                                                firebaseSchedule.addUniversityActivity(univActivity, dayOfWeek);
                                            }
                                            catch (ClassNotFoundException e) {
                                                e.printStackTrace();
                                            }

                                        }

                                    }

                                }

                                firebaseSchedule.printSchedule();
                                dataProvider.addSchedule(firebaseSchedule);
                            }

                            // display Schedules data...
                            activateSchedulesListView();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                });
        }



        /*
         * Setup the list which will display the available schedules
         */
        public void activateSchedulesListView() {
                Log.d(LOG_TAG, "Schedules count: " + this.dataProvider.getSchedulesCount());
                if (this.dataProvider.getSchedulesCount() == 0) {
                    // Don't activate the schedules list -> display text telling user that there are no schedules
                    return ;
                }

                hideNoSchedulesMessage();


                schedulesLayoutManager = new LinearLayoutManager(this);
                schedulesAdapter = new SchedulesRecyclerViewAdapter(getDataSet());
                schedulesRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewSchedulesList);
                schedulesRecyclerView.setHasFixedSize(true);
                schedulesRecyclerView.setLayoutManager(schedulesLayoutManager);
                schedulesRecyclerView.setAdapter(schedulesAdapter);

                // Code to Add an item with default animation
                //((SchedulesRecyclerViewAdapter) schedulesAdapter).addItem(obj, index);

                // Code to remove an item with default animation
                //((SchedulesRecyclerViewAdapter) schedulesAdapter).deleteItem(index);
        }


        private void hideNoSchedulesMessage() {
                View view = findViewById(R.id.relativeLayoutNoSchedules);
                view.setVisibility(View.INVISIBLE);
                view.setVisibility(View.GONE);
        }


        /*
         * Handle user click events
         */
        public void clicked(View view) {
                Log.d(LOG_TAG, "Button clicked!");

                switch (view.getId()) {
                    case R.id.ivLeftActionbarButton:
                        Log.d(LOG_TAG, "Button: Logout");
                        logout();
                        break;

                    case R.id.ivRightActionbarButton:
                        Log.d(LOG_TAG, "Button: Add Schedule");
                        openCreateScheduleActivity();
                        break;
                }
        }


        private void logout() {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, LoginActivity.class);
                this.startActivity(intent);
        }


        private void openCreateScheduleActivity() {
                Intent intent = new Intent(this, CreateScheduleActivity.class);
                this.startActivity(intent);
        }

}
