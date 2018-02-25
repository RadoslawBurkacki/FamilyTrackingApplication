package com.honoursproject.radoslawburkacki.familytrackingapplication;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;

import com.google.android.gms.location.LocationListener;

import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks.SendCoordinatesTask;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.Family;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.User;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class FusedLocation_Service extends Service implements SendCoordinatesTask.AsyncResponse, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private GoogleApiClient mLocationClient;
    private Location mCurrentLocation;
    LocationRequest mLocationRequest;

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private Long userid;
    private String token;
    private Long familyid;
    boolean firsStartCentred = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mLocationClient = new GoogleApiClient.Builder(FusedLocation_Service.this)
                .addApi(LocationServices.API).addConnectionCallbacks(FusedLocation_Service.this)
                .addOnConnectionFailedListener(FusedLocation_Service.this).build();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(6000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setFastestInterval(3000);
        mLocationClient.connect();
        return Service.START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("aaaa", "service 2 started");
        SharedPreferences prefs;
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        this.token = prefs.getString("token", null);
        User user = new User(prefs.getLong("userid", 0), prefs.getString("email", null), "", prefs.getString("fname", null), prefs.getString("lname", null));
        this.userid = user.getId();
        Family f = getFamilyFromSharedPreferences();
        familyid = f.getId();

    }

    @Override
    public void onLocationChanged(Location location) {
        if (mCurrentLocation != null)
            if (mCurrentLocation.getLongitude() == location.getLongitude() && mCurrentLocation.getLatitude() == location.getLatitude()) {
                SendCoordinates(new LatLng(location.getLatitude(), location.getLongitude()));
                Log.d("aaaa", "sending loc update");
            } else
                Log.d("aaaa", "not sending loc update as same as prev");

        mCurrentLocation = location;

        if (!firsStartCentred) {
            Intent i = new Intent("location_update");
            i.putExtra("Lat", location.getLatitude());
            i.putExtra("Long", location.getLongitude());
            sendBroadcast(i);
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
    }

    @Override
    public void onConnected(Bundle arg0) {
        LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mLocationClient.connect();
    }

    @Override
    public void processFinish() {

    }

    public void SendCoordinates(LatLng latLng) {
        new SendCoordinatesTask(this, latLng, token, userid, familyid).execute();
    }

    private Family getFamilyFromSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("Family", "");
        Family f = gson.fromJson(json, Family.class);
        return f;
    }


}
