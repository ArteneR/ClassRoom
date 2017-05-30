package com.xdot.classroom.university_activities;


public class Course extends UniversityActivity {
        private String backgroundColor = "#99FF99";


        /*
         * Do not delete!
         * Needed by Firebase.
         */
        public Course() {}


        public String getBackgroundColor() {
                return backgroundColor;
        }


        public Course(String id, String StartTime, String EndTime, String Room, String Building, String Subject) {
                super(id, StartTime, EndTime, Room, Building, Subject);
        }
}
