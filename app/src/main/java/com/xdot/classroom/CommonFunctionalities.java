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

}
