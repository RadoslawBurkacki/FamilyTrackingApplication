package com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks;


import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.*;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.User;
import com.honoursproject.radoslawburkacki.familytrackingapplication.ServerValues;



public class DeleteUserFromFamilyTask extends AsyncTask<Void, Void, Void> {

    public interface AsyncResponse {
        void processFinish(Integer statuscode);
    }

    public AsyncResponse delegate = null;
    int statuscode;
    User newUser;
    String token;
    long familyid;

    public DeleteUserFromFamilyTask(AsyncResponse delegate, User newUser, long familyid,String token) {
        this.newUser = newUser;
        this.delegate = delegate;
        this.token = token;
        this.familyid = familyid;
    }

    @Override
    protected Void doInBackground(Void... param) {

        final MediaType jsonMediaType = MediaType.parse("application/json");
        try {

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("familyId", familyid);
            jsonObject.addProperty("familyPassword", "none");
            jsonObject.addProperty("userId", newUser.getId());

            OkHttpClient client = ServerValues.getOkHttpClient();

            RequestBody requestBody = RequestBody.create(jsonMediaType, new Gson().toJson(jsonObject));

            Request request = new Request.Builder()
                    .url(ServerValues.SERVER_ADDRESS + "/families/")
                    .delete(requestBody)
                    .addHeader("Authorization", token)
                    .addHeader("content-type", "application/json")
                    .build();


            Response response = client.newCall(request).execute();

            statuscode = response.code();

            Log.d("http", statuscode + response.body().toString());

            response.body().close();



        } catch (Exception e) {
            Log.d("http", e.toString());
        }
        return null;
    }




    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void values) {
        delegate.processFinish(statuscode);

    }


}