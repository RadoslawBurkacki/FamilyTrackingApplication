package com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks;

/**
 * Radoslaw Burkacki Honours Project - Family Centre Application
 *
 * CreateFamilyTask
 * This class is used to sent request to server, its sending a POST request to server which is used
 * to create ne family.
 */

import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.Family;
import com.honoursproject.radoslawburkacki.familytrackingapplication.ServerValues;
import okhttp3.*;


public class CreateFamilyTask extends AsyncTask<Void, Void, Void> {

    public interface AsyncResponse {
        void processFinish(int statuscode);
    }

    public CreateFamilyTask.AsyncResponse delegate = null;
    int statuscode;
    Family family;
    String token;

    public CreateFamilyTask(AsyncResponse delegate, Family family, String token) {
        this.family = family;
        this.token = token;
        this.delegate = delegate;
    }

    @Override
    protected Void doInBackground(Void... param) {

        final MediaType jsonMediaType = MediaType.parse("application/json");
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", family.getId());
            jsonObject.addProperty("creatorId", family.getCreatorId());
            jsonObject.addProperty("familyName", family.getFamilyName());
            jsonObject.addProperty("joiningPassword", family.getJoiningPassword());

            RequestBody requestBody = RequestBody.create(jsonMediaType, new Gson().toJson(jsonObject));

            OkHttpClient client = ServerValues.getOkHttpClient();

            Request request = new Request.Builder()
                    .url(ServerValues.SERVER_ADDRESS + "/families")
                    .post(requestBody)
                    .addHeader("content-type", "application/json")
                    .addHeader("Authorization", token)
                    .build();


            Response response = client.newCall(request).execute();

            statuscode = response.code();
            token = response.header("Authorization");

            response.body().close();


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
        delegate.processFinish(statuscode);
    }


}