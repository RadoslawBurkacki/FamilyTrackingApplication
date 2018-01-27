package com.honoursproject.radoslawburkacki.familytrackingapplication;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks.RegisterTask;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.User;
import com.honoursproject.radoslawburkacki.familytrackingapplication.R;



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
        create = (Button) findViewById(R.id.registerCreate);

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
        Toast.makeText(this,"Please enter first name and last name.",Toast.LENGTH_LONG).show();
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
            Toast.makeText(this, "Fail! Check your internet connection.",
                    Toast.LENGTH_LONG).show();
        }
        else if (statuscode == 201) {
            Toast.makeText(this, "Success! New account was created.",
                    Toast.LENGTH_LONG).show();

        } else if (statuscode == 409) {
            Toast.makeText(this, "Fail! Email is already in use!",
                    Toast.LENGTH_LONG).show();

        }
        else{
            Toast.makeText(this, "Sorry. Our server is currently offline." + statuscode,
                    Toast.LENGTH_LONG).show();
        }


    }

    public void register() {
        new RegisterTask(this, newUser).execute();
    }


}







