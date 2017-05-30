package com.xdot.classroom.university_activities;


public class Seminary extends UniversityActivity {
        private String backgroundColor = "#DAE8FC";


        /*
         * Do not delete!
         * Needed by Firebase.
         */
        public Seminary() {}


        public String getBackgroundColor() {
                return backgroundColor;
        }


        public Seminary(String id, String StartTime, String EndTime, String Room, String Building, String Subject) {
                super(id, StartTime, EndTime, Room, Building, Subject);
        }
}
