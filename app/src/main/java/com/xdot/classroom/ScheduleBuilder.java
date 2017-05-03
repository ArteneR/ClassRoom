package com.xdot.classroom;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;


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
    private float scale;


    public ScheduleBuilder(String containerId, Context context) {
            this.containerId = containerId;
            this.mContext = context;
            this.calculateSmallestTimeUnitSlotSize();
            this.findDisplayScale();
            this.prepareScheduleContainer();
    }



    private void calculateSmallestTimeUnitSlotSize() {
            this.smallestTimeUnitSlotSize = ( this.oneHourSlotSize / ( 60 / this.smallestTimeUnit ) );
    }



    private void findDisplayScale() {
            this.scale = this.mContext.getResources().getDisplayMetrics().density;
    }



    private void prepareScheduleContainer() {
            int scheduleContainerViewId = ((Activity) mContext).getResources().getIdentifier(this.containerId, "id", ((Activity) mContext).getPackageName());
            RelativeLayout scheduleContainer = (RelativeLayout) ((Activity) mContext).findViewById(scheduleContainerViewId);

            if(((RelativeLayout) scheduleContainer).getChildCount() > 0) {
                   ((RelativeLayout) scheduleContainer).removeAllViews();
            }
    }



    public void addScheduleEntry(String startTime, String endTime, String subject, String room, String building) {
            int startTimeHour = extractTimeHour(startTime);
            int startTimeMinute = extractTimeMinute(startTime);
            int endTimeHour = extractTimeHour(endTime);
            int endTimeMinute = extractTimeMinute(endTime);

            Map scheduleEntryData = createScheduleEntryDataMap(startTime, endTime, subject, room, building);

            if (!isTimeIntervalValid(startTimeHour, startTimeMinute, endTimeHour, endTimeMinute)) {
                    return ;
            }

            int timeUntilScheduleEntryStartTime = calculateTimeBetween(intervalStartHour, intervalStartMinute, startTimeHour, startTimeMinute);
            int scheduleEntryDuration = calculateTimeBetween(startTimeHour, startTimeMinute, endTimeHour, endTimeMinute);     /* in minutes */

            int scheduleEntryMarginTop = ( ( timeUntilScheduleEntryStartTime / this.smallestTimeUnit ) * this.smallestTimeUnitSlotSize );
            int scheduleEntryHeight = ( ( scheduleEntryDuration / this.smallestTimeUnit ) * this.smallestTimeUnitSlotSize );

            Log.d(LOG_TAG, "scheduleEntryMarginTop: " + scheduleEntryMarginTop);
            Log.d(LOG_TAG, "scheduleEntryHeight: " + scheduleEntryHeight);

            RelativeLayout relativeLayout = makeScheduleEntryLayout("#fff2cc", scheduleEntryMarginTop, scheduleEntryHeight, scheduleEntryData);

            addScheduleEntryLayout(relativeLayout);
    }



    private Map createScheduleEntryDataMap(String startTime, String endTime, String subject, String room, String building) {
            Map scheduleEntryData = new HashMap();
            scheduleEntryData.put("startTime", startTime);
            scheduleEntryData.put("endTime", endTime);
            scheduleEntryData.put("subject", subject);
            scheduleEntryData.put("room", room);
            scheduleEntryData.put("building", building);
            return scheduleEntryData;
    }



    private RelativeLayout makeScheduleEntryLayout(String scheduleEntryColor, int scheduleEntryMarginTop, int scheduleEntryHeight, Map scheduleEntryData) {
            RelativeLayout relativeLayout = new RelativeLayout((Activity) this.mContext);
            relativeLayout.setBackgroundColor(Color.parseColor(scheduleEntryColor));

            int scheduleEntryHeightInPx = convertDpToPx(scheduleEntryHeight);
            int scheduleEntryMarginTopInPx = convertDpToPx(scheduleEntryMarginTop);

            // Set width and height
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, scheduleEntryHeightInPx);

            // Set margins
            params.setMargins(0, scheduleEntryMarginTopInPx, 0, 0);

            relativeLayout.setLayoutParams(params);

            // Setting the elements inside the schedule entry
            TextView startTimeLayout = makeStartTimeLayout(scheduleEntryData.get("startTime").toString());

            int endTimeLayoutHeight = 16;       /* in dp */
            TextView endTimeLayout = makeEndTimeLayout(scheduleEntryData.get("endTime").toString(), scheduleEntryHeight - endTimeLayoutHeight);

            LinearLayout subjectNameLayout = makeSubjectNameLayout(scheduleEntryData.get("subject").toString());

            LinearLayout locationLayout = makeLocationLayout(scheduleEntryData.get("room").toString(), scheduleEntryData.get("building").toString());

            relativeLayout.addView(startTimeLayout);
            relativeLayout.addView(endTimeLayout);
            relativeLayout.addView(subjectNameLayout);
