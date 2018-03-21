package com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks;

/**
 * Created by radek on 13/02/2018.
 */

import android.os.AsyncTask;
import android.util.Log;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.User;
import com.honoursproject.radoslawburkacki.familytrackingapplication.ServerValues;
import okhttp3.*;


public class SendSOSTask extends AsyncTask<Void, Void, Void> {

    public interface AsyncResponse {
        void processFinish(Integer statuscode);
    }

    public AsyncResponse delegate = null;
    String token;
    int statuscode;
    User user;

    public SendSOSTask(AsyncResponse delegate, User user, String token) {
        this.user = user;
        this.token = token;
        this.delegate = delegate;
    }

    @Override
    protected Void doInBackground(Void... param) {

        try {
            OkHttpClient client = ServerValues.getOkHttpClient();

            Request request = new Request.Builder()
                    .url(ServerValues.SERVER_ADDRESS + "/sos/" + user.getId())
                    .head()
                    .addHeader("content-type", "application/json")
                    .addHeader("Authorization", token)
                    .build();

            Response response = client.newCall(request).execute();

            statuscode = response.code();

            response.body().close();

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
        delegate.processFinish(statuscode);
    }


}