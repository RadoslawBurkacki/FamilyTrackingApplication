package com.honoursproject.radoslawburkacki.familytrackingapplication;


import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.os.Bundle;

import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.*;
import android.widget.Toast;


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks.GetFamilyMemberLocation;
import com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks.GetFamilyTask;
import com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks.SendFCMTokenTask;
import com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks.SendSOSTask;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.Family;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.User;
import com.honoursproject.radoslawburkacki.familytrackingapplication.fcm.MyFirebaseInstanceIDService;
import com.honoursproject.radoslawburkacki.familytrackingapplication.fcm.MyFirebaseMessagingService;

import java.util.HashMap;
import java.util.List;

public class Map extends AppCompatActivity implements OnMapReadyCallback, GetFamilyTask.AsyncResponse, GetFamilyMemberLocation.AsyncResponse, SendSOSTask.AsyncResponse, NavigationView.OnNavigationItemSelectedListener {
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private static final String TAG = "MapActivity";
    private static final float DEFAULT_ZOOM = 15f;


    private GoogleMap mMap;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private BroadcastReceiver broadcastReceiver;

    private boolean firstRunCentred = false;
    private Family family;
    private User user;
    private String token;

    SharedPreferences prefs;
    HashMap<Long, Marker> markerList = new HashMap<Long, Marker>();

    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        token = prefs.getString("token", null);

        user = new User(prefs.getLong("userid", 0), prefs.getString("email", null), "", prefs.getString("fname", null), prefs.getString("lname", null));

        navigationView = (NavigationView) findViewById(R.id.navigationview);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Hello " + user.getFname());

        menu = navigationView.getMenu();
        setNavigationViewListner();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getFamily();

        Log.d("\n", FirebaseInstanceId.getInstance().getToken());

