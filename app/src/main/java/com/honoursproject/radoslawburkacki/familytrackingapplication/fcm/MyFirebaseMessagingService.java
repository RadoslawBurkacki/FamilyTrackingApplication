package com.honoursproject.radoslawburkacki.familytrackingapplication.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Chat;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Database.dbHandler;
import com.honoursproject.radoslawburkacki.familytrackingapplication.MainActivity;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Map;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.Family;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.Message;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.User;
import com.honoursproject.radoslawburkacki.familytrackingapplication.PreChat;
import com.honoursproject.radoslawburkacki.familytrackingapplication.R;

/**
 * Created by radek on 02/02/2018.
 */

public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private LocalBroadcastManager broadcaster;
    private dbHandler db;

    @Override
    public void onCreate() {
        db = new dbHandler(this);
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d("FCM", "data received from FCM");

        if (remoteMessage.getData().size() > 0) { // data has been sent

            if (remoteMessage.getData().containsKey("fromid") && remoteMessage.getData().containsKey("toid")
                    && remoteMessage.getData().containsKey("date")
                    && remoteMessage.getData().containsKey("message")) {  // its a private chat message
                Log.d("FCM", "PM received");
                /*
                1. check if current activity is the chat(with this sender) if true then...
                    - Display top notification new message...
                    - pass data

                2. if current activity is not a chat(with this sender)
                    - Display top notification
                    - if notification clicked then open chat

                 */

                Message m = new Message((long) 0, Long.parseLong(remoteMessage.getData().get("fromid")), Long.parseLong(remoteMessage.getData().get("toid")), remoteMessage.getData().get("message"), remoteMessage.getData().get("date"));
                db.addMessage(m);
               // Log.d("FCM", "sending broadcast to Chat");
              //  Intent intent = new Intent("newmessage");
              //  intent.putExtra("message", "a");
              //  broadcaster.sendBroadcast(intent);

                Intent intent = new Intent("newmessage");
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

                //Intent intent = new Intent(this, Chat.class); // instance id? for recoognition of each unique chat??
                //intent.putExtra("user", remoteMessage.getData().containsKey("toid"));
                //intent.putExtra("receiver", remoteMessage.getData().containsKey("fromid"));
                //startActivity(intent);

                showPMNotification(remoteMessage.getData().get("message"), remoteMessage.getData().get("fromid"), remoteMessage.getData().get("toid"), remoteMessage.getData().get("date"));

            }
        }

        //showNotification(remoteMessage.getData().get("message"));
    }


    private void showPMNotification(String message, String senderid, String toid, String date) {


        User sender = new User();
        User user = new User();

        Log.d("FCM", "Displaying PM Notification");

        Family f = getFamilyFromSharedPreferences();

        for (User u : f.getFamilyMembers()) {
            if (u.getId() == Long.parseLong(senderid)) {// found sender
                message = u.getFname() + " " + u.getLname() + ": " + message; // generates notification message "fname lname: message"
                sender = u;
            }
            if (u.getId() == Long.parseLong(toid)) {// user
                user = u;
            }

        }

        Intent i = new Intent(this, Chat.class);

        i.putExtra("user", user);
        i.putExtra("receiver", sender);


        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("New chat message received")
                .setContentText(message)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0, builder.build());

    }

    private Family getFamilyFromSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("Family", "");
        Family f = gson.fromJson(json, Family.class);
        return f;
    }


}