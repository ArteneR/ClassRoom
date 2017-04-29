package com.xdot.classroom.screens.current_schedule;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;




public class CurrentScheduleCustomPagerAdapter extends FragmentPagerAdapter {
    protected Context mContext;


    public CurrentScheduleCustomPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }


    @Override
    // This method returns the fragment associated with
    // the specified position.
    //
    // It is called when the Adapter needs a fragment
    // and it does not exists.
    public Fragment getItem(int position) {
        Fragment fragment = new CurrentScheduleDemoFragment();

        Bundle args = new Bundle();
        args.putInt("page_position", position + 1);

        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public int getCount() {
        return 3;
    }

}