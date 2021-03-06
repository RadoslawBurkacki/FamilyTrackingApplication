package com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks;

/**
 * Radoslaw Burkacki Honours Project - Family Centre Application
 * <p>
 * RegisterTask
 * This class is used to sent request to server, its sending a POST request to the server
 * and which is used to attach new user to family.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.User;
import com.honoursproject.radoslawburkacki.familytrackingapplication.ServerValues;

import okhttp3.*;


public class RegisterTask extends AsyncTask<User, String, Integer> {

    public interface AsyncResponse {
        void processFinish(Integer statuscode);
    }

    public AsyncResponse delegate = null;
    int statuscode;
    User newUser;
    Context context;

    public RegisterTask(AsyncResponse delegate, Context context, User newUser) {
        this.newUser = newUser;
        this.delegate = delegate;
        this.context = context;
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

            OkHttpClient client = ServerValues.getOkHttpClient();

            Request request = new Request.Builder()
                    .url(ServerValues.SERVER_ADDRESS + "/users")
                    .post(requestBody)
                    .addHeader("content-type", "application/json")
                    .build();


            Response response = client.newCall(request).execute();

            statuscode = response.code();

            Log.d("http", statuscode + response.body().toString());

            response.body().close();


        } catch (Exception e) {
            Log.d("http", e.toString());
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