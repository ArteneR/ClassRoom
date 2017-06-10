package com.xdot.classroom.location;



public class Section {
        private static String LOG_TAG = "Section";
        private String Name;
        private BuildingLocationCoords SectionLocation;


        /*
         * Do not delete!
         * Needed by Firebase.
         */
        public Section() {}


        public Section(String Name) {
                this.Name = Name;
        }


        public String getName() {
                return this.Name;
        }


        public BuildingLocationCoords getSectionLocation() {
                return this.SectionLocation;
        }


        public void setSectionLocation(BuildingLocationCoords SectionLocation) {
                this.SectionLocation = SectionLocation;
        }


        @Override
        public String toString() {
                return "Section Name: " + this.Name;
        }

}
