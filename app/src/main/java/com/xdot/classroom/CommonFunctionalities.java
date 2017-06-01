package com.xdot.classroom;

import android.content.Context;
import android.widget.Toast;



public class CommonFunctionalities {

        public static void displayShortToast(String message, Context context) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }


        public static void displayLongToast(String message, Context context) {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
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


        public static String singularToPlural(String singular) {
                String CONSONANTS = "bcdfghjklmnpqrstvwxz";

                switch (singular) {
                    case "Person":
                        return "People";
                    case "Trash":
                        return "Trash";
                    case "Life":
                        return "Lives";
                    case "Man":
                        return "Men";
                    case "Woman":
                        return "Women";
                    case "Child":
                        return "Children";
                    case "Foot":
                        return "Feet";
                    case "Tooth":
                        return "Teeth";
                    case "Dozen":
                        return "Dozen";
                    case "Hundred":
                        return "Hundred";
                    case "Thousand":
                        return "Thousand";
                    case "Million":
                        return "Million";
                    case "Datum":
                        return "Data";
                    case "Criterion":
                        return "Criteria";
                    case "Analysis":
                        return "Analyses";
                    case "Fungus":
                        return "Fungi";
                    case "Index":
                        return "Indices";
                    case "Matrix":
                        return "Matrices";
                    case "Settings":
                        return "Settings";
                    case "UserSettings":
                        return "UserSettings";
                    default:
                        // Handle ending with "o" (if preceeded by a consonant, end with -es, otherwise -s: Potatoes and Radios)
                        if (singular.endsWith("o") && CONSONANTS.contains(String.valueOf(singular.charAt(singular.length() - 2)))) {
                            return singular + "es";
                        }

                        // Handle ending with "y" (if preceeded by a consonant, end with -ies, otherwise -s: Companies and Trays)
                        if (singular.endsWith("y") && CONSONANTS.contains(String.valueOf(singular.charAt(singular.length() - 2)))) {
                            return singular.substring(0, singular.length() - 1) + "ies";
                        }

                        // Ends with a whistling sound: boxes, buzzes, churches, passes
                        if (singular.endsWith("s") || singular.endsWith("sh") || singular.endsWith("ch") || singular.endsWith("x") || singular.endsWith("z")) {
                            return singular + "es";
                        }
                        return singular + "s";
                }
        }

}
