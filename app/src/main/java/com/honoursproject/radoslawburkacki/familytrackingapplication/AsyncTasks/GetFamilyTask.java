package com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.Family;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.User;
import com.honoursproject.radoslawburkacki.familytrackingapplication.ServerValues;
import com.squareup.okhttp.*;


public class GetFamilyTask extends AsyncTask<Void, Void, Void> {

    public interface AsyncResponse {
        void processFinish(Family family);
    }

    public GetFamilyTask.AsyncResponse delegate = null;

    User user;
    Family f;
    String token;

    public GetFamilyTask(AsyncResponse delegate, User user, String token) {
        this.user = user;
        this.token = token;
        this.delegate = delegate;
    }

    @Override
    protected Void doInBackground(Void... param) {

        try {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(ServerValues.SERVER_ADDRESS + "/families/by-user-id/"+user.getId())
                    .get()
                    .addHeader("content-type", "application/json")
                    .addHeader("Authorization", token)
                    .build();

            Response response = client.newCall(request).execute();

            String jsonData = response.body().string();

            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonElement mJson = parser.parse(jsonData);
            f = gson.fromJson(mJson, Family.class);


        } catch (Exception e) {
            Log.d("", e.toString());
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void v) {
        delegate.processFinish(f);
    }


}