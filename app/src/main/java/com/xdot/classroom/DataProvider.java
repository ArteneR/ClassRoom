package com.xdot.classroom;

import android.app.Application;

import com.xdot.classroom.schedule.Room;
import com.xdot.classroom.schedule.Schedule;
import com.xdot.classroom.schedule.Subject;
import com.xdot.classroom.schedule.Time;
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
        this.init();
    }


    public void init() {
        this.schedules = new ArrayList<Schedule>();
        this.createTestSchedule();
    }


    private void createTestSchedule() {
        Schedule myTestSchedule = new Schedule("12345", "Test schedule");

        UniversityActivity univActivity1 = new Course("CourseId1", new Time("08:00"), new Time("10:00"), new Room("RoomId1", "A209"), new Subject("SubjectId1", "Mathematics"));
        UniversityActivity univActivity2 = new Course("CourseId2", new Time("12:00"), new Time("14:00"), new Room("RoomId2", "A408"), new Subject("SubjectId2", "Programming"));
        UniversityActivity univActivity3 = new Exam("ExamId1", new Time("19:00"), new Time("21:00"), new Room("RoomId3", "A110"), new Subject("SubjectId3", "Physics"));
        UniversityActivity univActivity4 = new Lab("LabId1", new Time("10:00"), new Time("12:00"), new Room("RoomId4", "B205"), new Subject("SubjectId2", "Programming"));
        UniversityActivity univActivity5 = new Lab("LabId2", new Time("15:00"), new Time("18:00"), new Room("RoomId1", "A209"), new Subject("SubjectId1", "Mathematics"));
        UniversityActivity univActivity6 = new Seminary("SeminaryId1", new Time("15:00"), new Time("18:00"), new Room("RoomId5", "A112"), new Subject("SubjectId4", "Digital Logic"));
        UniversityActivity univActivity7 = new Seminary("SeminaryId1", new Time("10:00"), new Time("12:00"), new Room("RoomId2", "A408"), new Subject("SubjectId2", "Programming"));
        UniversityActivity univActivity8 = new Seminary("SeminaryId1", new Time("14:00"), new Time("16:00"), new Room("RoomId6", "C300"), new Subject("SubjectId3", "Physics"));

        myTestSchedule.addUniversityActivity(univActivity1, "Monday");
        myTestSchedule.addUniversityActivity(univActivity2, "Tuesday");
        myTestSchedule.addUniversityActivity(univActivity3, "Tuesday");
        myTestSchedule.addUniversityActivity(univActivity4, "Wednesday");
        myTestSchedule.addUniversityActivity(univActivity5, "Thursday");
        myTestSchedule.addUniversityActivity(univActivity6, "Friday");
        myTestSchedule.addUniversityActivity(univActivity7, "Friday");
        myTestSchedule.addUniversityActivity(univActivity8, "Friday");

        this.addSchedule(myTestSchedule);
    }


    public void printSchedules() {
        for (int i = 0; i < this.schedules.size(); i++) {
            this.schedules.get(i).printSchedule();
        }
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


    public List<Schedule> getAllSchedules() {
        return schedules;
    }


    public void addSchedule(Schedule newSchedule){
        this.schedules.add(newSchedule);
    }


    public int getSchedulesCount() {
        return this.schedules.size();
    }

}
