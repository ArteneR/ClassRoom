package com.xdot.classroom.location;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;



public class ClassroomLocation {
        private static String LOG_TAG = "ClassroomLocation";
        private String Name;
        private Map<String, Building> Buildings;


        /*
         * Do not delete!
         * Needed by Firebase.
         */
        public ClassroomLocation() {}


        public ClassroomLocation(String Name) {
                this.Name = Name;
        }


        public void setName(String Name) {
                this.Name = Name;
        }


        public String getName() {
                return this.Name;
        }


        public void setBuildings(Map<String, Building> Buildings) {
                this.Buildings = Buildings;
        }


        public Map<String, Building> getBuildings() {
                return this.Buildings;
        }


        public Building getBuilding(String searchedBuilding) {
                Set<Map.Entry<String, Building>> entries = Buildings.entrySet();
                Iterator<Map.Entry<String, Building>> it = entries.iterator();

                while (it.hasNext()) {
                        Map.Entry<String, Building> entry = it.next();
                        Building building = entry.getValue();

                        if (building.getName().equals(searchedBuilding)) {
                                return building;
                        }
                }
                return null;
        }


        public Section getSection(String searchedSection) {
                Set<Map.Entry<String, Building>> entries = Buildings.entrySet();
                Iterator<Map.Entry<String, Building>> it = entries.iterator();

                while (it.hasNext()) {
                    Map.Entry<String, Building> entry = it.next();
                    Building building = entry.getValue();

                    Section section = building.getSection(searchedSection);
                    return section;
                }
                return null;
        }


        @Override
        public String toString() {
                String locationPrintString = "Location Name: " + this.Name;
                if (Buildings == null) {
                        return locationPrintString;
                }
                locationPrintString += "\nBuildings:\n";

                Set<Map.Entry<String, Building>> entries = Buildings.entrySet();
                Iterator<Map.Entry<String, Building>> it = entries.iterator();

                while (it.hasNext()) {
                        Map.Entry<String, Building> entry = it.next();
                        Building building = entry.getValue();
                        locationPrintString += (building + "\n\n");
                }

                return locationPrintString;
        }

}
