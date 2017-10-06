package com.honoursproject.radoslawburkacki.familytrackingapplication;


import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    Button btnlogin;
    Button btnregister;


    private ConstraintLayout constraintLayout;
    private AnimationDrawable animationDrawable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startBackgroundAnimation();

        btnlogin = (Button) findViewById(R.id.button2);
        btnregister = (Button) findViewById(R.id.button);

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

    void startBackgroundAnimation(){
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);
        animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(4000);
        animationDrawable.setExitFadeDuration(4000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (animationDrawable != null && !animationDrawable.isRunning()) {
            // start the animation
            animationDrawable.start();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (animationDrawable != null && animationDrawable.isRunning()) {
            // stop the animation
            animationDrawable.stop();
        }
    }




}
