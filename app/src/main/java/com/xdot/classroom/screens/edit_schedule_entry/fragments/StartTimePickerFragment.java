package com.xdot.classroom.screens.edit_schedule_entry.fragments;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TimePicker;
import com.xdot.classroom.CommonFunctionalities;
import com.xdot.classroom.R;



public class StartTimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        private static String LOG_TAG = "StartTimePickerFragment";
        private Button btnStartTime;


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
                int timePickerTheme = TimePickerDialog.THEME_HOLO_DARK;
                boolean is24HourFormat = true;

                btnStartTime = (Button) getActivity().findViewById(R.id.btnStartTime);
                String currentStartTime = btnStartTime.getText().toString();
                int currentStartHour = CommonFunctionalities.extractHourFromTime(currentStartTime);
                int currentStartMinute = CommonFunctionalities.extractMinuteFromTime(currentStartTime);

                return new TimePickerDialog(getActivity(), timePickerTheme, this, currentStartHour, currentStartMinute, is24HourFormat);
        }


        public void onTimeSet(TimePicker view, int setHour, int setMinute) {
                String formattedTime = CommonFunctionalities.formatTime(setHour, setMinute);
                btnStartTime.setText(formattedTime);
        }

}