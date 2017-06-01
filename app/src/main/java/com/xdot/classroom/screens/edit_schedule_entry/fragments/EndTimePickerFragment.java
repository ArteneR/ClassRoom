package com.xdot.classroom.screens.edit_schedule_entry.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.TimePicker;
import com.xdot.classroom.CommonFunctionalities;
import com.xdot.classroom.R;



public class EndTimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        private static String LOG_TAG = "EndTimePickerFragment";
        private Button btnStartTime;
        private Button btnEndTime;


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
                int timePickerTheme = TimePickerDialog.THEME_HOLO_DARK;
                boolean is24HourFormat = true;

                btnStartTime = (Button) getActivity().findViewById(R.id.btnStartTime);
                btnEndTime = (Button) getActivity().findViewById(R.id.btnEndTime);
                String currentEndTime = btnEndTime.getText().toString();
                int currentEndHour = CommonFunctionalities.extractHourFromTime(currentEndTime);
                int currentEndMinute = CommonFunctionalities.extractMinuteFromTime(currentEndTime);

                return new TimePickerDialog(getActivity(), timePickerTheme, this, currentEndHour, currentEndMinute, is24HourFormat);
        }


        public void onTimeSet(TimePicker view, int setEndHour, int setEndMinute) {
                String startTime = btnStartTime.getText().toString();
                int startHour = CommonFunctionalities.extractHourFromTime(startTime);
                int startMinute = CommonFunctionalities.extractMinuteFromTime(startTime);

                if (!isTimeIntervalValid(startHour, startMinute, setEndHour, setEndMinute)) {
                        CommonFunctionalities.displayShortToast("'End time' must be GREATER than the 'Start time'!", getContext());
                        return ;
                }

                String formattedTime = CommonFunctionalities.formatTime(setEndHour, setEndMinute);
                btnEndTime.setText(formattedTime);
        }


        private boolean isTimeIntervalValid(int startHour, int startMinute, int endHour, int endMinute) {
                if (startHour > endHour) {
                        return false;
                }

                if (startHour == endHour && startMinute >= endMinute) {
                        return false;
                }
                return true;
        }

}