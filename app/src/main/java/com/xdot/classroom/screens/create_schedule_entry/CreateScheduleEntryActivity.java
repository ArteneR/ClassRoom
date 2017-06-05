package com.xdot.classroom.screens.create_schedule_entry;

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
import com.xdot.classroom.R;



public class CreateScheduleEntryActivity extends AppCompatActivity {
        private static String LOG_TAG = "CodeTemplateActivity";
        private String selectedDayOfTheWeek;
        private Spinner spinnerDayOfTheWeek;
        private Spinner spinnerEntryType;
        private EditText etSubjectName;
        private Button btnStartTime;
        private Button btnEndTime;
        private EditText etBuildingName;
        private EditText etRoomName;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_create_schedule_entry);

                selectedDayOfTheWeek = getIntent().getStringExtra("selected_day_of_week");

                initializeUIElements();

                activateCustomActionBar();
                activateDayOfTheWeekSpinner();
                activateEntryTypeSpinner();

                fillElementsWithData();
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

                }
        }


        private void goToPreviousActivity() {
                super.onBackPressed();
        }

}
