package com.xdot.classroom;



public class ScheduleBuilder {
    private String containerId;
    private int maxHoursInterval = 14;          /* eg. 08:00 -> 22:00 (14 hours) */
    private int intervalStartHour = 8;          /* Schedule will start from hours 08:00 */
    private int intervalStartMinute = 0;        /* Schedule will start from hours 08:00 */
    private int oneHourSlotSize = 36;           /* 1h - 36dp (how many dps does an hour occupy on the schedule) */
    private int smallestTimeUnit = 5;           /* 5min - 3dp */
    private int smallestTimeUnitSlotSize;       /* Will be 3dp for smallestTimeUnit==5min & oneHourSlotSize=36dp */



    public ScheduleBuilder(String containerId) {
            this.containerId = containerId;
            this.calculateSmallestTimeUnitSlotSize();
    }



    private void calculateSmallestTimeUnitSlotSize() {
            this.smallestTimeUnitSlotSize = ( this.oneHourSlotSize / ( 60 / this.smallestTimeUnit ) );
    }



    public void addScheduleEntry(String startTime, String endTime) {
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
