package com.xdot.classroom.screens.current_schedule;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.widget.TextView;
import com.xdot.classroom.DayOfWeek;
import com.xdot.classroom.R;



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
        Fragment fragment = new CurrentScheduleDemoFragment();

        Bundle args = new Bundle();
        args.putInt("day_of_week", position);

//        String dayOfWeek = DayOfWeek.values()[position].toString();
//        TextView v = (TextView) ((Activity)mContext).findViewById(R.id.tvDayOfWeek);
//        v.setText(dayOfWeek);

        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public int getCount() {
        return 7;
    }

}