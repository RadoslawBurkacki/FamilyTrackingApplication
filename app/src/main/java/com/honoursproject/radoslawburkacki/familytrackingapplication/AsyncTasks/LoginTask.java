package com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.User;
import com.honoursproject.radoslawburkacki.familytrackingapplication.ServerValues;
import com.squareup.okhttp.*;



public class LoginTask extends AsyncTask<Void, Void, String> {

    public interface AsyncResponse {
        void processFinish(String token, int statuscode, User user, boolean isUserFamilyMember);
    }


    public LoginTask.AsyncResponse delegate = null;
    int statuscode;
    User user;
    String token;
    String email;
    String password;
    boolean isUserFamilyMember;

    public LoginTask(AsyncResponse delegate, String email, String password) {
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
                    .url(ServerValues.SERVER_ADDRESS + "/login")
                    .post(requestBody)
                    .addHeader("content-type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();

            statuscode = response.code();
            token = response.header("Authorization");


            if (statuscode == 200) { // send new request if authentication was successful this request is used to get user instance using email
                Request request2 = new Request.Builder()
                        .url(ServerValues.SERVER_ADDRESS + "/user/" + email)
                        .get()
                        .addHeader("content-type", "application/json")
                        .addHeader("Authorization", token)
                        .build();

                Response response2 = client.newCall(request2).execute();

                String jsonData = response2.body().string();

                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                JsonElement mJson = parser.parse(jsonData);
                user = gson.fromJson(mJson, User.class);


                // check if user is a member of any family, the response which will be received will send a status code, 302 if found and 404 if not
                Request request3 = new Request.Builder()
                        .url(ServerValues.SERVER_ADDRESS + "/family/check/" + user.getId())
                        .head()
                        .addHeader("content-type", "application/json")
                        .addHeader("Authorization", token)
                        .build();

                Response response3 = client.newCall(request3).execute();

                if(response3.code() == 302){
                    isUserFamilyMember = true;
                }
                else if(response3.code() == 404){
                    isUserFamilyMember = false;
                }
            }

        } catch (Exception e) {
            Log.d("test1", e.toString());
        }

        return token;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String token) {
        delegate.processFinish(token, statuscode, user, isUserFamilyMember);
    }


}