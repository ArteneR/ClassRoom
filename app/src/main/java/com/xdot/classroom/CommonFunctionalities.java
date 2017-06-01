package com.xdot.classroom;

import android.content.Context;
import android.widget.Toast;



public class CommonFunctionalities {

        public static void displayShortToast(String message, Context context) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }


        public static String formatTime(int hour, int minute) {
                String hourFormatted = String.valueOf(hour);
                String minuteFormatted = String.valueOf(minute);

                if (hour < 10) {
                        hourFormatted = "0" + hourFormatted;
                }

                if (minute < 10) {
                        minuteFormatted = "0" + minuteFormatted;
                }

                return hourFormatted + ":" + minuteFormatted;
        }


        public static int extractHourFromTime(String time) {
                if (time.length() < 2) {
                    return -1;
                }
                String hour = time.substring(0, 2);
                return Integer.parseInt(hour);
        }


        public static int extractMinuteFromTime(String time) {
                if (time.length() < 5) {
                    return -1;
                }
                String minute = time.substring(3, 5);
                return Integer.parseInt(minute);
        }

}
