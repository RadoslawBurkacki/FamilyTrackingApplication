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
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.*;
import android.widget.ImageButton;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks.GetFamilyMemberLocationTask;
import com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks.GetFamilyTask;
import com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks.SendSOSTask;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.Family;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.User;

import java.util.HashMap;
import java.util.List;

public class Map extends AppCompatActivity implements OnMapReadyCallback, GetFamilyTask.AsyncResponse, GetFamilyMemberLocationTask.AsyncResponse, SendSOSTask.AsyncResponse, NavigationView.OnNavigationItemSelectedListener {
    public static final String MY_PREFS_NAME = "FamilyCentreApplicationPrefFile";

    private static final String TAG = "MapActivity";
    private static final float DEFAULT_ZOOM = 15f;

    private ImageButton changeMaptype;
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
    SharedPreferences.Editor editor;

    HashMap<Long, Marker> markerList = new HashMap<Long, Marker>();

    Menu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();


        changeMaptype = (ImageButton) findViewById(R.id.changeMapType);


        token = prefs.getString("token", null);

        user = new User(prefs.getLong("userid", 0), prefs.getString("email", null), "", prefs.getString("fname", null), prefs.getString("lname", null));

        navigationView = (NavigationView) findViewById(R.id.navigationview);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(getResources().getString(R.string.hello) + " " + user.getFname());

        menu = navigationView.getMenu();
        setNavigationViewListner();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getFamily();


