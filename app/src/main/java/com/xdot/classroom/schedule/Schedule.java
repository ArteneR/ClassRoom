package com.xdot.classroom.schedule;

import android.util.Log;

import com.xdot.classroom.university_activities.UniversityActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Schedule {
    private String id;
    private String name;
    private Map<String, List<UniversityActivity>> univActivities;


    public Schedule(String id, String name) {
        this.id = id;
        this.name = name;
        this.univActivities = new HashMap<String, List<UniversityActivity>>();
        this.makeScheduleDayOfWeekEntries();
    }


    public void makeScheduleDayOfWeekEntries() {
        this.univActivities.put("Monday", new ArrayList<UniversityActivity>());
        this.univActivities.put("Tuesday", new ArrayList<UniversityActivity>());
        this.univActivities.put("Wednesday", new ArrayList<UniversityActivity>());
        this.univActivities.put("Thursday", new ArrayList<UniversityActivity>());
        this.univActivities.put("Friday", new ArrayList<UniversityActivity>());
        this.univActivities.put("Saturday", new ArrayList<UniversityActivity>());
        this.univActivities.put("Sunday", new ArrayList<UniversityActivity>());
    }


    public void printSchedule() {
        Log.d("DEBUG", "Schedule name: " + this.name + " (id: " + this.id + "):");
        for (String key : this.univActivities.keySet()) {
            Log.d("DEBUG", key + ":");
            for (int i = 0; i < this.univActivities.get(key).size(); i++) {
                this.univActivities.get(key).get(i).printActivity();
            }
        }
    }


    public String getId() {
        return this.id;
    }


    public void addUniversityActivity(UniversityActivity univActivity, String dayOfTheWeek) {
        this.univActivities.get(dayOfTheWeek).add(univActivity);
    }


    public void removeUniversityActivity() {

    }

}
