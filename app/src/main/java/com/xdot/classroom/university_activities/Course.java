package com.xdot.classroom.university_activities;

import com.xdot.classroom.schedule.Room;
import com.xdot.classroom.schedule.Subject;
import com.xdot.classroom.schedule.Time;



public class Course extends UniversityActivity {
    private String backgroundColor = "#C9BE62";


    public Course(String id, Time startTime, Time endTime, Room room, Subject subject) {
        super(id, startTime, endTime, room, subject);
    }


    public Course(String id, Time startTime, Time endTime, Room room, Subject subject, boolean evenWeeksOnly, boolean oddWeeksOnly) {
        super(id, startTime, endTime, room, subject, evenWeeksOnly, oddWeeksOnly);
    }
}
