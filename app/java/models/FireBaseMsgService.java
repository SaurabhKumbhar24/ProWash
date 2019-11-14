package models;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import android.content.Context;

import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import Pro.Wash.R;

public class FireBaseMsgService  extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        assert remoteMessage.getNotification()!=null;
        sendNotification(remoteMessage.getNotification().getTitle(),
                remoteMessage.getNotification().getBody());

    }
    private void sendNotification(String title , String message){

        int notifyID = 1;
        String CHANNEL_ID = "Pro Wash";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, "Pro",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mChannel.setLightColor(R.color.MediumBlue);
            mChannel.enableLights(true);
            mChannel.enableVibration(true);
            mChannel.setSound(defaultSoundUri,null);
            mChannel.setImportance(NotificationManager.IMPORTANCE_HIGH);
            mChannel.setVibrationPattern(new long[]{100, 100, 100, 100});

            Notification notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.notification)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon))
                    .setChannelId(CHANNEL_ID)
                    .build();

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(mChannel);
            mNotificationManager.notify(notifyID , notification);
        }
        else{
            Notification notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.icon)
                    .setVibrate(new long[]{100, 100, 100, 100})
                    .setPriority(Notification.PRIORITY_MAX)
                    .setSound(defaultSoundUri)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon))
                    .build();

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert mNotificationManager != null;
            mNotificationManager.notify(notifyID , notification);
        }
    }
}
