package com.eagle.cansacaredoctor.ressources;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.eagle.cansacaredoctor.MainActivity;
import com.eagle.cansacaredoctor.R;


public class PresentNotifications {
    private static final String CHANNEL_ID = "Doctors";


//    public void PresentNotification(Context context, String title, String message) {
//
//        // Create an explicit intent for an Activity in your app
//        Intent intent = new Intent(context, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setContentTitle(title)
//                .setContentText(message)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setContentIntent(pendingIntent);
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//
////// notificationId is a unique int for each notification that you must define
//        notificationManager.notify(100, builder.build());
//
//
//    }
}


