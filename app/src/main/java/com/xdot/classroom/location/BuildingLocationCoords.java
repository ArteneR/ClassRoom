package com.xdot.classroom.location;



public class BuildingLocationCoords {
        private static String LOG_TAG = "BuildingLocationCoords";
        private double Latitude;
        private double Longitude;


        /*
         * Do not delete!
         * Needed by Firebase.
         */
        public BuildingLocationCoords() {}


        public BuildingLocationCoords(double Latitude, double Longitude) {
                this.Latitude = Latitude;
                this.Longitude = Longitude;
        }


        public void setLatitude(double Latitude) {
                this.Latitude = Latitude;
        }


        public double getLatitude() {
                return this.Latitude;
        }


        public void setLongitude(double Longitude) {
                this.Longitude = Longitude;
        }


        public double getLongitude() {
                return this.Longitude;
        }


        @Override
        public String toString() {
                return "Building Location Coords:\nLatitude: " + this.Latitude + " - Longitude: " + this.Longitude;
        }

}
