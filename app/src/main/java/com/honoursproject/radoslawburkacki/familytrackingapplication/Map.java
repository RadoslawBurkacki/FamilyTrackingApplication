package com.honoursproject.radoslawburkacki.familytrackingapplication;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.location.Location;
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
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.api.GoogleApiClient;



import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks.GetFamilyTask;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.Family;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.User;


public class Map extends AppCompatActivity implements OnMapReadyCallback, GetFamilyTask.AsyncResponse, NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    private GoogleMap mMap;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private GoogleApiClient googleApiClient;



    private Family family;
    private User user;
    private String token;
    private Boolean mLocationPermissionsGranted = false;



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
    }





    private void moveCamera(LatLng latLng, float zoom) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera52.256334, 21.052733
        LatLng sydney = new LatLng(52.256334, 21.052733);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.nav_chat) {

            Intent intent = new Intent(Map.this, PreChat.class);
            intent.putExtra("user",user);
            intent.putExtra("token", token);
            intent.putExtra("family", family);
            startActivity(intent);

        }
        if (id == R.id.nav_SOS) {

        }
        if (id == R.id.nav_myfamily) {

        }
        if (id == R.id.nav_settings) {

        }
        if (id == R.id.nav_about) {

        }
        if (id == R.id.nav_signout) {

        } else { // user tracking request, id is the user id that tracking is requested
            //need to do call to the server requesting the location of user (id)
            Log.d(TAG,""+item.getItemId() + item.getTitle());

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

        Log.d(TAG,f.toString());

        MenuItem myMoveGroupItem = navigationView.getMenu().getItem(0);
        SubMenu subMenu = myMoveGroupItem.getSubMenu();


        if (f.getFamilyMembers().size() != 0) {
            for (User u : f.getFamilyMembers()) {


                subMenu.add(Menu.NONE, (int) u.getId(), Menu.NONE, u.getFname() + " " + u.getLname());
            }
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


