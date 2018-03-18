package com.honoursproject.radoslawburkacki.familytrackingapplication;

/**
 * Radoslaw Burkacki Honours Project - Family Centre Application
 *
 * Register2
 * This class is allows user to register. It controls all the logic which is behind the UI which allows user to register.
 * It gets data from user(firstname, lastname). When the create button is pressed POST request is sent to the server,
 * this request is used to register user, this class awaits for the response from server, based on that it displays
 * appropriate message (success/fail) then it goes back to Main Activity
 */

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks.RegisterTask;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.User;
public class Register2 extends AppCompatActivity implements RegisterTask.AsyncResponse {


    private EditText fname;
    private EditText lname;
    private Button create;
    private User newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        Intent i = getIntent();
        newUser = (User) i.getSerializableExtra("user");

        fname = (EditText) findViewById(R.id.familyid);
        lname = (EditText) findViewById(R.id.familypassword);
        create = (Button) findViewById(R.id.btncreate);

         fname.setText("first name");
         lname.setText("last name");



        create.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(fname.getText().toString().equals("") || lname.getText().toString().equals("")){
                    pleaseEnterCredentials();
                }
                else {
                    newUser.setFname(fname.getText().toString());
                    newUser.setLname(lname.getText().toString());

                    register();

                    finish();
                }
            }

        });

    }

    private void pleaseEnterCredentials(){
        Toast.makeText(this, getResources().getText(R.string.msgplseneterfnamelnmae),Toast.LENGTH_LONG).show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void processFinish(Integer statuscode) {

        if(!isNetworkAvailable()){
            Toast.makeText(this, getResources().getString(R.string.errconnection),
                    Toast.LENGTH_LONG).show();
        }
        else if (statuscode == 201) {
            Toast.makeText(this, getResources().getString(R.string.newacccreated),
                    Toast.LENGTH_LONG).show();

        } else if (statuscode == 409) {
            Toast.makeText(this, getResources().getString(R.string.erremailinuse),
                    Toast.LENGTH_LONG).show();

        }
        else{
            Toast.makeText(this, getResources().getString(R.string.errserveroff) + statuscode,
                    Toast.LENGTH_LONG).show();
        }


    }

    public void register() {
        new RegisterTask(this,getApplicationContext(), newUser).execute();
    }


}







