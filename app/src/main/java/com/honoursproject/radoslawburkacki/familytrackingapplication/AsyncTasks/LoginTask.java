package com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.User;
import com.honoursproject.radoslawburkacki.familytrackingapplication.ServerValues;
import com.squareup.okhttp.*;

public class LoginTask extends AsyncTask<User, String, Integer> {


    ServerValues s;
    public interface AsyncResponse {
        void processFinish(Integer statuscode);
    }

    public RegisterTask.AsyncResponse delegate = null;


    int statuscode;
    String email;
    String password;
    String serveraddress;


    public LoginTask(RegisterTask.AsyncResponse delegate, String email, String password){
        this.email = email;
        this.password = password;
        this.serveraddress = serveraddress;
        this.delegate = delegate;
    }



    @Override
    protected Integer doInBackground(User... param) {

        final MediaType jsonMediaType = MediaType.parse("application/json");
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("email", email);
            jsonObject.addProperty("password", password);

            RequestBody requestBody = RequestBody.create(jsonMediaType, new Gson().toJson(jsonObject));

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(s.getServerAddress()+"register/")
                    .post(requestBody)
                    .addHeader("content-type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();

            statuscode = response.code();
            Log.d("con1n", response.message());

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