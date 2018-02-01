package com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks;

import android.os.AsyncTask;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.Family;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.User;
import com.honoursproject.radoslawburkacki.familytrackingapplication.ServerValues;
import com.squareup.okhttp.*;

public class CheckForNewFamilyMembersTask extends AsyncTask<Void,Void,Void> {

    public CheckForNewFamilyMembersTask.AsyncResponse delegate = null;


    String token;
    int statuscode;
    int familyid;
    int noOfFamilyMembers;
    Family f;

    public interface AsyncResponse {
        void processFinish(int statuscode, Family f);
    }


    public CheckForNewFamilyMembersTask(AsyncResponse delegate ){
               this.delegate = delegate;
    }


    @Override
    protected Void doInBackground(Void... voids) {


        try {


            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(ServerValues.SERVER_ADDRESS + "/families/"+ familyid +"/"+noOfFamilyMembers)
                    .get()
                    .addHeader("content-type", "application/json")
                    .addHeader("Authentication", token)
                    .build();

            Response response = client.newCall(request).execute();

            statuscode = response.code();

            if(statuscode==203){
                String jsonData = response.body().string();

                response.body().close();

                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                JsonElement mJson = parser.parse(jsonData);
                f = gson.fromJson(mJson, Family.class);
            }
            else {
                f = new Family();
            }

            response.body().close();


        }catch(Exception e){

        }


        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        delegate.processFinish(statuscode, f);
    }
}
