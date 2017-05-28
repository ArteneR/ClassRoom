package com.xdot.classroom.send_notifications;

import android.util.Log;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;



public class FirebaseIDService extends FirebaseInstanceIdService {

        private static String LOG_TAG = "FirebaseIDService";
        private FirebaseDatabase firebaseDB;
        private DatabaseReference firebaseDBRef;


        @Override
        public void onTokenRefresh() {
                // Get updated InstanceID token.
                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                Log.d(LOG_TAG, "Refreshed token: " + refreshedToken);

                connectToFirebase();
                // TODO: Implement this method to send any registration to your app's servers.
                sendRegistrationToServer(refreshedToken);
        }



        private void sendRegistrationToServer(String newDeviceRegistrationId) {
                // Add custom implementation, as needed.
                String userId = "4o5JWilDQyTcrY7JyngUhzR8NGj1";
                firebaseDBRef.child("Users").child(userId).child("DeviceRegistrationID").setValue(newDeviceRegistrationId);
        }



        private void connectToFirebase() {
                Log.d(LOG_TAG, "connectToFirebase");
                firebaseDB = FirebaseDatabase.getInstance();
                firebaseDBRef = firebaseDB.getReference();
        }
}