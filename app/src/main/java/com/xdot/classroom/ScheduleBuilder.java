package com.xdot.classroom;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.widget.RelativeLayout;



public class ScheduleBuilder {
    private static String LOG_TAG = "ScheduleBuilder";
    private String containerId;
    private int maxHoursInterval = 14;          /* eg. 08:00 -> 22:00 (14 hours) */
    private int intervalStartHour = 8;          /* Schedule will start from hours 08:00 */
    private int intervalStartMinute = 0;        /* Schedule will start from hours 08:00 */
    private int oneHourSlotSize = 36;           /* 1h - 36dp (how many dps does an hour occupy on the schedule) */
    private int smallestTimeUnit = 5;           /* 5min - 3dp */
    private int smallestTimeUnitSlotSize;       /* Will be 3dp for smallestTimeUnit==5min & oneHourSlotSize=36dp */
    private Context mContext;


    public ScheduleBuilder(String containerId, Context context) {
            this.containerId = containerId;
            this.mContext = context;
            this.calculateSmallestTimeUnitSlotSize();
            this.prepareScheduleContainer();
    }



    private void calculateSmallestTimeUnitSlotSize() {
            this.smallestTimeUnitSlotSize = ( this.oneHourSlotSize / ( 60 / this.smallestTimeUnit ) );
    }



    private void prepareScheduleContainer() {
            int scheduleContainerViewId = ((Activity) mContext).getResources().getIdentifier(this.containerId, "id", ((Activity) mContext).getPackageName());
//            RelativeLayout scheduleContainer = (RelativeLayout) ((Activity) mContext).findViewById(scheduleContainerViewId);
            RelativeLayout scheduleContainer = (RelativeLayout) ((Activity) mContext).findViewById(R.id.schedule_container);
        Log.d(LOG_TAG, "this.containerId: " + this.containerId);
        Log.d(LOG_TAG, "scheduleContainerViewId: " + scheduleContainerViewId);
        Log.d(LOG_TAG, "scheduleContainer: " + scheduleContainer);
            if(((RelativeLayout) scheduleContainer).getChildCount() > 0) {
                   ((RelativeLayout) scheduleContainer).removeAllViews();
            }
    }



    public void addScheduleEntry(String startTime, String endTime, String subject, String room, String building) {
            int startTimeHour = extractTimeHour(startTime);
            int startTimeMinute = extractTimeMinute(startTime);
            int endTimeHour = extractTimeHour(endTime);
            int endTimeMinute = extractTimeMinute(endTime);

            if (!isTimeIntervalValid(startTimeHour, startTimeMinute, endTimeHour, endTimeMinute)) {
                    return ;
            }

            int timeUntilScheduleEntryStartTime = calculateTimeBetween(intervalStartHour, intervalStartMinute, startTimeHour, startTimeMinute);
            int scheduleEntryDuration = calculateTimeBetween(startTimeHour, startTimeMinute, endTimeHour, endTimeMinute);     /* in minutes */

            int scheduleEntryMarginTop = ( ( timeUntilScheduleEntryStartTime / this.smallestTimeUnit ) * this.smallestTimeUnitSlotSize );
            int scheduleEntryHeight = ( ( scheduleEntryDuration / this.smallestTimeUnit ) * this.smallestTimeUnitSlotSize );

            Log.d(LOG_TAG, "scheduleEntryMarginTop: " + scheduleEntryMarginTop);
            Log.d(LOG_TAG, "scheduleEntryHeight: " + scheduleEntryHeight);



            RelativeLayout relativeLayout = new RelativeLayout(((Activity) mContext));
            relativeLayout.setBackgroundColor(Color.parseColor("#555555"));

            final float scale = mContext.getResources().getDisplayMetrics().density;
            int px = (int) (scheduleEntryHeight * scale + 0.5f);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, px);
            params.setMargins(0, scheduleEntryMarginTop, 0, 0);


        relativeLayout.setLayoutParams(params);


        int scheduleContainerViewId = ((Activity) mContext).getResources().getIdentifier(this.containerId, "id", ((Activity) mContext).getPackageName());
        RelativeLayout scheduleContainer = (RelativeLayout) ((Activity) mContext).findViewById(R.id.schedule_container);

        scheduleContainer.addView(relativeLayout);

        Log.d(LOG_TAG, "relativeLayout: " + relativeLayout);
        Log.d(LOG_TAG, "scale: " + scale);
        Log.d(LOG_TAG, "px: " + px);
        Log.d(LOG_TAG, "params: " + params);
        Log.d(LOG_TAG, "scheduleContainerViewId: " + scheduleContainerViewId);
        Log.d(LOG_TAG, "scheduleContainer: " + scheduleContainer);
    }



    private boolean isTimeIntervalValid(int startTimeHour, int startTimeMinute, int endTimeHour, int endTimeMinute) {
            if (!isHourValid(startTimeHour) || !isMinuteValid(startTimeMinute) ||
                !isHourValid(endTimeHour) || !isMinuteValid(endTimeMinute)) {
                    return false;
            }

            if (startTimeHour > endTimeHour) {
                    return false;
            }

            if (startTimeHour == endTimeHour && startTimeMinute >= endTimeMinute) {
                return false;
            }

            return true;
    }



    private boolean isHourValid(int hour) {
            if (hour >= 0 && hour <= 23) {
                    return true;
            }
            return false;
    }



    private boolean isMinuteValid(int minute) {
            if (minute >= 0 && minute <= 59) {
                    return true;
            }
            return false;
    }



    private String eliminateLeadingZero(String value) {
            String newValue = value;

            if (value.length() < 2) {
                    return newValue;
            }

            String firstCharacter = String.valueOf(value.charAt(0));
            if (firstCharacter.equals("0")) {
                    newValue = value.substring(1, value.length());
            }
            return newValue;
    }



    private int extractTimeHour(String time) {
            if (time.length() < 2) {
                    return -1;
            }
            String timeHour = time.substring(0, 2);
            timeHour = eliminateLeadingZero(timeHour);
            return Integer.parseInt(timeHour);
    }



    private int extractTimeMinute(String time) {
            if (time.length() < 5) {
                    return -1;
            }
            String timeMinute = time.substring(3, 5);
            timeMinute = eliminateLeadingZero(timeMinute);
            return Integer.parseInt(timeMinute);
    }



    private int calculateTimeBetween(int startTimeHour, int startTimeMinute, int endTimeHour, int endTimeMinute) {
            int hoursBetween = endTimeHour - startTimeHour;
            int minutesBetween = endTimeMinute - startTimeMinute;
            return ( hoursBetween * 60 ) + minutesBetween;
    }

}
