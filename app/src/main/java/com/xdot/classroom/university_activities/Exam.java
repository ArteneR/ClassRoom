package com.xdot.classroom.university_activities;

import com.xdot.classroom.schedule.Building;
import com.xdot.classroom.schedule.Room;
import com.xdot.classroom.schedule.Subject;
import com.xdot.classroom.schedule.Time;



public class Exam extends UniversityActivity {
        private String backgroundColor = "#E77471";


        public Exam(String id, Time startTime, Time endTime, Room room, Building building, Subject subject) {
                super(id, startTime, endTime, room, building, subject);
        }


        public Exam(String id, Time startTime, Time endTime, Room room, Building building, Subject subject, boolean evenWeeksOnly, boolean oddWeeksOnly) {
                super(id, startTime, endTime, room, building, subject, evenWeeksOnly, oddWeeksOnly);
        }
}
