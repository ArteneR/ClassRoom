package com.xdot.classroom.send_notifications;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.xdot.classroom.R;



public class RegistrationService extends IntentService {

        public RegistrationService() {
                super("RegistrationService");
        }


        @Override
        protected void onHandleIntent(Intent intent) {
                InstanceID myID = InstanceID.getInstance(this);
                try {
                    String registrationToken = myID.getToken(
                        getString(R.string.gcm_defaultSenderId),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE,
                        null
                    );

                    Log.d("Registration Token", registrationToken);

                    GcmPubSub subscription = GcmPubSub.getInstance(this);
                    subscription.subscribe(registrationToken, "/topics/my_little_topic", null);

                } catch(Exception e) {
                    Log.e("REGISTRATION ERROR", e.toString());
                }
        }
}
