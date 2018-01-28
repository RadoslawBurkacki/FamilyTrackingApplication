package com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.Message;
import com.honoursproject.radoslawburkacki.familytrackingapplication.ServerValues;
import com.squareup.okhttp.*;


public class SendChatMessageTask extends AsyncTask<Void, Void, Void> {

    public interface AsyncResponse {
        void processFinish(int statuscode);
    }

    public SendChatMessageTask.AsyncResponse delegate = null;


    public SendChatMessageTask(AsyncResponse delegate, Message m, String token) {

        this.delegate = delegate;
        this.m = m;
        this.token = token;
    }

    int statuscode;
    String token;

    private Message m;

    @Override
    protected Void doInBackground(Void... param) {

        final MediaType jsonMediaType = MediaType.parse("application/json");
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("messageId", m.getMessageId());
            jsonObject.addProperty("fromId", m.getFromId());
            jsonObject.addProperty("toId", m.getToId());
            jsonObject.addProperty("message", m.getMessage());
            jsonObject.addProperty("date", m.getDate());

            RequestBody requestBody = RequestBody.create(jsonMediaType, new Gson().toJson(jsonObject));

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(ServerValues.SERVER_ADDRESS + "/chat/")
                    .post(requestBody)
                    .addHeader("content-type", "application/json")
                    .addHeader("Authorization", token)
                    .build();

            Response response = client.newCall(request).execute();

            statuscode = response.code();

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