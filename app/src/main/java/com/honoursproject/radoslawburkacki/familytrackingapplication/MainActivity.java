package com.honoursproject.radoslawburkacki.familytrackingapplication;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.google.firebase.iid.FirebaseInstanceId;
import com.honoursproject.radoslawburkacki.familytrackingapplication.fcm.MyFirebaseInstanceIDService;


public class MainActivity extends AppCompatActivity {

    Button btnlogin;
    Button btnregister;

    private ConstraintLayout constraintLayout;
    private AnimationDrawable animationDrawable;

    public static final String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences prefs;
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        if(prefs.contains("email")){
            Intent intent = new Intent(MainActivity.this, Map.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            MainActivity.this.finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        startBackgroundAnimation();


        btnlogin = (Button) findViewById(R.id.createnewfamily);
        btnregister = (Button) findViewById(R.id.joinfamily);

        btnlogin.setOnClickListener(new View.OnClickListener() { // action listener for login button
            @Override
            public void onClick(View view) { // when login button is pressed then...
                startActivity(new Intent(MainActivity.this, Login.class)); // open new activity called Register

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
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
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
