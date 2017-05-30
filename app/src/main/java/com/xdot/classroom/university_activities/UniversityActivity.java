package com.xdot.classroom.university_activities;

import android.util.Log;


public class UniversityActivity {
        public String StartTime;
        public String EndTime;
        public String Room;
        public String Building;
        public String Subject;
        private String id;
        private String backgroundColor = "#C9BE62";
        private boolean evenWeeksOnly = false;
        private boolean oddWeeksOnly = false;
        private static String LOG_TAG = "UniversityActivity";


        /*
         * Do not delete!
         * Needed by Firebase.
         */
        public UniversityActivity() {}


        public UniversityActivity(String id, String StartTime, String EndTime, String Room, String Building, String Subject) {
                this.id = id;
                this.StartTime = StartTime;
                this.EndTime = EndTime;
                this.Room = Room;
                this.Building = Building;
                this.Subject = Subject;
        }


        public void setId(String id) {
                this.id = id;
        }


        public String getBackgroundColor() {
                return backgroundColor;
        }


        public void printActivity() {
                Log.d(LOG_TAG, this.Subject + " - " + this.Room + " : " + this.Building + " (" + this.StartTime + " - " + this.EndTime + ")");
        }


        @Override
        public String toString() {
                return "[id: " + this.id + "]" + this.Subject + " - " + this.EndTime + ": " + this.Subject + " (" + this.Room + " - " + this.Building + ")";
        }
}
