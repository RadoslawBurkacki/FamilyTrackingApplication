package com.honoursproject.radoslawburkacki.familytrackingapplication;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks.RegisterTask;


public class Register2 extends AppCompatActivity implements RegisterTask.AsyncResponse {



    private EditText fname;
    private EditText lname;
    private Button create;
    private User newUser;
    private String serverAddress = "http://10.16.30.10:8080/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        Intent i = getIntent();
        newUser = (User) i.getSerializableExtra("user");

        fname = (EditText) findViewById(R.id.fname);
        lname = (EditText) findViewById(R.id.lname);
        create = (Button) findViewById(R.id.registerCreate);



        create.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                newUser.setFname(fname.getText().toString());
                newUser.setLname(lname.getText().toString());
                register();


                finish();
            }



        });




    }

    @Override
    public void processFinish(Integer statuscode){

        if(statuscode == 201){
            Toast.makeText(this, "Success! New account was created.",
                    Toast.LENGTH_LONG).show();

        }
        else if(statuscode == 409){
            Toast.makeText(this, "Fail! Email is already in use!",
                    Toast.LENGTH_LONG).show();

        }


    }

    public void register(){
        new RegisterTask (this,newUser,serverAddress).execute();
    }




}







