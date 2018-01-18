package com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;
import com.google.android.gms.maps.GoogleMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.Family;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.User;
import com.honoursproject.radoslawburkacki.familytrackingapplication.ServerValues;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class SendCoordinatesTask extends AsyncTask<Void, Void, Void> {

    public interface AsyncResponse {
        void processFinish();
    }

    public SendCoordinatesTask.AsyncResponse delegate = null;
    private GoogleMap googlemap;


    public SendCoordinatesTask(AsyncResponse delegate, GoogleMap googlemap) {
        this.googlemap = googlemap;
    }

    @Override
    protected Void doInBackground(Void... param) {

        try {



        } catch (Exception e) {
            Log.d("", e.toString());
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void v) {
        delegate.processFinish();
    }


}