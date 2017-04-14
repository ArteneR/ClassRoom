package com.xdot.classroom.university_activities;


import android.util.Log;

public class UniversityActivity {
    private String id;
    private String startTime;
    private String endTime;
    private String RoomId;
    private String SubjectId;
    private boolean evenWeeksOnly = false;
    private boolean oddWeeksOnly = false;


    public UniversityActivity(String id) {
        this.id = id;
    }


    public void printActivity() {
        Log.d("DEBUG", "Subject name - roomid (start time - end time)");
    }

}
