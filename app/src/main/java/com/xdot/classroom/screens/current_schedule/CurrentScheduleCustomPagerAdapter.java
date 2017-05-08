package com.xdot.classroom.screens.current_schedule;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;



public class CurrentScheduleCustomPagerAdapter extends FragmentPagerAdapter {
    private static String LOG_TAG = "CurrentScheduleCustomPagerAdapter";
    protected Context mContext;


    public CurrentScheduleCustomPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }


    @Override
    public Fragment getItem(int position) {
        Log.d(LOG_TAG, "-----------------GET ITEM: " + position);
        Fragment fragment;
        String fragmentLayoutId = "";
        String scheduleContainerId = "";


        switch(position) {
            case 0:
                fragment = new CurrentScheduleFragmentMonday();
                fragmentLayoutId = "fragment_current_schedule_monday";
                scheduleContainerId = "schedule_container_monday";
                break;

            case 1:
                fragment = new CurrentScheduleFragmentTuesday();
                fragmentLayoutId = "fragment_current_schedule_tuesday";
                scheduleContainerId = "schedule_container_tuesday";
                break;

            case 2:
                fragment = new CurrentScheduleFragmentWednesday();
                fragmentLayoutId = "fragment_current_schedule_wednesday";
                scheduleContainerId = "schedule_container_wednesday";
                break;

            case 3:
                fragment = new CurrentScheduleFragmentThursday();
                fragmentLayoutId = "fragment_current_schedule_thursday";
                scheduleContainerId = "schedule_container_thursday";
                break;

            case 4:
                fragment = new CurrentScheduleFragmentFriday();
                fragmentLayoutId = "fragment_current_schedule_friday";
                scheduleContainerId = "schedule_container_friday";
                break;

            case 5:
                fragment = new CurrentScheduleFragmentSaturday();
                fragmentLayoutId = "fragment_current_schedule_saturday";
                scheduleContainerId = "schedule_container_saturday";
                break;

            case 6:
                fragment = new CurrentScheduleFragmentSunday();
                fragmentLayoutId = "fragment_current_schedule_sunday";
                scheduleContainerId = "schedule_container_sunday";
                break;

            default:
                fragment = new CurrentScheduleFragmentMonday();
                fragmentLayoutId = "fragment_current_schedule_monday";
                scheduleContainerId = "schedule_container_monday";
        }

        Bundle args = new Bundle();
        args.putInt("day_of_week", position);
        args.putString("fragment_id", fragmentLayoutId);
        args.putString("schedule_container_id", scheduleContainerId);

        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public int getCount() {
        return 7;
    }

}