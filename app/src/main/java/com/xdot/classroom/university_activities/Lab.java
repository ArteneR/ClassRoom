package com.xdot.classroom.university_activities;


public class Lab extends UniversityActivity {
        private String backgroundColor = "#FFE6CC";


        /*
         * Do not delete!
         * Needed by Firebase.
         */
        public Lab() {}


        public String getBackgroundColor() {
                return backgroundColor;
        }


        public Lab(String id, String StartTime, String EndTime, String Room, String Building, String Subject) {
                super(id, StartTime, EndTime, Room, Building, Subject);
        }
}
