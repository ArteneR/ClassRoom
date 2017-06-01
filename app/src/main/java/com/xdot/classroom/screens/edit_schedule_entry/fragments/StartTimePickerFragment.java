package com.xdot.classroom.screens.edit_schedule_entry.fragments;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TimePicker;
import com.xdot.classroom.CommonFunctionalities;
import com.xdot.classroom.R;
import java.util.Calendar;



public class StartTimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                int timePickerTheme = TimePickerDialog.THEME_HOLO_DARK;
                boolean is24HourFormat = true;

                return new TimePickerDialog(getActivity(), timePickerTheme, this, hour, minute, is24HourFormat);
        }


        public void onTimeSet(TimePicker view, int setHour, int setMinute) {
                Button btnStartTime = (Button) getActivity().findViewById(R.id.btnStartTime);
                String formattedTime = CommonFunctionalities.formatTime(setHour, setMinute);
                btnStartTime.setText(formattedTime);
        }

}