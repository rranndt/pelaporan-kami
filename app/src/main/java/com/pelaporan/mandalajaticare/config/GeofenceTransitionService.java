package com.pelaporan.mandalajaticare.config;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.util.Log;

import com.pelaporan.mandalajaticare.R;
import com.pelaporan.mandalajaticare.Menu;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

public class GeofenceTransitionService extends IntentService {

    private static final String TAG = GeofenceTransitionService.class.getSimpleName();

    public static final int GEOFENCE_NOTIFICATION_ID = 0;
    String CHANNEL_ID = "my_channel_01";
    CharSequence name = "my_channel";
    String Description = "This is my channel";

    int NOTIFICATION_ID = 234;
    public GeofenceTransitionService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        // Handling errors
        if ( geofencingEvent.hasError() ) {
            String errorMsg = getErrorString(geofencingEvent.getErrorCode() );
            Log.e( TAG, errorMsg );
            return;
        }

        System.out.println("messageeeeee");

        int geoFenceTransition = geofencingEvent.getGeofenceTransition();
        // Check if the transition type is of interest
        if ( geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geoFenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT ) {
            // Get the geofence that were triggered
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            String geofenceTransitionDetails = getGeofenceTransitionDetails(geoFenceTransition, triggeringGeofences );

            // Send notification details as a String
            sendNotification( geofenceTransitionDetails );
        }
    }

    private String getGeofenceTransitionDetails(int geoFenceTransition, List<Geofence> triggeringGeofences) {
        // get the ID of each geofence triggered
        ArrayList<String> triggeringGeofencesList = new ArrayList<>();
        for ( Geofence geofence : triggeringGeofences ) {
            triggeringGeofencesList.add( geofence.getRequestId() );
        }

        String status = null;
        if ( geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER )
            status = "Entering ";
        else if ( geoFenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT )
            status = "Exiting ";
        return status + TextUtils.join( ", ", triggeringGeofencesList);
    }

    private void sendNotification( String msg ) {
        Log.i(TAG, "sendNotification: " + msg );

        // Intent to start the main Activity
//        Intent notificationIntent = MainActivity.makeNotificationIntent(
//                getApplicationContext(), msg
//        );

//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        stackBuilder.addParentStack(MainActivity.class);
//        stackBuilder.addNextIntent(notificationIntent);
//        PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        Intent resultIntent = new Intent(this, Menu.class);
        android.support.v4.app.TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(Menu.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);



        // Creating and sending Notification
        NotificationManager notificatioMng =
                (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
        notificatioMng.notify(
                GEOFENCE_NOTIFICATION_ID,
                createNotification(msg, resultPendingIntent));

    }

    // Create notification
    private Notification createNotification(String msg, PendingIntent notificationPendingIntent) {

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(true);

            if (notificationManager != null) {

                notificationManager.createNotificationChannel(mChannel);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("")
                .setContentText("")
//                .setStyle(new NotificationCompat.BigTextStyle().bigText("Notice that the NotificationCompat.Builder constructor requires that you provide a channel ID. This is required for compatibility with Android 8.0 (API level 26) and higher, but is ignored by older versions By default, the notification's text content is truncated to fit one line. If you want your notification to be longer, you can enable an expandable notification by adding a style template with setStyle(). For example, the following code creates a larger text area"))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true)
                .setColor(getResources().getColor(android.R.color.holo_red_dark));
//                )
//                .addAction(R.drawable.ic_launcher_foreground, "Call", notificationPendingIntent)
//                .addAction(R.drawable.ic_launcher_foreground, "More", notificationPendingIntent)
//                .addAction(R.drawable.ic_launcher_foreground, "And more", notificationPendingIntent);


//        if (notificationManager != null) {
//
//            notificationManager.notify(NOTIFICATION_ID, builder.build());
//        }

        return builder.build();

//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
//        notificationBuilder
//                .setSmallIcon(R.drawable.ic_action_location)
//                .setColor(Color.RED)
//                .setContentTitle(msg)
//                .setContentText("Geofence Notification!")
//                .setContentIntent(notificationPendingIntent)
//                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
//                .setAutoCancel(true);
//        return notificationBuilder.build();
    }

    private static String getErrorString(int errorCode) {
        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return "GeoFence not available";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return "Too many GeoFences";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return "Too many pending intents";
            default:
                return "Unknown error.";
        }
    }
}