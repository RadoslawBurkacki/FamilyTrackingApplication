package com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.honoursproject.radoslawburkacki.familytrackingapplication.ServerValues;
import com.squareup.okhttp.*;

public class LoginTask extends AsyncTask<Void, Void, String> {

    public interface AsyncResponse {
        void processFinish(String token,int statuscode);
    }


    public LoginTask.AsyncResponse delegate = null;
    int statuscode;
    String token;
    String email;
    String password;

    public LoginTask(AsyncResponse delegate, String email, String password){
        this.email = email;
        this.password = password;
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(Void... param) {

        final MediaType jsonMediaType = MediaType.parse("application/json");
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("email", email);
            jsonObject.addProperty("password", password);

            RequestBody requestBody = RequestBody.create(jsonMediaType, new Gson().toJson(jsonObject));

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(ServerValues.SERVER_ADDRESS+"/login")
                    .post(requestBody)
                    .addHeader("content-type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();

            statuscode = response.code();
            token = response.header("Authorization");
            Log.d("con1n", token);

        } catch (Exception e) {
           token = Integer.toString(statuscode);
           Log.d("con1n", token);
        }
        return token;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String token) {
        delegate.processFinish(token,statuscode);

    }


}