package com.xdot.classroom.send_notifications;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.xdot.classroom.R;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

        private static final String TAG = "MyFirebaseMessagingService";


        @Override
        public void onMessageReceived(RemoteMessage remoteMessage) {
                // TODO: Handle FCM messages here.
                // If the application is in the foreground handle both data and notification messages here.
                // Also if you intend on generating your own notifications as a result of a received FCM
                // message, here is where that should be initiated.
                Log.d(TAG, "From: " + remoteMessage.getFrom());
                Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

                sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }


        /**
         * Create and show a simple notification containing the received FCM message.
         */

        private void sendNotification(String messageTitle, String messageBody) {
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    R.mipmap.ic_classroom_launcher);

                NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                    .setSound(defaultSoundUri)
                    .setLargeIcon(icon)
                    .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAppMain))
                    .setSmallIcon(R.mipmap.ic_classroom_launcher)
                    .setContentTitle(messageTitle)
                    .setContentText(messageBody);

                NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

        }
}