//            relativeLayout.addView(locationLayout);

            return relativeLayout;
    }



    private int convertDpToPx(int dpValue) {
            int pxValue = (int) (dpValue * this.scale + 0.5f);
            return pxValue;
    }



    private TextView makeStartTimeLayout(String startTime) {
            TextView startTimeLayout = new TextView((Activity) this.mContext);

            // Set text
            startTimeLayout.setText(startTime);

            // Set width and height
            int startTimeLayoutWidthInPx = convertDpToPx(50);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(startTimeLayoutWidthInPx, RelativeLayout.LayoutParams.WRAP_CONTENT);

            // Set margins
            params.setMargins(0, 0, 0, 0);

            // Set text alignment
            startTimeLayout.setGravity(Gravity.CENTER);

            startTimeLayout.setLayoutParams(params);

            return startTimeLayout;
    }



    private TextView makeEndTimeLayout(String endTime, int marginTop) {
            TextView endTimeLayout = new TextView((Activity) this.mContext);

            // Set text
            endTimeLayout.setText(endTime);

            // Set width and height
            int endTimeLayoutWidthInPx = convertDpToPx(50);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(endTimeLayoutWidthInPx, RelativeLayout.LayoutParams.WRAP_CONTENT);

            // Set margins
            int marginTopInPx = convertDpToPx(marginTop);
            params.setMargins(0, marginTopInPx, 0, 0);

            // Set text alignment
            endTimeLayout.setGravity(Gravity.CENTER);

            endTimeLayout.setLayoutParams(params);

            return endTimeLayout;
    }



    private LinearLayout makeSubjectNameLayout(String subject) {
            String color = "#FF0000";

            LinearLayout linearLayout = new LinearLayout((Activity) this.mContext);
            linearLayout.setBackgroundColor(Color.parseColor(color));

            // Set text alignment
            linearLayout.setGravity(Gravity.CENTER);

            int subjectNameLayoutHeightInPx = convertDpToPx(18);

            // Set width and height
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, subjectNameLayoutHeightInPx);

            // Set margins
            int marginTopInPx = convertDpToPx(3);
            int marginLeftInPx = convertDpToPx(60);
            int marginRightInPx = convertDpToPx(10);
            params.setMargins(marginLeftInPx, marginTopInPx, marginRightInPx, 0);

            // Set padding
            int paddingRightInPx = convertDpToPx(40);
            linearLayout.setPadding(0, 0, paddingRightInPx, 0);

            // Set the text layout
            TextView textViewLayout = makeSubjectTextViewLayout(subject);

            linearLayout.addView(textViewLayout);

            linearLayout.setLayoutParams(params);

            return linearLayout;
    }



    private TextView makeSubjectTextViewLayout(String subject) {
            TextView subjectTextViewLayout = new TextView((Activity) this.mContext);

            // Set text alignment
            subjectTextViewLayout.setGravity(Gravity.CENTER);

            // Set width and height
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);

            // Set text
            subjectTextViewLayout.setText(subject);

            // Set text bold
            subjectTextViewLayout.setTypeface(null, Typeface.BOLD);

            subjectTextViewLayout.setLayoutParams(params);

            return subjectTextViewLayout;
    }



    private LinearLayout makeLocationLayout(String room, String building) {
            return null;
    }



    private void addScheduleEntryLayout(RelativeLayout relativeLayout) {
            int scheduleContainerViewId = ((Activity) mContext).getResources().getIdentifier(this.containerId, "id", ((Activity) mContext).getPackageName());
            RelativeLayout scheduleContainer = (RelativeLayout) ((Activity) mContext).findViewById(scheduleContainerViewId);

            scheduleContainer.addView(relativeLayout);
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
