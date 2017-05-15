package com.xdot.classroom.university_activities;

import android.util.Log;
import com.xdot.classroom.schedule.Building;
import com.xdot.classroom.schedule.Room;
import com.xdot.classroom.schedule.Subject;
import com.xdot.classroom.schedule.Time;



public class UniversityActivity {
        private String id;
        private Time startTime;
        private Time endTime;
        private Subject subject;
        private Room room;
        private Building building;
        private boolean evenWeeksOnly = false;
        private boolean oddWeeksOnly = false;
        private static String LOG_TAG = "UniversityActivity";


        public UniversityActivity(String id, Time startTime, Time endTime, Room room, Building building, Subject subject) {
                this.id = id;
                this.startTime = startTime;
                this.endTime = endTime;
                this.room = room;
                this.building = building;
                this.subject = subject;
        }


        public UniversityActivity(String id, Time startTime, Time endTime, Room room, Building building, Subject subject, boolean evenWeeksOnly, boolean oddWeeksOnly) {
                this.id = id;
                this.startTime = startTime;
                this.endTime = endTime;
                this.room = room;
                this.building = building;
                this.subject = subject;
                this.evenWeeksOnly = evenWeeksOnly;
                this.oddWeeksOnly = oddWeeksOnly;
        }


        public void printActivity() {
                Log.d(LOG_TAG, this.subject.getName() + " - " + this.room.getName() + " : " + this.building.getName() + " (" + this.startTime.getTimeString() + " - " + this.endTime.getTimeString() + ")");
        }


        public Time getStartTime() {
                return startTime;
        }


        public Time getEndTime() {
                return endTime;
        }


        public Subject getSubject() {
                return subject;
        }


        public Room getRoom() {
                return room;
        }


        public Building getBuilding() {
                return building;
        }


        @Override
        public String toString() {
                return "[id: " + id + "]" + startTime + " - " + endTime + ": " + subject + " (" + room + " - " + building + ")";
        }
}
