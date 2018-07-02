package com.bongo.pushservice;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bongo.pushservice.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import java.util.Random;

import static android.app.PendingIntent.FLAG_IMMUTABLE;

public class FirebaseMessageRecvService extends FirebaseMessagingService {
    private static final String TAG = "FirebaseMessageRecvServ";
    private static int notificationId;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
            notificationId= genNotificationId();
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            setNotificationToService(remoteMessage.getData().get("message"),remoteMessage.getData().get("title"),getIntent());
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            //showNotification(remoteMessage.getData().toString());
            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {

            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }


    private PendingIntent getIntent(){
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return PendingIntent.getActivity(getApplicationContext(), notificationId, intent,
                FLAG_IMMUTABLE);

    }

    public void setNotificationToService(String messageBody,String title, PendingIntent pendingIntent){
        final NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
        notiStyle.setBigContentTitle(messageBody);
       // notiStyle.setSummaryText(text);
       // notiStyle.bigPicture(bitmap);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//       int icon = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? R.drawable.bioscopenew : R.drawable.bioscopenew2;
        int icon = R.drawable.ic_launcher_background;
        NotificationCompat.Builder notificationBuilder;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationBuilder = new NotificationCompat.Builder(this, MainActivity.CHANNEL_ID);
        } else {
            notificationBuilder = new NotificationCompat.Builder(this);
        }
        notificationBuilder.setContentTitle(title)
                .setContentText(messageBody)
                .setSmallIcon(icon)
              //  .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.bioscopenew2))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
              //  .setColor(ContextCompat.getColor(this, R.color.color_notification_icon))
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

       // if(bitmap!=null)notificationBuilder.setStyle(notiStyle);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notificationId, notificationBuilder.build());


      //  Log.d(TAG, "pendingIntent() called with: messageBody = [" + messageBody + "], text = [" + text + "], image = [" + bitmap + "], notificationId = [" + notificationId + "]");
    }
    private int genNotificationId(){
        Random ran = new Random();
        int x = ran.nextInt(Integer.MAX_VALUE);
        return x;
    }
    private void scheduleJob() {
        Log.d(TAG, "scheduleJob() called");
    }
    private void handleNow(){
        Log.d(TAG, "handleNow() called");
    }
}
