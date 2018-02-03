package com.honoursproject.radoslawburkacki.familytrackingapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

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

