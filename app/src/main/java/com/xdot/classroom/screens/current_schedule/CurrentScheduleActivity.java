package com.xdot.classroom.screens.current_schedule;

import android.app.Activity;
import android.content.Context;
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
import com.xdot.classroom.DayOfWeek;
import com.xdot.classroom.R;



public class CurrentScheduleActivity extends AppCompatActivity {
        private static String LOG_TAG = "CurrentScheduleActivity";
        private String activityTitle = "Current Schedule Name";
        private android.support.v7.app.ActionBar mActionBar;
        private CurrentScheduleCustomPagerAdapter mCustomPagerAdapter;
        private ViewPager mViewPager;
        private Context mContext;
        private final int DEFAULT_SCHEDULE_INDEX = 0;
        private DataProvider dataProvider;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                mContext = this;
                setContentView(R.layout.activity_current_schedule);

                initializeDataProviderModule();

                int scheduleIndex = getIntent().getIntExtra("SELECTED_SCHEDULE_INDEX", DEFAULT_SCHEDULE_INDEX);
                Log.d(LOG_TAG, "Selected schedule index: " + scheduleIndex);
                dataProvider.setCurrentScheduleIndex(scheduleIndex);

                activateCustomActionBar();
                activateViewPager();
        }



        private void initializeDataProviderModule() {
                dataProvider = (DataProvider) (((Activity) mContext)).getApplication();
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
                mViewPager.setOffscreenPageLimit(2);

                setupOnPageChangeListener();
        }



        private void setupOnPageChangeListener() {
                mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            String dayOfWeek = DayOfWeek.values()[position].toString();
                            TextView v = (TextView) ((Activity)mContext).findViewById(R.id.tvDayOfWeek);
                            v.setText(dayOfWeek);
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                });
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
                        goToPreviousActivity();
                        break;

                    case R.id.ivRightActionbarButton:
                        Log.d(LOG_TAG, "Button: Add Course");
                        break;
                }
        }



        private void goToPreviousActivity() {
                super.onBackPressed();
        }

}
