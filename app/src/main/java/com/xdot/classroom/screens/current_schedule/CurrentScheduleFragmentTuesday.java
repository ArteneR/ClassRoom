package com.xdot.classroom.screens.current_schedule;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.xdot.classroom.DataProvider;
import com.xdot.classroom.R;
import com.xdot.classroom.ScheduleBuilder;



public class CurrentScheduleFragmentTuesday extends Fragment {
        private static String LOG_TAG = "CurrentScheduleFragmentTuesday";
        private DataProvider dataProvider;
        private Context mContext;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                View rootView = inflater.inflate(R.layout.fragment_current_schedule_tuesday, container, false);
                Log.d(LOG_TAG, "-----------------------ON FRAGMENT CREATE");

                mContext = getContext();
                initializeDataProviderModule();

                return rootView;
        }


        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
                super.onActivityCreated(savedInstanceState);

                Log.d(LOG_TAG, "-----------------------ON ACTIVITY CREATE");

                // create and show the schedule
                ScheduleBuilder scheduleBuilder = new ScheduleBuilder("schedule_container_tuesday", mContext);
                scheduleBuilder.addScheduleEntry("10:30", "14:00", "Programming", "B514", "Corpul B");

                scheduleBuilder.addScheduleEntry("15:00", "16:45", "Mathematics", "B514", "Corpul B");
        }


        private void initializeDataProviderModule() {
                Log.d(LOG_TAG, "Initializing Data Provider");
                dataProvider = (DataProvider) (((Activity) mContext)).getApplication();
                dataProvider.init();
        }
}