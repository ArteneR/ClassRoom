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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.xdot.classroom.DataProvider;
import com.xdot.classroom.list_views.schedules_activity.SchedulesListData;
import com.xdot.classroom.list_views.schedules_activity.SchedulesRecyclerViewAdapter;
import com.xdot.classroom.R;
import com.xdot.classroom.screens.create_schedule.CreateScheduleActivity;
import com.xdot.classroom.send_notifications.RegistrationService;

import java.util.ArrayList;



public class SchedulesActivity extends AppCompatActivity {
        private RecyclerView schedulesRecyclerView;
        private RecyclerView.Adapter schedulesAdapter;
        private RecyclerView.LayoutManager schedulesLayoutManager;
        private static String LOG_TAG = "SchedulesActivity";
        private DataProvider dataProvider;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_schedules);

                connectToFirebase();

                initializeDataProviderModule();

                activateCustomActionBar();
                activateSchedulesListView();
                activateNotificationsRegistrationService();
        }



        private void connectToFirebase() {
                Log.d(LOG_TAG, "connectToFirebase");
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();

                myRef.child("test").setValue("Hello, World!");
                Log.d(LOG_TAG, "HERE1");
        }



        private void initializeDataProviderModule() {
                dataProvider = (DataProvider)getApplication();
                dataProvider.init();
                dataProvider.printSchedules();
        }



        @Override
        protected void onResume() {
                super.onResume();
                if (schedulesAdapter != null) {
                    ((SchedulesRecyclerViewAdapter) schedulesAdapter).setOnItemClickListener(new SchedulesRecyclerViewAdapter
                        .MyClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            Log.i(LOG_TAG, " Clicked on Item " + position + " - " + v);
                        }
                    });
                }
        }



        private ArrayList<SchedulesListData> getDataSet() {
                ArrayList results = new ArrayList<SchedulesListData>();
                for (int index = 0; index < 6; index++) {
                    SchedulesListData obj = new SchedulesListData("Schedule " + index + " Name");
                    results.add(index, obj);
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



        /*
         * Setup the list which will display the available schedules
         */
        private void activateSchedulesListView() {
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



        private void activateNotificationsRegistrationService() {
                Intent i = new Intent(this, RegistrationService.class);
                startService(i);
        }



        private void hideNoSchedulesMessage() {
                View view = findViewById(R.id.relativeLayoutNoSchedules);
                view.setVisibility(View.INVISIBLE);
                view.setVisibility(View.GONE);
        }



        private void openCreateScheduleActivity() {
                Intent intent = new Intent(this, CreateScheduleActivity.class);
                this.startActivity(intent);
        }



        /*
         * Handle user click events
         */
        public void clicked(View view) {
                Log.d(LOG_TAG, "Button clicked!");

                switch (view.getId()) {
                    case R.id.ivLeftActionbarButton:
                        Log.d(LOG_TAG, "Button: Back");
                        break;

                    case R.id.ivRightActionbarButton:
                        Log.d(LOG_TAG, "Button: Add Schedule");
                        openCreateScheduleActivity();
                        break;
                }
        }
}
