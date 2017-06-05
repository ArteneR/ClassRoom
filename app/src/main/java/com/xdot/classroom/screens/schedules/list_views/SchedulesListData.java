package com.xdot.classroom.screens.schedules.list_views;


public class SchedulesListData {
        private String scheduleName;


        public SchedulesListData(String scheduleName){
                this.scheduleName = scheduleName;
        }


        public String getScheduleName() {
                return scheduleName;
        }


        public void setScheduleName(String scheduleName) {
                this.scheduleName = scheduleName;
        }
}