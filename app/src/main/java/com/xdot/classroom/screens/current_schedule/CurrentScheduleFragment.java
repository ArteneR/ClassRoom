package com.xdot.classroom.screens.current_schedule;

import android.annotation.SuppressLint;
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

import java.lang.reflect.Type;


public class CurrentScheduleFragment extends Fragment {
    private static String LOG_TAG = "CurrentScheduleFragment";
    private DataProvider dataProvider;
    private Context mContext;
    private String fragmentId;
    private String scheduleContainerId;
    private String dayOfWeek;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.fragmentId = getArguments().getString("fragment_id");
        this.scheduleContainerId = getArguments().getString("schedule_container_id");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.fragment_current_schedule_friday, container, false);
        Log.d(LOG_TAG, "-----------------------ON FRAGMENT CREATE");

        mContext = getContext();

        int fragmentLayoutId = mContext.getResources().getIdentifier(fragmentId, "layout", mContext.getPackageName());
        View rootView = inflater.inflate(fragmentLayoutId, container, false);

        initializeDataProviderModule();

        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(LOG_TAG, "-----------------------ON ACTIVITY CREATE");

        // create and show the schedule
        ScheduleBuilder scheduleBuilder = new ScheduleBuilder(scheduleContainerId, mContext);

//        ScheduleBuilder scheduleBuilder = new ScheduleBuilder("schedule_container_friday", mContext);
        scheduleBuilder.addScheduleEntry("08:30", "10:00", "Programming", "B514", "Corpul B");

        scheduleBuilder.addScheduleEntry("11:00", "14:00", "Mathematics", "B514", "Corpul B");
    }


    private void initializeDataProviderModule() {
        Log.d(LOG_TAG, "Initializing Data Provider");
        dataProvider = (DataProvider) (((Activity) mContext)).getApplication();
        dataProvider.init();
    }
}