package com.xdot.classroom;

import android.app.Application;

import com.xdot.classroom.university_activities.Course;
import com.xdot.classroom.university_activities.Exam;
import com.xdot.classroom.university_activities.Lab;
import com.xdot.classroom.university_activities.Seminary;
import com.xdot.classroom.university_activities.UniversityActivity;

import java.util.ArrayList;
import java.util.List;


public class DataProvider extends Application {
    private List<Schedule> schedules;


    public DataProvider() {
        this.schedules = new ArrayList<Schedule>();
        this.createTestSchedule();
    }


    public Schedule getSchedule(String id){
        for (int i = 0; i < this.schedules.size(); i++) {
            String scheduleId = this.schedules.get(i).getId();
            if (scheduleId.equals(id)) {
                return this.schedules.get(i);
            }
        }
        return null;
    }


    private void createTestSchedule() {
        Schedule myTestSchedule = new Schedule("12345", "Test schedule");

        UniversityActivity univActivity1 = new Course();
        UniversityActivity univActivity2 = new Course();
        UniversityActivity univActivity3 = new Exam();
        UniversityActivity univActivity4 = new Lab();
        UniversityActivity univActivity5 = new Lab();
        UniversityActivity univActivity6 = new Seminary();
        UniversityActivity univActivity7 = new Seminary();
        UniversityActivity univActivity8 = new Seminary();

        myTestSchedule.addUniversityActivity(univActivity1, "Monday");
        myTestSchedule.addUniversityActivity(univActivity2, "Tuesday");
        myTestSchedule.addUniversityActivity(univActivity3, "Tuesday");
        myTestSchedule.addUniversityActivity(univActivity4, "Wednesday");
        myTestSchedule.addUniversityActivity(univActivity5, "Thursday");
        myTestSchedule.addUniversityActivity(univActivity6, "Friday");
        myTestSchedule.addUniversityActivity(univActivity7, "Friday");
        myTestSchedule.addUniversityActivity(univActivity8, "Friday");
    }


    public void printSchedules() {
        for (int i = 0; i < this.schedules.size(); i++) {
            this.schedules.get(i);
        }
    }


    public List<Schedule> getAllSchedules() {
        return schedules;
    }


    public void addSchedule(Schedule newSchedule){
        this.schedules.add(newSchedule);
    }

}
