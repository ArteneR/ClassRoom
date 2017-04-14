package com.xdot.classroom.university_activities;

import com.xdot.classroom.Room;
import com.xdot.classroom.Subject;
import com.xdot.classroom.Time;



public class Seminary extends UniversityActivity {
    private String backgroundColor = "#FFF8C6";


    public Seminary(String id, Time startTime, Time endTime, Room room, Subject subject) {
        super(id, startTime, endTime, room, subject);
    }


    public Seminary(String id, Time startTime, Time endTime, Room room, Subject subject, boolean evenWeeksOnly, boolean oddWeeksOnly) {
        super(id, startTime, endTime, room, subject, evenWeeksOnly, oddWeeksOnly);
    }
}
