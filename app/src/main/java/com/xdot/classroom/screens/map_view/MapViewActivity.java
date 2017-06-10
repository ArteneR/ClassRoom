package com.xdot.classroom.screens.map_view;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.xdot.classroom.location.Building;
import com.xdot.classroom.location.BuildingLocationCoords;
import com.xdot.classroom.CommonFunctionalities;
import com.xdot.classroom.R;
import com.xdot.classroom.location.ClassroomLocation;
import com.xdot.classroom.location.Section;



public class MapViewActivity extends AppCompatActivity implements OnMapReadyCallback,
                                                                  GoogleApiClient.ConnectionCallbacks,
                                                                  GoogleApiClient.OnConnectionFailedListener,
                                                                  LocationListener {
        private static String LOG_TAG = "MapViewActivity";
        private FirebaseDatabase firebaseDB;
        private DatabaseReference firebaseDBRef;
        private GoogleMap googleMap;
        private SupportMapFragment mapFrag;
        private LocationRequest mLocationRequest;
        private GoogleApiClient mGoogleApiClient;
        private Location mLastLocation;
        private Marker mCurrLocationMarker;
        private Marker mSearchedBuildingMarker;
        public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
        private static final String PREFS_LOCATION = "PREFS_LOCATION";
        private String lastLocationLongitude;
        private String lastLocationLatitude;
        private EditText etBuilding;
        private EditText etRoom;
        private String searchedBuilding;
        private String searchedRoom;



        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_map_view);

                connectToFirebase();
                initializeUIElements();

                restoreUserPreferences();
                activateCustomActionBar();
                activateGoogleMaps();
        }


        @Override
        protected void onStop() {
                super.onStop();
                saveUserPreferences();
        }


        private void connectToFirebase() {
                firebaseDB = FirebaseDatabase.getInstance();
                firebaseDBRef = firebaseDB.getReference();
        }


        private void initializeUIElements() {
                etBuilding = (EditText) findViewById(R.id.etBuilding);
                etRoom = (EditText) findViewById(R.id.etRoom);
        }


        private void restoreUserPreferences() {
                SharedPreferences settings = getSharedPreferences(PREFS_LOCATION, 0);
                lastLocationLongitude = settings.getString("lastLocationLongitude", "0");
                lastLocationLatitude = settings.getString("lastLocationLatitude", "0");
        }


        private void saveUserPreferences() {
                SharedPreferences settings = getSharedPreferences(PREFS_LOCATION, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("lastLocationLongitude", String.valueOf(mLastLocation.getLongitude()));
                editor.putString("lastLocationLatitude", String.valueOf(mLastLocation.getLatitude()));
                editor.commit();
        }


        /*
         * Setup the custom action bar for navigation
         */
        private void activateCustomActionBar() {
                android.support.v7.app.ActionBar mActionBar = getSupportActionBar();

                mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#BB266195")));
                mActionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#BB266195")));

                mActionBar.setDisplayShowHomeEnabled(false);
                mActionBar.setDisplayShowTitleEnabled(false);
                LayoutInflater mInflater = LayoutInflater.from(this);

                View mCustomView = mInflater.inflate(R.layout.custom_actionbar_map_view, null);

                mActionBar.setCustomView(mCustomView);
                mActionBar.setDisplayShowCustomEnabled(true);
        }


        private void activateGoogleMaps() {
                Log.d(LOG_TAG, "### activateGoogleMaps");
                mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                mapFrag.getMapAsync(this);
        }


        @Override
        public void onMapReady(GoogleMap googleMap) {
                Log.d(LOG_TAG, "### onMapReady");
                this.googleMap = googleMap;
                this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                this.googleMap.getUiSettings().setZoomControlsEnabled(true);

                //Initialize Google Play Services
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                //Location Permission already granted
                                Log.d(LOG_TAG, "Location Permission already granted");
                                buildGoogleApiClient();
                                this.googleMap.setMyLocationEnabled(true);
                        }
                        else {
                                //Request Location Permission
                                checkLocationPermission();
                        }
                }
                else {
                        buildGoogleApiClient();
                        this.googleMap.setMyLocationEnabled(true);
                }
        }


        private void checkLocationPermission() {
                Log.d(LOG_TAG, "### checkLocationPermission");
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                                // Show an explanation to the user *asynchronously* -- don't block
                                // this thread waiting for the user's response! After the user
                                // sees the explanation, try again to request the permission.
                                new AlertDialog.Builder(this)
                                    .setTitle("Location Permission Needed")
                                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                    //Prompt the user once explanation has been shown
                                                    ActivityCompat.requestPermissions(MapViewActivity.this,
                                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                                        MY_PERMISSIONS_REQUEST_LOCATION );
                                            }
                                    })
                                    .create()
                                    .show();
                        }
                        else {
                                // No explanation needed, we can request the permission.
                                ActivityCompat.requestPermissions(this,
                                                                  new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                                                  MY_PERMISSIONS_REQUEST_LOCATION );
                        }
                }
        }


        protected synchronized void buildGoogleApiClient() {
                Log.d(LOG_TAG, "### buildGoogleApiClient");
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                                            .addConnectionCallbacks(this)
                                            .addOnConnectionFailedListener(this)
                                            .addApi(LocationServices.API)
                                            .build();
                mGoogleApiClient.connect();
        }


        @Override
        public void onConnected(Bundle bundle) {
                Log.d(LOG_TAG, "### onConnected");
                mLocationRequest = new LocationRequest();
                mLocationRequest.setInterval(1000);
                mLocationRequest.setFastestInterval(1000);
                mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Log.d(LOG_TAG, "### onConnected Granted");
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                }

                Location lastLocation = new Location("dummyprovider");
                Double lastLocLongitude = Double.parseDouble(lastLocationLongitude);
                Double lastLocLatitude = Double.parseDouble(lastLocationLatitude);
                lastLocation.setLongitude(lastLocLongitude);
                lastLocation.setLatitude(lastLocLatitude);
                onLocationChanged(lastLocation);
        }


        @Override
        public void onLocationChanged(Location location) {
                Log.d(LOG_TAG, "### onLocationChanged");
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                        mCurrLocationMarker.remove();
                }

                //Place current location marker
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Your Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                mCurrLocationMarker = this.googleMap.addMarker(markerOptions);

                //move map camera
                int zoomLevel = 16;
                this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
        }


        @Override
        public void onConnectionSuspended(int i) {
                Log.d(LOG_TAG, "### onConnectionSuspended");
        }


        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
                Log.d(LOG_TAG, "### onConnectionFailed");
        }


        @Override
        public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
                Log.d(LOG_TAG, "### onRequestPermissionsResult");
                switch (requestCode) {
                        case MY_PERMISSIONS_REQUEST_LOCATION: {
                                // If request is cancelled, the result arrays are empty.
                                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                                        // permission was granted, yay! Do the
                                        // location-related task you need to do.
                                        if (ContextCompat.checkSelfPermission(this,
                                            Manifest.permission.ACCESS_FINE_LOCATION)
                                            == PackageManager.PERMISSION_GRANTED) {

                                                if (mGoogleApiClient == null) {
                                                        buildGoogleApiClient();
                                                }
                                                this.googleMap.setMyLocationEnabled(true);
                                        }

                                } else {
                                        // permission denied, boo! Disable the
                                        // functionality that depends on this permission.
                                        Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                                }
                                return;
                        }
                }
        }


        /*
         * Handle user click events
         */
        public void clicked(View view) {
                Log.d(LOG_TAG, "Button clicked!");

                switch (view.getId()) {
                        case R.id.ivLeftActionbarButton:
                                Log.d(LOG_TAG, "Button: Back");
                                goToPreviousActivity();
                                break;

                        case R.id.btnFind:
                                Log.d(LOG_TAG, "Button: Find");
                                findSearchedBuildingLocation();
                                break;
                }
        }


        private void goToPreviousActivity() {
                super.onBackPressed();
        }


        private void findSearchedBuildingLocation() {
                getUserSelectedValues();
                final String searchedLocation = "Timisoara";

                firebaseDBRef.child("Locations").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot locationsSnapshot) {
                                boolean foundLocation = false;

                                for (DataSnapshot locationSnapshot: locationsSnapshot.getChildren()) {
                                        ClassroomLocation location = locationSnapshot.getValue(ClassroomLocation.class);
                                        String currentLocationName = location.getName().toString();

                                        if (currentLocationName.equals(searchedLocation)) {
                                                foundLocation = true;
                                                findSearchedBuildingAtLocation(location);
                                        }
                                }

                                if (!foundLocation) {
                                        CommonFunctionalities.displayLongToast("Couldn't find the specified building/room!", getApplicationContext());
                                }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                });
        }


        private void findSearchedBuildingAtLocation(ClassroomLocation location) {
                Building building = location.getBuilding(searchedBuilding);
                Section section = location.getSection(searchedBuilding);
                BuildingLocationCoords buildingCoords = null;

                if (building != null) {
                        buildingCoords = building.getBuildingLocation();
                }
                else if (section != null) {
                        buildingCoords = section.getSectionLocation();
                }

                if (buildingCoords == null) {
                        CommonFunctionalities.displayLongToast("The specified building/room couldn't be found!", getApplicationContext());
                        return ;
                }

                double buildingLatitude = buildingCoords.getLatitude();
                double buildingLongitude= buildingCoords.getLongitude();

                addDestinationMarker(buildingLatitude, buildingLongitude);
        }


        private void addDestinationMarker(double destLatitude, double destLongitude) {
                if (mSearchedBuildingMarker != null) {
                        mSearchedBuildingMarker.remove();
                }
                String destMarkerTitle = searchedBuilding + (searchedRoom.isEmpty() ? "" : " - " + searchedRoom);
                LatLng latLngSearchedBuilding = new LatLng(destLatitude, destLongitude);
                MarkerOptions markerOptionsSearchedBuilding = new MarkerOptions();

                markerOptionsSearchedBuilding.position(latLngSearchedBuilding);
                markerOptionsSearchedBuilding.title(destMarkerTitle);
                markerOptionsSearchedBuilding.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                mSearchedBuildingMarker = this.googleMap.addMarker(markerOptionsSearchedBuilding);
                mSearchedBuildingMarker.showInfoWindow();
        }


        private void getUserSelectedValues() {
                searchedBuilding = etBuilding.getText().toString();
                searchedRoom = etRoom.getText().toString();
        }

}
