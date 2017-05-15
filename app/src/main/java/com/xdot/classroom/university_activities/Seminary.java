package com.xdot.classroom.university_activities;

import com.xdot.classroom.schedule.Building;
import com.xdot.classroom.schedule.Room;
import com.xdot.classroom.schedule.Subject;
import com.xdot.classroom.schedule.Time;



public class Seminary extends UniversityActivity {
        private String backgroundColor = "#FFF8C6";


        public Seminary(String id, Time startTime, Time endTime, Room room, Building building, Subject subject) {
                super(id, startTime, endTime, room, building, subject);
        }


        public Seminary(String id, Time startTime, Time endTime, Room room, Building building, Subject subject, boolean evenWeeksOnly, boolean oddWeeksOnly) {
                super(id, startTime, endTime, room, building, subject, evenWeeksOnly, oddWeeksOnly);
        }
}
