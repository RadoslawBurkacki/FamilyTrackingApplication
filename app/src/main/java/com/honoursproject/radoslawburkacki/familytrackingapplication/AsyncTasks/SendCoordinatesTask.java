package com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.Family;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.User;
import com.honoursproject.radoslawburkacki.familytrackingapplication.ServerValues;
import com.squareup.okhttp.*;

public class SendCoordinatesTask extends AsyncTask<Void, Void, Void> {

    public interface AsyncResponse {
        void processFinish();
    }

    public SendCoordinatesTask.AsyncResponse delegate = null;

    public SendCoordinatesTask(AsyncResponse delegate, LatLng latLng, String token, long userid, long familyid) {
        this.delegate = delegate;
        this.latLng = latLng;
        this.token = token;
        this.userid = userid;
        this.familyid = familyid;
    }

    LatLng latLng;
    String token;
    long userid;
    long familyid;

    @Override
    protected Void doInBackground(Void... param) {

        final MediaType jsonMediaType = MediaType.parse("application/json");
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("latitude", latLng.latitude);
            jsonObject.addProperty("longitude", latLng.longitude);

            RequestBody requestBody = RequestBody.create(jsonMediaType, new Gson().toJson(jsonObject));

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(ServerValues.SERVER_ADDRESS + "/families/location/" + userid)
                    .post(requestBody)
                    .addHeader("content-type", "application/json")
                    .addHeader("Authorization", token)
                    .build();


            client.newCall(request).execute();




        } catch (Exception e) {
            Log.d("test1", e.toString());
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