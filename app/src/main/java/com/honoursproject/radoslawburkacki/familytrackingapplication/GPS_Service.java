package com.honoursproject.radoslawburkacki.familytrackingapplication;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks.SendCoordinatesTask;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.Family;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.User;


public class GPS_Service extends Service implements SendCoordinatesTask.AsyncResponse {

    public static final String MY_PREFS_NAME = "MyPrefsFile";

    private LocationListener listener;
    private LocationManager locationManager;
    private Long userid;
    private String token;
    private Long familyid;

    private Intent ii;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    private Family getFamilyFromSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("Family", "");
        Family f = gson.fromJson(json, Family.class);
        return f;
    }

    @Override
    public void onCreate() {
        SharedPreferences prefs;
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        this.token = prefs.getString("token", null);
        User user = new User(prefs.getLong("userid", 0), prefs.getString("email", null), "", prefs.getString("fname", null), prefs.getString("lname", null));
        this.userid = user.getId();
        Family f = getFamilyFromSharedPreferences();
        familyid = f.getId();

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Intent i = new Intent("location_update");
                i.putExtra("Lat", location.getLatitude());
                i.putExtra("Long", location.getLongitude());

                SendCoordinates(new LatLng(location.getLatitude(), location.getLongitude()));

                sendBroadcast(i);


            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {


                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, listener);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            //noinspection MissingPermission
            locationManager.removeUpdates(listener);
        }
    }


    public void SendCoordinates(LatLng latLng) {
        new SendCoordinatesTask(this, latLng, token, userid, familyid).execute();
    }

    @Override
    public void processFinish() {

    }
}
