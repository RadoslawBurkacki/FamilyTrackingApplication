package com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks;

/**
 * Radoslaw Burkacki Honours Project - Family Centre Application
 * <p>
 * JoinFamilyTask
 * This class is used to sent request to server, its sending a POST request to the server
 * and which is used to attach new user to family.
 */

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.User;
import com.honoursproject.radoslawburkacki.familytrackingapplication.*;
import com.squareup.okhttp.*;

public class JoinFamilyTask extends AsyncTask<Void, Void, Void> {

    public interface AsyncResponse {
        void processFinish(int statuscode);
    }

    public JoinFamilyTask.AsyncResponse delegate = null;


    User user;
    String token;
    String familyid;
    String familypassword;
    int statuscode;

    public JoinFamilyTask(AsyncResponse delegate, User user, String token, String familyid, String familypassword) {
        this.user = user;
        this.token = token;
        this.familyid = familyid;
        this.familypassword = familypassword;
        this.delegate = delegate;
    }

    @Override
    protected Void doInBackground(Void... param) {

        final MediaType jsonMediaType = MediaType.parse("application/json");
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("familyId", familyid);
            jsonObject.addProperty("familyPassword", familypassword);
            jsonObject.addProperty("userId", user.getId());

            RequestBody requestBody = RequestBody.create(jsonMediaType, new Gson().toJson(jsonObject));

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(ServerValues.SERVER_ADDRESS + "/families/")
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