package com.xdot.classroom.university_activities;


public class Exam extends UniversityActivity {
        private String backgroundColor = "#E77471";


        /*
         * Do not delete!
         * Needed by Firebase.
         */
        public Exam() {}


        public String getBackgroundColor() {
                return backgroundColor;
        }


        public Exam(String id, String StartTime, String EndTime, String Room, String Building, String Subject) {
                super(id, StartTime, EndTime, Room, Building, Subject);
        }
}
