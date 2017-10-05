package com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.honoursproject.radoslawburkacki.familytrackingapplication.User;
import com.squareup.okhttp.*;

public class RegisterTask extends AsyncTask<User, String, Integer> {

    public interface AsyncResponse {
        void processFinish(Integer statuscode);
    }

    public AsyncResponse delegate = null;


    int statuscode;
    User newUser;
    String serveraddress;


    public RegisterTask(AsyncResponse delegate,User newUser, String serveraddress){
        this.newUser = newUser;
        this.serveraddress = serveraddress;
        this.delegate = delegate;
    }



    @Override
    protected Integer doInBackground(User... param) {

        final MediaType jsonMediaType = MediaType.parse("application/json");
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", newUser.getId());
            jsonObject.addProperty("email", newUser.getEmail());
            jsonObject.addProperty("password", newUser.getPassword());
            jsonObject.addProperty("fname", newUser.getFname());
            jsonObject.addProperty("lname", newUser.getLname());

            RequestBody requestBody = RequestBody.create(jsonMediaType, new Gson().toJson(jsonObject));

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(serveraddress+"register/")
                    .post(requestBody)
                    .addHeader("content-type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();

            statuscode = response.code();


        } catch (Exception e) {
            Log.d("con1n", e.toString());
        }
        return statuscode;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Integer statuscode) {
        delegate.processFinish(statuscode);

    }


}