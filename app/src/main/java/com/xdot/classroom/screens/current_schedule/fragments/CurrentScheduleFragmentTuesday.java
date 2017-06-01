package com.xdot.classroom.screens.current_schedule.fragments;

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
import com.xdot.classroom.schedule.Schedule;
import com.xdot.classroom.university_activities.UniversityActivity;
import java.util.List;



public class CurrentScheduleFragmentTuesday extends Fragment {
        private static String LOG_TAG = "CurrentScheduleFragmentTuesday";
        private DataProvider dataProvider;
        private Context mContext;
        private Schedule currentSchedule;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                View rootView = inflater.inflate(R.layout.fragment_current_schedule_tuesday, container, false);
                Log.d(LOG_TAG, "-----------------------ON FRAGMENT CREATE");

                mContext = getContext();
                initializeDataProviderModule();

                currentSchedule = dataProvider.getCurrentSchedule();

                return rootView;
        }


        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
                super.onActivityCreated(savedInstanceState);

                // create and show the schedule
                ScheduleBuilder scheduleBuilder = new ScheduleBuilder("schedule_container_tuesday", mContext);
                List<UniversityActivity> univActivities = currentSchedule.getUniversityActivitiesOnDay("Tuesday");

                for (int i = 0; i < univActivities.size(); i++) {
                        Log.d(LOG_TAG, "UnivActivity: " + univActivities.get(i));
                        scheduleBuilder.addScheduleEntry(univActivities.get(i).StartTime,
                                                         univActivities.get(i).EndTime,
                                                         univActivities.get(i).Subject,
                                                         univActivities.get(i).Room,
                                                         univActivities.get(i).Building,
                                                         univActivities.get(i).getBackgroundColor());
                }
        }


        private void initializeDataProviderModule() {
                Log.d(LOG_TAG, "Initializing Data Provider");
                dataProvider = (DataProvider) (((Activity) mContext)).getApplication();
        }
}