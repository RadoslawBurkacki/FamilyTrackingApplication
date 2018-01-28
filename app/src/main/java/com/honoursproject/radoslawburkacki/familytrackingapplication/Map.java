package com.honoursproject.radoslawburkacki.familytrackingapplication;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.*;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.api.GoogleApiClient;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks.GetFamilyMemberLocation;
import com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks.GetFamilyTask;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.Family;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.User;

public class Map extends AppCompatActivity implements OnMapReadyCallback, GetFamilyTask.AsyncResponse, GetFamilyMemberLocation.AsyncResponse, NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MapActivity";
    private static final float DEFAULT_ZOOM = 15f;

    private GoogleMap mMap;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    private boolean firstRunCentred = false;
    private Family family;
    private User user;
    private String token;

    private BroadcastReceiver broadcastReceiver;

    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent i = getIntent();
        user = (User) i.getSerializableExtra("user");
        token = (String) i.getSerializableExtra("token");

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


/*

        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // Check if enabled and if not send user to the GPS settings
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
        */
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

                    if (firstRunCentred) {

                    } else if (!firstRunCentred) {
                        moveCamera(new LatLng((Double) intent.getExtras().get("Lat"), (Double) intent.getExtras().get("Long")), DEFAULT_ZOOM);

                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng((Double) intent.getExtras().get("Lat"), (Double) intent.getExtras().get("Long")))
                                .title(user.getFname() + " " + user.getFname())
                                .snippet("xx"));
                        marker.setVisible(true);

                        firstRunCentred = true;
                    }
                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("location_update"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }


    private void moveCamera(LatLng latLng, float zoom) {
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.nav_chat:

                Intent intent = new Intent(Map.this, PreChat.class);
                intent.putExtra("user", user);
                intent.putExtra("token", token);
                intent.putExtra("family", family);
                startActivity(intent);

                break;

            case R.id.nav_SOS:

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


    public void getFamily() {
        new GetFamilyTask(this, user, token).execute();
    }

    @Override
    public void processFinish(Family f) {
        this.family = f;

        Log.d(TAG, family.toString());

        MenuItem myMoveGroupItem = navigationView.getMenu().getItem(0);
        SubMenu subMenu = myMoveGroupItem.getSubMenu();


        if (f.getFamilyMembers().size() != 0) {

            Log.d(TAG, "Setting up drawer menu");

            for (User u : f.getFamilyMembers()) {

                if (u.getId() == user.getId()) {
                    //subMenu.add(Menu.NONE, (int) u.getId(), Menu.NONE, u.getFname() + " " + u.getLname() + " (Me)");
                    continue;
                }

                subMenu.add(Menu.NONE, (int) u.getId(), Menu.NONE, u.getFname() + " " + u.getLname());
            }

            if (!runtime_permissions()) {
                startLocationService();
                Log.d(TAG, "Starting location service");
            }
        }
    }


    public void getFamilyMemberLocation(long familyMemberId) {
        new GetFamilyMemberLocation(this, token, familyMemberId).execute();
    }


    @Override
    public void processFinish(int statuscode, LatLng coordinates, long familyMemberId) {

        if (statuscode == 302) {
            moveCamera(coordinates, DEFAULT_ZOOM);

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(coordinates.latitude, coordinates.longitude))
                    .title(family.getFamilyMembers().get((int) familyMemberId - 1).getFname() + " " + family.getFamilyMembers().get((int) familyMemberId - 1).getLname())
                    .snippet("xx"));
            marker.setVisible(true);


            marker.showInfoWindow();

        } else if (statuscode == 404) {
            Toast.makeText(this, "We cant locate this user",
                    Toast.LENGTH_LONG).show();
        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}






