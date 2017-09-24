package com.honoursproject.radoslawburkacki.familytrackingapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Register extends AppCompatActivity {

    EditText email;
    EditText pass;
    EditText pass2;
    Button btnnext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = (EditText) findViewById(R.id.txtEmail);
        pass = (EditText) findViewById(R.id.txtPassword);
        pass2 = (EditText) findViewById(R.id.txtRePassword);
        btnnext = (Button) findViewById(R.id.registerNext);





        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(pass.getText().toString().length() >= 6){ // if password is at least 6 chars then...

                    if(pass.getText().toString().equals(pass2.getText().toString())){// if both password are the same then...

                        Log.d("myTag", "This is my message");

                    }


                }


            }
        });



    }
}
