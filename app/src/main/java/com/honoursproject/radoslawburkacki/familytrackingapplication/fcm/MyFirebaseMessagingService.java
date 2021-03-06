package com.honoursproject.radoslawburkacki.familytrackingapplication.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
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
    public static final String MY_PREFS_NAME = "FamilyCentreApplicationPrefFile";
    private LocalBroadcastManager broadcaster;
    private dbHandler db;
    User user;
    SharedPreferences prefs;

    @Override
    public void onCreate() {
        db = new dbHandler(this);
        broadcaster = LocalBroadcastManager.getInstance(this);

        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        user = new User(prefs.getLong("userid", 0), prefs.getString("email", null), "", prefs.getString("fname", null), prefs.getString("lname", null));
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d("FCM", "data received from FCM");

        if (remoteMessage.getData().size() > 0) { // data has been sent
            playSoundandVib();
            Log.d("FCM", remoteMessage.getData().toString());
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
            else if(remoteMessage.getData().containsKey("newfamilymember")){
                showNewFamilyMemberNotification(remoteMessage.getData().get("newfamilymember"));
                Log.d("FCM", "sending broadcast to map");
                Intent z = new Intent("newUserJoinedFamily");
                sendBroadcast(z);
            }
            else if(remoteMessage.getData().containsKey("sos")){
                showSOSnotification(remoteMessage);
            }
            else if(remoteMessage.getData().containsKey("firstname") &&remoteMessage.getData().containsKey("lastname") &&remoteMessage.getData().containsKey("userid") ){
                showFamilyMemberRemovedNotification(remoteMessage);
                if(user.getId()==Long.parseLong(remoteMessage.getData().get("userid"))){
                    Intent z = new Intent("ihavebeenremoved");
                    sendBroadcast(z);
                }else{
                    Intent z = new Intent("userremoved");
                    sendBroadcast(z);
                }

            }
        }

    }

    private void showFamilyMemberRemovedNotification(RemoteMessage rm){
        String msg="User has been removed from family:";
        String msg2=rm.getData().get("firstname") +" " +rm.getData().get("lastname");
        if(user.getId()==Long.parseLong(rm.getData().get("userid"))){
            msg ="You have been removed from family";
            msg2 = "";
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(msg)
                .setContentText(msg2 )
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0, builder.build());
    }

    private void playSoundandVib(){
        try {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(500);

            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showSOSnotification(RemoteMessage remoteMessage){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(getResources().getText(R.string.sosnotification))
                .setContentText(remoteMessage.getData().get("sos"))
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0, builder.build());

    }

    private void showNewFamilyMemberNotification(String newFamilyMemberName) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(getResources().getText(R.string.newmembernotification))
                .setContentText(newFamilyMemberName)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0, builder.build());
    }

    private void showPMNotification(String message, String senderid, String toid, String date) {

        User sender = new User();
        User user = new User();

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
                .setContentTitle(getResources().getText(R.string.newchatmessagenotifcation))
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