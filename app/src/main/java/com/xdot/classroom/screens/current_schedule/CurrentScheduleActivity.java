package com.xdot.classroom.screens.current_schedule;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.xdot.classroom.DataProvider;
import com.xdot.classroom.R;



public class CurrentScheduleActivity extends AppCompatActivity {
    private static String LOG_TAG = "CurrentScheduleActivity";
    private String activityTitle = "Current Schedule Name";
    private android.support.v7.app.ActionBar mActionBar;
    private CurrentScheduleCustomPagerAdapter mCustomPagerAdapter;
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_schedule);

        activateCustomActionBar();
        activateViewPager();
    }


    /*
     * Setup the custom action bar for navigation
     */
    private void activateCustomActionBar() {
        mActionBar = getSupportActionBar();

        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#BB266195")));
        mActionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#BB266195")));

        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.custom_actionbar_current_schedule, null);

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        setCustomActionBarTitle(this.activityTitle);
    }


    /*
     * Setup the View Pager
     */
    private void activateViewPager() {
        mCustomPagerAdapter = new CurrentScheduleCustomPagerAdapter(getSupportFragmentManager(), this);

        mViewPager = (ViewPager) findViewById(R.id.current_schedule_pager);
        mViewPager.setAdapter(mCustomPagerAdapter);
    }


    private void setCustomActionBarTitle(String newTitle) {
        TextView tvActivityTitle = (TextView) findViewById(R.id.tvActivityTitle);
        tvActivityTitle.setText(newTitle);
    }


    /*
     * Handle user click events
     */
    public void clicked(View view) {
        Log.d(LOG_TAG, "Button clicked!");

        switch (view.getId()) {
            case R.id.ivLeftActionbarButton:
                Log.d(LOG_TAG, "Button: Back");
                break;

            case R.id.ivRightActionbarButton:
                Log.d(LOG_TAG, "Button: Add Course");
                break;
        }
    }

}