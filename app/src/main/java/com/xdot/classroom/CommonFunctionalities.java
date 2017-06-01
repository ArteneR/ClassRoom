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


        public static String pluralToSingular(String plural) {
                String singular = "";
                int pluralLength = plural.length();

                if (pluralLength <= 0) {
                    return "";
                }

                if (pluralLength >= 3) {
                    String lastThreeLetters = plural.substring(pluralLength-3, pluralLength);
                    if (lastThreeLetters.equals("ies")) {
                        singular = plural.substring(0, pluralLength-3) + "y";
                        return singular;
                    }
                }

                String lastLetter = plural.substring(pluralLength-1, pluralLength);
                if (lastLetter.equals("s")) {
                    singular = plural.substring(0, pluralLength-1);
                }

                return singular;
        }

}
