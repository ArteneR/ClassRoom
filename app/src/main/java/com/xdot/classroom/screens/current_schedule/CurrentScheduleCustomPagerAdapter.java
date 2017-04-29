package com.xdot.classroom.screens.current_schedule;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.xdot.classroom.DayOfWeek;


public class CurrentScheduleCustomPagerAdapter extends FragmentPagerAdapter {
    private static String LOG_TAG = "CurrentScheduleCustomPagerAdapter";
    protected Context mContext;


    public CurrentScheduleCustomPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }



    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new CurrentScheduleDemoFragment();

        Bundle args = new Bundle();
        args.putInt("day_of_week", position);

        Log.d(LOG_TAG, "day_of_week: " + position);

        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public int getCount() {
        return 7;
    }

}