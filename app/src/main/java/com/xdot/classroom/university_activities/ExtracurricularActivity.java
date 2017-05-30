package com.xdot.classroom.university_activities;


public class ExtracurricularActivity  extends UniversityActivity {
        private String backgroundColor = "#E1D5E7";


        /*
         * Do not delete!
         * Needed by Firebase.
         */
        public ExtracurricularActivity() {}


        public String getBackgroundColor() {
                return backgroundColor;
        }


        public ExtracurricularActivity(String id, String StartTime, String EndTime, String Room, String Building, String Subject) {
                super(id, StartTime, EndTime, Room, Building, Subject);
        }
}
