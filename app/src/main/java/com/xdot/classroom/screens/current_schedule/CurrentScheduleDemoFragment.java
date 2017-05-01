package com.xdot.classroom.screens.current_schedule;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.xdot.classroom.DataProvider;
import com.xdot.classroom.DayOfWeek;
import com.xdot.classroom.R;



public class CurrentScheduleDemoFragment extends Fragment {
    private static String LOG_TAG = "CurrentScheduleDemoFragment";
    private DataProvider dataProvider;
    private Context mContext;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_current_schedule_demo, container, false);

        Bundle args = getArguments();
        int dayOfWeekIndex = args.getInt("day_of_week");
        String dayOfWeek = DayOfWeek.values()[dayOfWeekIndex].toString();
        ((TextView) rootView.findViewById(R.id.text)).setText(dayOfWeek);

        mContext = getContext();
        initializeDataProviderModule();

        Log.d(LOG_TAG, "DoW: " + dayOfWeekIndex + " - " + DayOfWeek.values()[dayOfWeekIndex]);
        dataProvider.printSchedules();

        // create and show the schedule


        return rootView;
    }


    private void initializeDataProviderModule() {
        Log.d(LOG_TAG, "Initializing Data Provider");
        dataProvider = (DataProvider) (((Activity) mContext)).getApplication();
        dataProvider.init();
    }
}