        changeMaptype.setOnClickListener(new View.OnClickListener() { // action listener for login button
            @Override
            public void onClick(View view) { // when login button is pressed then...
                if (mMap.getMapType() == GoogleMap.MAP_TYPE_HYBRID) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    editor.putString("maptype", "GoogleMap.MAP_TYPE_NORMAL");
                    changeMaptype.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.ic_satellite_black_24dp));

                } else {
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    editor.putString("maptype", "GoogleMap.MAP_TYPE_HYBRID");
                    // changeMaptype.setImageResource(android.R.drawable.alert_light_frame);
                    // changeMaptype.setImageResource(R.mipmap.ic_satellite_white_24dp);
                    changeMaptype.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.ic_satellite_white_24dp));
                }
                editor.commit();
            }
        });

        if (prefs.getString("maptype", "a").equals("GoogleMap.MAP_TYPE_HYBRID"))
            changeMaptype.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.ic_satellite_white_24dp));
        else
            changeMaptype.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.ic_satellite_black_24dp));


    }

    private void startLocationService() {

        // Intent i = new Intent(getApplicationContext(), GPS_Service.class);
        // startService(i);

        Intent i = new Intent(getApplicationContext(), FusedLocation_Service.class);
        startService(i);
        moveCamera(new LatLng(55.8751597, -4.2636893), DEFAULT_ZOOM);


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
                                    .icon(BitmapDescriptorFactory.defaultMarker(getMarkerColour((int) user.getId())))
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

        if (prefs.contains("maptype")) {
            String a = prefs.getString("maptype", null);
            if (a.equals("GoogleMap.MAP_TYPE_HYBRID")) {
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            }
        }
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
                builder.setMessage(getResources().getString(R.string.areyousure)).setPositiveButton(getResources().getString(R.string.yes), dialogClickListener)
                        .setNegativeButton(getResources().getString(R.string.no), dialogClickListener).show();

                break;

            case R.id.nav_myfamily:

                Intent intentFamilyDetails = new Intent(Map.this, FamilyDetails.class);
                intentFamilyDetails.putExtra("family", family);
                intentFamilyDetails.putExtra("user", user);
                startActivity(intentFamilyDetails);

                break;


            case R.id.nav_settings:
                Intent settings = new Intent(Map.this, SettingsMenu.class);

                startActivity(settings);

                break;

            case R.id.nav_about:


                break;

            case R.id.nav_signout:

                DialogInterface.OnClickListener dialogClickListenerSignOut = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:

                                stopService(new Intent(getApplicationContext(), FusedLocation_Service.class));
                                SharedPreferences.Editor editor = prefs.edit();

                                editor.remove("token");
                                editor.remove("family");
                                editor.remove("userid");
                                editor.remove("email");
                                editor.remove("token");
                                editor.remove("fname");
                                editor.remove("lname");


                                editor.commit();
                                finish();
                                System.exit(0);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };

                AlertDialog.Builder builderSignOut = new AlertDialog.Builder(this);
                builderSignOut.setMessage(getResources().getString(R.string.logoutmsg)).setPositiveButton(getResources().getString(R.string.yes), dialogClickListenerSignOut)
                        .setNegativeButton(getResources().getString(R.string.no), dialogClickListenerSignOut).show();


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

    public String getLastSeen(List<Integer> lastseenList) {
        //lastseenList = [x(year),x(month),x(day),x(hour),x(min),x(sec)]

        if (lastseenList.get(0) > 0) { // user has been seen atleast year ago
            return getString(R.string.last_seen) + lastseenList.get(0) + " " + getString(R.string.years) + " " + getString(R.string.ago);
        } else if (lastseenList.get(1) > 0) {   // user has been seen atleast month ago
            return getString(R.string.last_seen) + lastseenList.get(1) + " " + getString(R.string.months) + " " + getString(R.string.ago);
        } else if (lastseenList.get(2) > 0) {   // user has been seen atleast month ago
            return getString(R.string.last_seen) + lastseenList.get(2) + " " + getString(R.string.days) + " " + getString(R.string.ago);
        } else if (lastseenList.get(3) > 0) {   // user has been seen atleast month ago
            return getString(R.string.last_seen) + lastseenList.get(3) + " " + getString(R.string.hours) + " " + getString(R.string.ago);
        } else if (lastseenList.get(4) > 0) {   // user has been seen atleast month ago
            return getString(R.string.last_seen) + lastseenList.get(4) + " " + getString(R.string.minutes) + " " + getString(R.string.ago);
        } else {
            return getString(R.string.last_seen) + " " + getString(R.string.now);
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

    private boolean isMyServiceRunning(Class<?> FusedLocation_Service) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (FusedLocation_Service.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private Float getMarkerColour(int userid) {

        String a = "colorForUser:" + userid;
        Log.d("aaaa", a);


        int i = prefs.getInt(a, 6);
        Log.d("aaaa", i + "");
        switch (i) {
            case 0:
                return BitmapDescriptorFactory.HUE_AZURE;
            case 1:
                return BitmapDescriptorFactory.HUE_BLUE;
            case 2:
                return BitmapDescriptorFactory.HUE_CYAN;
            case 3:
                return BitmapDescriptorFactory.HUE_GREEN;
            case 4:
                return BitmapDescriptorFactory.HUE_MAGENTA;
            case 5:
                return BitmapDescriptorFactory.HUE_ORANGE;
            case 6:
                return BitmapDescriptorFactory.HUE_RED;
            case 7:
                return BitmapDescriptorFactory.HUE_ROSE;
            case 8:
                return BitmapDescriptorFactory.HUE_VIOLET;
            case 9:
                return BitmapDescriptorFactory.HUE_YELLOW;

        }
        return (float) 6;
    }

    // Below this line is the AsyncTask section
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    public void getFamily() {
        new GetFamilyTask(this, user, token).execute();
    }   // Start getFamily Async Task

    @Override
    public void processFinish(Family f, int statuscode) { // get family result

        if (statuscode != 0) {

            this.family = f;
            Log.d(TAG, family.toString());

            if (f.getFamilyMembers().size() != 0) { // family has atleast 1 family member

                saveFamilyToSharedPreferences(f);

                if (isMyServiceRunning(FusedLocation_Service.class)) {
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

            } else
                Toast.makeText(this, getResources().getText(R.string.errnetworkproblem),
                        Toast.LENGTH_LONG).show();

        }
    }   // return from getFamily Async Task

    public void getFamilyMemberLocation(long familyMemberId) {
        new GetFamilyMemberLocationTask(this, token, familyMemberId).execute();
    }  // Start getFamilyMemberLocation Async Task

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
                    .icon(BitmapDescriptorFactory.defaultMarker(getMarkerColour((int) familyMemberId)))
                    .snippet(getLastSeen(list)));
            marker.setVisible(true);

            markerList.put(familyMemberId, marker); // add marker to hash list

            marker.showInfoWindow();

        } else if (statuscode == 404) {
            Toast.makeText(this, getResources().getString(R.string.errcantlocate),
                    Toast.LENGTH_LONG).show();
        }


    }   // return from getFamilyMemberLocation Async Task

    public void sendSOS() {
        new SendSOSTask(this, user, token).execute();
    } // Start SendSOS Async Task

    @Override
    public void processFinish(Integer statuscode) {
        if (statuscode == 200) {
            Toast.makeText(this, getResources().getString(R.string.sossent),
                    Toast.LENGTH_LONG).show();
        }

    }   // SensSOS return


}