        SendFcmToken(user.getId(), token, FirebaseInstanceId.getInstance().getToken());


    }

    private void startLocationService() {

        Intent i = new Intent(getApplicationContext(), GPS_Service.class);
        i.putExtra("userid", user.getId());
        i.putExtra("token", token);
        i.putExtra("familyid", family.getId());
        startService(i);

    }

    private boolean runtime_permissions() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);

            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                startLocationService();
            } else {
                runtime_permissions();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    Log.d("aaa", intent.getAction().toString());


                    if (intent.getAction().toString().equals("location_update")) {
                        if (!firstRunCentred) {
                            moveCamera(new LatLng((Double) intent.getExtras().get("Lat"), (Double) intent.getExtras().get("Long")), DEFAULT_ZOOM);

                            Marker marker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng((Double) intent.getExtras().get("Lat"), (Double) intent.getExtras().get("Long")))
                                    .title(user.getFname() + " " + user.getFname())
                                    .snippet("xx"));
                            marker.setVisible(true);

                            markerList.put(user.getId(), marker);
                            firstRunCentred = true;
                        }

                    } else if (intent.getAction().toString().equals("newUserJoinedFamily")) {

                        getFamily();
                    }


                }
            };
        }
        Log.d("aaa", "register new receiver");
        IntentFilter filterRefreshUpdate = new IntentFilter();
        filterRefreshUpdate.addAction("newUserJoinedFamily");
        filterRefreshUpdate.addAction("location_update");

        registerReceiver(broadcastReceiver, filterRefreshUpdate);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

    private void moveCamera(LatLng latLng, float zoom) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.nav_chat:

                Intent intent = new Intent(Map.this, PreChat.class);
                intent.putExtra("user", user);
                intent.putExtra("family", family);
                startActivity(intent);

                break;

            case R.id.nav_SOS:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                sendSOS();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

                break;

            case R.id.nav_myfamily:

                Intent intentFamilyDetails = new Intent(Map.this, FamilyDetails.class);
                intentFamilyDetails.putExtra("family", family);
                startActivity(intentFamilyDetails);

                break;

            case R.id.nav_settings:

                break;

            case R.id.nav_about:

                break;

            case R.id.nav_signout:

                break;

            default:

                getFamilyMemberLocation(item.getItemId());

                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private void setNavigationViewListner() {
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveFamilyToSharedPreferences(Family f) {
        SharedPreferences.Editor prefsEditor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(f);
        prefsEditor.putString("Family", json);
        prefsEditor.commit();
    }

    private Family getFamilyFromSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("Family", "");
        Family f = gson.fromJson(json, Family.class);
        return f;
    }

    public void setUpDrawerMenu() {
        MenuItem myMoveGroupItem = navigationView.getMenu().getItem(0);
        SubMenu subMenu = myMoveGroupItem.getSubMenu();
        subMenu.clear();

        Family f = getFamilyFromSharedPreferences();

        for (User u : f.getFamilyMembers()) {
              if (u.getId() == user.getId()) {
                subMenu.add(Menu.NONE, (int) u.getId(), Menu.NONE, u.getFname() + " " + u.getLname() + " (Me)").setIcon(R.drawable.ic_gps_fixed_black_24dp);
                continue;
            }

            subMenu.add(Menu.NONE, (int) u.getId(), Menu.NONE, u.getFname() + " " + u.getLname()).setIcon(R.mipmap.ic_location_on_black_24dp);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private boolean isMyServiceRunning(Class<?> GPS_Service) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (GPS_Service.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    // Below this line is the AsyncTask section
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    public void SendFcmToken(Long userid, String token, String FCMtoken) {
        new SendFCMTokenTask(userid, token, FCMtoken).execute();
    }

    public void getFamily() {
        new GetFamilyTask(this, user, token).execute();
    }

    @Override
    public void processFinish(Family f) { // get family result
        this.family = f;
        Log.d(TAG, family.toString());

        if (f.getFamilyMembers().size() != 0) { // family has atleast 1 family member

            saveFamilyToSharedPreferences(f);

            if (isMyServiceRunning(GPS_Service.class)) {
                Log.d(TAG, "gps service on");
            } else {
                Log.d(TAG, "gps service off");
                if (!runtime_permissions()) {
                    startLocationService();
                    Log.d(TAG, "Starting location service");
                }
            }

            Log.d(TAG, "Setting up drawer menu");

            setUpDrawerMenu();

        }
    }

    public void getFamilyMemberLocation(long familyMemberId) {
        new GetFamilyMemberLocation(this, token, familyMemberId).execute();
    }

    @Override
    public void processFinish(int statuscode, LatLng coordinates, long familyMemberId, List<Integer> list) {  // Gett family member location result
        String trackedUserName = "";

        for (User u : family.getFamilyMembers()) {
            if (u.getId() == familyMemberId) {
                trackedUserName = u.getFname() + " " + u.getLname();
            }
        }

        if (statuscode == 302) {
            moveCamera(coordinates, DEFAULT_ZOOM);

            if (markerList.containsKey(familyMemberId)) { // check if marker for user x is already in the HashMap
                markerList.get(familyMemberId).setVisible(false);   // if true then remove marker
            }

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(coordinates.latitude, coordinates.longitude))
                    .title(trackedUserName)
                    .snippet("Last seen:" + list.get(0) +list.get(1) + list.get(2) +list.get(3) + list.get(4) + list.get(5)));
            marker.setVisible(true);

            markerList.put(familyMemberId, marker); // add marker to hash list

            marker.showInfoWindow();

        } else if (statuscode == 404) {
            Toast.makeText(this, "We cant locate this user",
                    Toast.LENGTH_LONG).show();
        }


    }


    public void sendSOS() {
        new SendSOSTask(this, user, token).execute();
    }

    @Override
    public void processFinish(Integer statuscode) {
        if (statuscode == 200) {
            Toast.makeText(this, "SOS sent successfully",
                    Toast.LENGTH_LONG).show();
        }

    }


}






