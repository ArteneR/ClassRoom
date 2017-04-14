package com.xdot.classroom.university_activities;

import com.xdot.classroom.Room;
import com.xdot.classroom.Subject;
import com.xdot.classroom.Time;



public class Lab extends UniversityActivity {
    private String backgroundColor = "#FFA62F";


    public Lab(String id, Time startTime, Time endTime, Room room, Subject subject) {
        super(id, startTime, endTime, room, subject);
    }


    public Lab(String id, Time startTime, Time endTime, Room room, Subject subject, boolean evenWeeksOnly, boolean oddWeeksOnly) {
        super(id, startTime, endTime, room, subject, evenWeeksOnly, oddWeeksOnly);
    }
}
