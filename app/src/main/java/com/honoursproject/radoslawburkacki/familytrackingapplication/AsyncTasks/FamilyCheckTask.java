package com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.honoursproject.radoslawburkacki.familytrackingapplication.ServerValues;
import com.squareup.okhttp.*;

public class FamilyCheckTask extends AsyncTask<Void, Void, Boolean> {

    public interface AsyncResponse {
        void processFinish(Boolean isFamilyMember,int statuscode);
    }


    public FamilyCheckTask.AsyncResponse delegate = null;
    int statuscode;
    Boolean isFamilyMember;
    String token;
    String email;

    public FamilyCheckTask(AsyncResponse delegate, String email, String token){
        this.email = email;
        this.token = token;
        this.delegate = delegate;
    }

    @Override
    protected Boolean doInBackground(Void... param) {

        final MediaType jsonMediaType = MediaType.parse("application/json");
        try {
            JsonObject jsonObject = new JsonObject();
            RequestBody requestBody = RequestBody.create(jsonMediaType, new Gson().toJson(jsonObject));

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(ServerValues.SERVER_ADDRESS+"/familycheck/"+email)
                    .post(requestBody)
                    .addHeader("content-type", "application/json")
                    .addHeader("Authorization", token)
                    .build();

            Response response = client.newCall(request).execute();

            statuscode = response.code();
            if(statuscode==302){//found
                isFamilyMember=true;
            }
            else{
              isFamilyMember=false;
            }



        } catch (Exception e) {

        }
        return isFamilyMember;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Boolean isFamilyMember) {
        delegate.processFinish(isFamilyMember,statuscode);

    }


}