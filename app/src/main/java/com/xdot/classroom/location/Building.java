package com.xdot.classroom.location;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;



public class Building {
        private static String LOG_TAG = "Building";
        private String Name;
        private BuildingLocationCoords BuildingLocation;
        private Map<String, Section> Sections;


        /*
         * Do not delete!
         * Needed by Firebase.
         */
        public Building() {}


        public Building(String Name) {
                this.Name = Name;
        }


        public void setName(String Name) {
                this.Name = Name;
        }


        public String getName() {
                return this.Name;
        }


        public void setBuildingLocation(BuildingLocationCoords BuildingLocation) {
                this.BuildingLocation = BuildingLocation;
        }


        public BuildingLocationCoords getBuildingLocation() {
                return this.BuildingLocation;
        }


        public void setSections(Map<String, Section> Sections) {
                this.Sections = Sections;
        }


        public Map<String, Section> getSections() {
                return this.Sections;
        }


        public Section getSection(String searchedSection) {
                Set<Map.Entry<String, Section>> entries = Sections.entrySet();
                Iterator<Map.Entry<String, Section>> it = entries.iterator();

                while (it.hasNext()) {
                    Map.Entry<String, Section> entry = it.next();
                    Section section = entry.getValue();
                    String sectionName = section.getName();

                    if (sectionName.equals(searchedSection)) {
                            return section;
                    }
                }
                return null;
        }


        @Override
        public String toString() {
                String buildingPrintString = "Building Name: " + this.Name + "\nBuilding Location: " + this.BuildingLocation;
                if (Sections == null) {
                    return buildingPrintString;
                }
                buildingPrintString += "\nSections:\n";

                Set<Map.Entry<String, Section>> entries = Sections.entrySet();
                Iterator<Map.Entry<String, Section>> it = entries.iterator();

                while (it.hasNext()) {
                    Map.Entry<String, Section> entry = it.next();
                    Section section = entry.getValue();
                    buildingPrintString += (section + "\n\n");
                }

                return buildingPrintString;
        }

}
