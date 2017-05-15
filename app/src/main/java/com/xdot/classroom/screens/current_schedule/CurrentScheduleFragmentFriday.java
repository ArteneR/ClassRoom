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
import com.xdot.classroom.schedule.Schedule;
import com.xdot.classroom.university_activities.UniversityActivity;
import java.util.List;



public class CurrentScheduleFragmentFriday extends Fragment {
        private static String LOG_TAG = "CurrentScheduleFragmentFriday";
        private DataProvider dataProvider;
        private Context mContext;
        private Schedule currentSchedule;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                View rootView = inflater.inflate(R.layout.fragment_current_schedule_friday, container, false);
                Log.d(LOG_TAG, "-----------------------ON FRAGMENT CREATE");

                mContext = getContext();
                initializeDataProviderModule();

                int currentScheduleIndex = dataProvider.getCurrentScheduleIndex();
                currentSchedule = dataProvider.getSchedule("" + currentScheduleIndex);

                return rootView;
        }


        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
                super.onActivityCreated(savedInstanceState);

                Log.d(LOG_TAG, "-----------------------ON ACTIVITY CREATE");

                // create and show the schedule
                ScheduleBuilder scheduleBuilder = new ScheduleBuilder("schedule_container_friday", mContext);
//                scheduleBuilder.addScheduleEntry("08:30", "10:00", "Programming", "B514", "Corpul B");

//                scheduleBuilder.addScheduleEntry("11:00", "14:00", "Mathematics", "B514", "Corpul B");

//                int currentScheduleIndex = dataProvider.getCurrentScheduleIndex();
//                currentSchedule = dataProvider.getSchedule("" + currentScheduleIndex);
                List<UniversityActivity> univActivities = currentSchedule.getUniversityActivitiesOnDay("Friday");

                for (int i = 0; i < univActivities.size(); i++) {
                        Log.d(LOG_TAG, "UnivActivity: " + univActivities.get(i));
                        scheduleBuilder.addScheduleEntry(univActivities.get(i).getStartTime().toString(),
                                                         univActivities.get(i).getEndTime().toString(),
                                                         univActivities.get(i).getSubject().toString(),
                                                         univActivities.get(i).getRoom().toString(),
                                                         univActivities.get(i).getBuilding().toString());
                }
        }


        private void initializeDataProviderModule() {
                Log.d(LOG_TAG, "Initializing Data Provider");
                dataProvider = (DataProvider) (((Activity) mContext)).getApplication();
                dataProvider.init();
        }
}