package com.xdot.classroom.screens.edit_schedule_entry;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.xdot.classroom.R;
import com.xdot.classroom.screens.edit_schedule_entry.fragments.EndTimePickerFragment;
import com.xdot.classroom.screens.edit_schedule_entry.fragments.StartTimePickerFragment;



public class EditScheduleEntryActivity extends AppCompatActivity {
        private static String LOG_TAG = "EditScheduleEntryActivity";


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_edit_schedule_entry);

                activateCustomActionBar();
                activateEntryTypeSpinner();
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
                }
        }


        private void goToPreviousActivity() {
                super.onBackPressed();
        }


        private void showStartTimePickerDialog() {
                StartTimePickerFragment newFragment = new StartTimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "startTimePicker");
        }


        private void showEndTimePickerDialog() {
                EndTimePickerFragment newFragment = new EndTimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "endTimePicker");
        }


        private void saveChanges() {

        }

}
