package com.example.asus.myapplication.FirebaseMessageService;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Set;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e("fcm message", remoteMessage.getNotification().getBody());

        Set<String> keys = remoteMessage.getData().keySet();
        for(String s:keys) {
            Log.e("fcm data:", remoteMessage.getData().get(s));
        }
    }
}
