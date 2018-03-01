package com.honoursproject.radoslawburkacki.familytrackingapplication;

/**
 * Radoslaw Burkacki Honours Project - Family Centre Application
 *
 * MainActivity
 * This class is the starting point of the application. It is used to let user to pick either to login or register,
 * it controls all the logic which is behind the UI which allows user to register or login. When the app is started
 * it checks if user has login previously if so then autologin function is triggered, then it checks if used
 * has changed the language if so then appropriate language is loaded.
 */


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    public static final String MY_PREFS_NAME = "FamilyCentreApplicationPrefFile";

    Button btnlogin;
    Button btnregister;

    private ConstraintLayout constraintLayout;
    private AnimationDrawable animationDrawable;

    SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        setLanguage();
        tryAutologin();

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

    protected void tryAutologin(){
        if (prefs.contains("email")) {
            Intent intent = new Intent(MainActivity.this, Map.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            overridePendingTransition(0, 0);
            startActivity(intent);
            MainActivity.this.finish();
        }
    }

    protected void setLanguage() {
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        String language = prefs.getString("language", "");

        if(language.equals("")){

        }else {
            String languageToLoad = language;
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
            this.setContentView(R.layout.activity_main);
        }
    }

    protected void startBackgroundAnimation() {
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
