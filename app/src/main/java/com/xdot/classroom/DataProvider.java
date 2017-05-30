package com.xdot.classroom;

import android.app.Application;
import android.util.Log;
import com.xdot.classroom.schedule.Schedule;
import java.util.ArrayList;
import java.util.List;



public class DataProvider extends Application {
        private List<Schedule> schedules;
        private static String LOG_TAG = "DataProvider";
        private int currentScheduleIndex = 0;


        public DataProvider() {}


        public void init() {
                this.schedules = new ArrayList<Schedule>();
//                this.createTestSchedule();
        }


        private void createTestSchedule() {
                Schedule myTestSchedule = new Schedule("12345", "Test schedule");

//                UniversityActivity univActivity1 = new Course("CourseId1", new Time("08:00"), new Time("10:00"), new Room("RoomId1", "A209"), new Building("BuildingId1", "Corpul B"), new OLD_Subject("SubjectId1", "Mathematics"));
//                UniversityActivity univActivity2 = new Course("CourseId2", new Time("12:00"), new Time("14:00"), new Room("RoomId2", "A408"), new Building("BuildingId2", "Corpul A"), new OLD_Subject("SubjectId2", "Programming"));
//                UniversityActivity univActivity3 = new Exam("ExamId1", new Time("19:00"), new Time("21:00"), new Room("RoomId3", "A110"), new Building("BuildingId1", "Corpul B"), new OLD_Subject("SubjectId3", "Physics"));
//                UniversityActivity univActivity4 = new Lab("LabId1", new Time("10:00"), new Time("12:00"), new Room("RoomId4", "B205"), new Building("BuildingId2", "Corpul A"), new OLD_Subject("SubjectId2", "Programming"));
//                UniversityActivity univActivity5 = new Lab("LabId2", new Time("15:00"), new Time("18:00"), new Room("RoomId1", "A209"), new Building("BuildingId2", "Corpul A"), new OLD_Subject("SubjectId1", "Mathematics"));
//                UniversityActivity univActivity6 = new Seminary("SeminaryId1", new Time("15:00"), new Time("18:00"), new Room("RoomId5", "A112"), new Building("BuildingId2", "Corpul A"), new OLD_Subject("SubjectId4", "Digital Logic"));
//                UniversityActivity univActivity7 = new Seminary("SeminaryId1", new Time("10:00"), new Time("12:00"), new Room("RoomId2", "A408"), new Building("BuildingId2", "Corpul A"), new OLD_Subject("SubjectId2", "Programming"));
//                UniversityActivity univActivity8 = new Seminary("SeminaryId1", new Time("14:00"), new Time("15:00"), new Room("RoomId6", "C300"), new Building("BuildingId2", "Corpul A"), new OLD_Subject("SubjectId3", "Physics"));
//
//                myTestSchedule.addUniversityActivity(univActivity1, "Monday");
//                myTestSchedule.addUniversityActivity(univActivity2, "Tuesday");
//                myTestSchedule.addUniversityActivity(univActivity3, "Tuesday");
//                myTestSchedule.addUniversityActivity(univActivity4, "Wednesday");
//                myTestSchedule.addUniversityActivity(univActivity5, "Thursday");
//                myTestSchedule.addUniversityActivity(univActivity6, "Friday");
//                myTestSchedule.addUniversityActivity(univActivity7, "Friday");
//                myTestSchedule.addUniversityActivity(univActivity8, "Friday");

                this.addSchedule(myTestSchedule);
        }


        public void printSchedules() {
                for (int i = 0; i < this.schedules.size(); i++) {
                    this.schedules.get(i).printSchedule();
                }
        }


        public Schedule getSchedule(String id){
                return this.schedules.get(Integer.parseInt(id));
        }


        public Schedule getCurrentSchedule() {
                return getSchedule(String.valueOf(this.currentScheduleIndex));
        }

        public int getCurrentScheduleIndex() {
                return currentScheduleIndex;
        }


        public void setCurrentScheduleIndex(int currentScheduleIndex) {
                this.currentScheduleIndex = currentScheduleIndex;
        }


        public List<Schedule> getAllSchedules() {
                return schedules;
        }


        public void addSchedule(Schedule newSchedule) {
                this.schedules.add(newSchedule);
        }


        public int getSchedulesCount() {
                return this.schedules.size();
        }


        public void removeAllSchedules() {
                this.schedules.clear();
        }

}
