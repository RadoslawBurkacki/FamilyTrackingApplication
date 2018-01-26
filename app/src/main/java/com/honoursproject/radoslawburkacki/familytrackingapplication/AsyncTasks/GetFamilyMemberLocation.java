package com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.Family;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.User;
import com.honoursproject.radoslawburkacki.familytrackingapplication.ServerValues;
import com.squareup.okhttp.*;
import org.json.JSONObject;

public class GetFamilyMemberLocation extends AsyncTask<Void, Void, Void> {

    public interface AsyncResponse {
        void processFinish(int statuscode, LatLng coordinates, long familyMemberId);
    }

    public GetFamilyMemberLocation.AsyncResponse delegate = null;

    int statuscode;
    String token;
    long familyMemberId;
    LatLng coordinates;

    public GetFamilyMemberLocation(AsyncResponse delegate, String token, long familyMemberId) {
        this.familyMemberId = familyMemberId;
        this.token = token;
        this.delegate = delegate;
    }

    @Override
    protected Void doInBackground(Void... param) {


        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(ServerValues.SERVER_ADDRESS + "/families/location/" + familyMemberId)
                    .get()
                    .addHeader("content-type", "application/json")
                    .addHeader("Authorization", token)
                    .build();

            Response response = client.newCall(request).execute();

            statuscode = response.code();

            String jsonData = response.body().string();

            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonElement mJson = parser.parse(jsonData);
            coordinates = gson.fromJson(mJson, LatLng.class);

            Log.d("hello", ""+ coordinates.latitude + coordinates.longitude);


        } catch (Exception e) {

        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void v) {
        delegate.processFinish(statuscode, coordinates, familyMemberId);
    }


}