package com.example.myapplication;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static com.example.myapplication.MainActivity.CHANNEL_ID;

public class NotificationHelper {


    public static void displayNotification(Context context,String title,String body){
        Intent intent=new Intent(context,MainActivity2.class);
        PendingIntent pendingIntent= PendingIntent.getActivity(context,
                100,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder mBuilder= new NotificationCompat.Builder(context,CHANNEL_ID)
                .setSmallIcon(R.drawable.chessbase)
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1,mBuilder.build());

    }
}
