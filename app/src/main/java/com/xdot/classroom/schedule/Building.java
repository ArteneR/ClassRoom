package com.xdot.classroom.schedule;


public class Building {
        private String id;
        private String name;


        public Building(String id, String name) {
                this.id = id;
                this.name = name;
        }


        public String getName() {
                return this.name;
        }


        @Override
        public String toString() {
                return name;
        }
}
