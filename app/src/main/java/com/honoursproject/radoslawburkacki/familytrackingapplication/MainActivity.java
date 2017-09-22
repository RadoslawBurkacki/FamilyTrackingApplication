package com.honoursproject.radoslawburkacki.familytrackingapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnlogin;
    Button btnregister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnlogin = (Button) findViewById(R.id.button);
        btnregister = (Button) findViewById(R.id.button2);

        btnlogin.setOnClickListener(new View.OnClickListener() { // action listener for login button
            @Override
            public void onClick(View view) { // when login button is pressed then...


            }
        });

        btnregister.setOnClickListener(new View.OnClickListener() { // action listener for register button
            @Override
            public void onClick(View view) { // when register button is pressed then...
                startActivity(new Intent(MainActivity.this, Register.class)); // open new activity called Register

            }
        });


    }


}
