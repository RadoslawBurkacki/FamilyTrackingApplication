package com.honoursproject.radoslawburkacki.familytrackingapplication;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import com.google.android.gms.maps.model.LatLng;
import com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks.SendCoordinatesTask;

public class CheckForUpdate_Service extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public void onCreate() {
        /*
         this service must be started with map activity

         used for: checking if there are new messages and if new family members have joined

         check for new message every 1 sec

         check for new family members every 5 sec


         */







    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

}

