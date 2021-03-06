package com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks;

/**
 * Radoslaw Burkacki Honours Project - Family Centre Application
 *
 * GetFamilyMemberLocationTask
 * This class is used to sent request to server, its sending a GET request to the server and its passing
 * user id in the uri then its getting location data of a user
 */


import android.os.AsyncTask;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.honoursproject.radoslawburkacki.familytrackingapplication.ServerValues;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class GetFamilyMemberLocationTask extends AsyncTask<Void, Void, Void> {

    public interface AsyncResponse {
        void processFinish(int statuscode, LatLng coordinates, long familyMemberId, List<Integer> list);
    }

    public GetFamilyMemberLocationTask.AsyncResponse delegate = null;

    List<Integer> list = new ArrayList<>();
    int statuscode;
    String token;
    long familyMemberId;
    LatLng coordinates;

    public GetFamilyMemberLocationTask(AsyncResponse delegate, String token, long familyMemberId) {
        this.familyMemberId = familyMemberId;
        this.token = token;
        this.delegate = delegate;
    }

    @Override
    protected Void doInBackground(Void... param) {


        try {
            OkHttpClient client = ServerValues.getOkHttpClient();

            Request request = new Request.Builder()
                    .url(ServerValues.SERVER_ADDRESS + "/families/location/" + familyMemberId)
                    .get()
                    .addHeader("content-type", "application/json")
                    .addHeader("Authorization", token)
                    .build();

            Response response = client.newCall(request).execute();

            statuscode = response.code();

            String jsonData = response.body().string();

            response.body().close();

            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonElement mJson = parser.parse(jsonData);
            coordinates = gson.fromJson(mJson, LatLng.class);


            JSONObject obj = new JSONObject(jsonData);
            JSONArray params = obj.getJSONArray("list");

            if (params != null) {
                int len = params.length();
                for (int i=0;i<len;i++){
                    list.add(params.getInt(i));
                }
            }

            Log.d("hello", "" + coordinates.latitude + coordinates.longitude);

            for(Integer i : list){
                Log.d("hello", ""+i);
            }


        } catch (Exception e) {
            Log.d("getFamilyMemberLocation", e.toString());
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void v) {
        delegate.processFinish(statuscode, coordinates, familyMemberId, list);
    }


}