package com.xdot.classroom.send_notifications;

import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;



public class FirebaseIDService extends FirebaseInstanceIdService {
        private static String LOG_TAG = "FirebaseIDService";
        private FirebaseDatabase firebaseDB;
        private DatabaseReference firebaseDBRef;
        private FirebaseAuth firebaseAuth;


        @Override
        public void onTokenRefresh() {
                // Get updated InstanceID token.
                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                Log.d(LOG_TAG, "Refreshed token: " + refreshedToken);

                connectToFirebase();
                sendRegistrationToServer(refreshedToken);
        }


        private void connectToFirebase() {
                Log.d(LOG_TAG, "connectToFirebase");
                firebaseDB = FirebaseDatabase.getInstance();
                firebaseDBRef = firebaseDB.getReference();
                firebaseAuth = FirebaseAuth.getInstance();
        }


        private void sendRegistrationToServer(String newDeviceRegistrationId) {
                String userId = firebaseAuth.getCurrentUser().getUid();

                firebaseDBRef.child("Users").child(userId).child("DeviceRegistrationID").setValue(newDeviceRegistrationId);
        }
}