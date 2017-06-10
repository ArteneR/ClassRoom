package com.xdot.classroom.screens.schedules;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.xdot.classroom.CommonFunctionalities;
import com.xdot.classroom.DataProvider;
import com.xdot.classroom.screens.map_view.MapViewActivity;
import com.xdot.classroom.screens.schedules.list_views.SchedulesListData;
import com.xdot.classroom.screens.schedules.list_views.SchedulesRecyclerViewAdapter;
import com.xdot.classroom.R;
import com.xdot.classroom.schedule.Schedule;
import com.xdot.classroom.screens.create_schedule.CreateScheduleActivity;
import com.xdot.classroom.screens.login.LoginActivity;
import com.xdot.classroom.university_activities.UniversityActivity;
import java.util.ArrayList;
import com.google.android.gms.location.LocationListener;



public class SchedulesActivity extends AppCompatActivity implements OnMapReadyCallback,
                                                                    GoogleApiClient.ConnectionCallbacks,
                                                                    GoogleApiClient.OnConnectionFailedListener,
                                                                    LocationListener {
        private static String LOG_TAG = "SchedulesActivity";
        private RecyclerView schedulesRecyclerView;
        private RecyclerView.Adapter schedulesAdapter;
        private RecyclerView.LayoutManager schedulesLayoutManager;
        private DataProvider dataProvider;
        private FirebaseDatabase firebaseDB;
        private DatabaseReference firebaseDBRef;
        private FirebaseAuth firebaseAuth;
        private FirebaseUser currentUser;
        private GoogleMap googleMap;
        private SupportMapFragment mapFrag;
        private LocationRequest mLocationRequest;
        private GoogleApiClient mGoogleApiClient;
        private Location mLastLocation;
        private Marker mCurrLocationMarker;
        public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
        private static final String PREFS_LOCATION = "PREFS_LOCATION";
        private String lastLocationLongitude;
        private String lastLocationLatitude;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_schedules);

                restoreUserPreferences();
                connectToFirebase();
                activateGoogleMaps();
        }


        @Override
        protected void onStart() {
                super.onStart();

                if (!userIsLoggedIn()) {
                    goToLoginActivity();
                } else {
                    displaySchedulesIfUserAccountIsActivated();
                }
        }


        @Override
        protected void onStop() {
                super.onStop();
                saveUserPreferences();
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


        private boolean userIsLoggedIn() {
                currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    return true;
                }
                return false;
        }


        private void displaySchedulesIfUserAccountIsActivated() {
                String userId = currentUser.getUid();

                firebaseDBRef.child("Users").child(userId).child("AccountActivated").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                                Object snapshotValue = snapshot.getValue();

                                if (snapshotValue == null) {
                                    firebaseAuth.signOut();
                                    CommonFunctionalities.displayLongToast("Something went wrong! Please login again!", getApplicationContext());
                                    goToLoginActivity();
                                    return;
                                }

                                boolean accountActivated = Boolean.parseBoolean(snapshotValue.toString());

                                if (accountActivated) {
                                    addListenerForUpdateDeviceRegistrationId();

                                    initializeDataProviderModule();

                                    activateCustomActionBar();
                                    getFirebaseSchedules();
                                } else {
                                    CommonFunctionalities.displayLongToast("Your user account is not activated yet!", getApplicationContext());
                                    goToLoginActivity();
                                }
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                });
        }


        private void goToLoginActivity() {
                Intent intent = new Intent(this, LoginActivity.class);
                this.startActivity(intent);
        }


        private void connectToFirebase() {
                firebaseDB = FirebaseDatabase.getInstance();
                firebaseDBRef = firebaseDB.getReference();
                firebaseAuth = FirebaseAuth.getInstance();
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
                                                        ActivityCompat.requestPermissions(SchedulesActivity.this,
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
                mCurrLocationMarker.showInfoWindow();

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


        private void addListenerForUpdateDeviceRegistrationId() {
                String userId = firebaseAuth.getCurrentUser().getUid();

                firebaseDBRef.child("Users").child(userId).child("DeviceRegistrationID").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                                Object snapshotValue = snapshot.getValue();
                                if (snapshotValue == null) {
                                    checkForUpdateDeviceRegistrationId("");
                                }
                                else {
                                    String firebaseDeviceRegistrationId = snapshotValue.toString();
                                    checkForUpdateDeviceRegistrationId(firebaseDeviceRegistrationId);
                                }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                });
        }



        private void checkForUpdateDeviceRegistrationId(String firebaseDeviceRegistrationId) {
                String newDeviceRegistrationId = FirebaseInstanceId.getInstance().getToken();
                Log.d(LOG_TAG, "OLD Token: " + firebaseDeviceRegistrationId);
                Log.d(LOG_TAG, "NEW Token: " + newDeviceRegistrationId);
                if (!newDeviceRegistrationId.equals(firebaseDeviceRegistrationId)) {
                    updateDeviceRegistrationId(newDeviceRegistrationId);
                }
        }



        private void updateDeviceRegistrationId(String newDeviceRegistrationId) {
                String userId = firebaseAuth.getCurrentUser().getUid();

                firebaseDBRef.child("Users").child(userId).child("DeviceRegistrationID").setValue(newDeviceRegistrationId);
        }



        private void initializeDataProviderModule() {
                dataProvider = (DataProvider) getApplication();
                dataProvider.init();
                dataProvider.printSchedules();
        }



        @Override
        protected void onResume() {
                super.onResume();
        }



        private ArrayList<SchedulesListData> getDataSet() {
                ArrayList results = new ArrayList<SchedulesListData>();
                int index = 0;
                for (Schedule schedule: dataProvider.getAllSchedules()) {
                    SchedulesListData obj = new SchedulesListData(schedule.Name);
                    results.add(index, obj);
                    index++;
                }
                return results;
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

                View mCustomView = mInflater.inflate(R.layout.custom_actionbar_schedules, null);

                mActionBar.setCustomView(mCustomView);
                mActionBar.setDisplayShowCustomEnabled(true);
        }



        private void getFirebaseSchedules() {
                String userId = firebaseAuth.getCurrentUser().getUid();

                firebaseDBRef.child("Users").child(userId).child("Schedules").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            // First delete the schedules
                            dataProvider.removeAllSchedules();

                            for (DataSnapshot scheduleSnapshot: dataSnapshot.getChildren()) {
                                String scheduleId = scheduleSnapshot.getKey();
                                Schedule firebaseSchedule = scheduleSnapshot.getValue(Schedule.class);
                                firebaseSchedule.setId(scheduleId);
                                firebaseSchedule.init();

                                for (DataSnapshot entrySnapshot: scheduleSnapshot.child("Entries").getChildren()) {
                                    String dayOfWeek = entrySnapshot.getKey();

                                    for (DataSnapshot univActivityTypeSnapshot: entrySnapshot.getChildren()) {
                                        String univActivityType = univActivityTypeSnapshot.getKey();

                                        for (DataSnapshot univActivitySnapshot: univActivityTypeSnapshot.getChildren()) {
                                            String univActivityId = univActivitySnapshot.getKey();
                                            String className = "com.xdot.classroom.university_activities." + CommonFunctionalities.pluralToSingular(univActivityType);

                                            try {
                                                Class univActivityClass = Class.forName(className);
                                                UniversityActivity univActivity = (UniversityActivity) univActivitySnapshot.getValue(univActivityClass);
                                                univActivity.setId(univActivityId);
                                                firebaseSchedule.addUniversityActivity(univActivity, dayOfWeek);
                                            }
                                            catch (ClassNotFoundException e) {
                                                e.printStackTrace();
                                            }

                                        }

                                    }

                                }

                                firebaseSchedule.printSchedule();
                                dataProvider.addSchedule(firebaseSchedule);
                            }

                            // display Schedules data...
                            activateSchedulesListView();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                });
        }



        /*
         * Setup the list which will display the available schedules
         */
        public void activateSchedulesListView() {
                Log.d(LOG_TAG, "Schedules count: " + this.dataProvider.getSchedulesCount());
                if (this.dataProvider.getSchedulesCount() == 0) {
                    // Don't activate the schedules list -> display text telling user that there are no schedules
                    return ;
                }

                hideNoSchedulesMessage();


                schedulesLayoutManager = new LinearLayoutManager(this);
                schedulesAdapter = new SchedulesRecyclerViewAdapter(getDataSet());
                schedulesRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewSchedulesList);
                schedulesRecyclerView.setHasFixedSize(true);
                schedulesRecyclerView.setLayoutManager(schedulesLayoutManager);
                schedulesRecyclerView.setAdapter(schedulesAdapter);

                // Code to Add an item with default animation
                //((SchedulesRecyclerViewAdapter) schedulesAdapter).addItem(obj, index);

                // Code to remove an item with default animation
                //((SchedulesRecyclerViewAdapter) schedulesAdapter).deleteItem(index);
        }


        private void hideNoSchedulesMessage() {
                View view = findViewById(R.id.relativeLayoutNoSchedules);
                view.setVisibility(View.INVISIBLE);
                view.setVisibility(View.GONE);
        }


        /*
         * Handle user click events
         */
        public void clicked(View view) {
                Log.d(LOG_TAG, "Button clicked!");

                switch (view.getId()) {
                        case R.id.ivLeftActionbarButton:
                                Log.d(LOG_TAG, "Button: Logout");
                                logout();
                                break;

                        case R.id.ivRightActionbarButton:
                                Log.d(LOG_TAG, "Button: Add Schedule");
                                openCreateScheduleActivity();
                                break;

                        case R.id.tvMapView:
                                Log.d(LOG_TAG, "Button: Map View");
                                openMapViewActivity();
                                break;
                }
        }


        private void logout() {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, LoginActivity.class);
                this.startActivity(intent);
        }


        private void openCreateScheduleActivity() {
                Intent intent = new Intent(this, CreateScheduleActivity.class);
                this.startActivity(intent);
        }


        private void openMapViewActivity() {
                Intent intent = new Intent(this, MapViewActivity.class);
                this.startActivity(intent);
        }

}
