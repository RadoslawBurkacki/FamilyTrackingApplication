package com.honoursproject.radoslawburkacki.familytrackingapplication;

/**
 * Radoslaw Burkacki Honours Project - Family Centre Application
 *
 * Login
 * This class is used to allow user to login, it controls all the logic which is behind the UI
 * which is used to get input from user. This class is mainly responsible for getting data from user
 * (email and password) and sending it to server, based on response in can do various actions:
 * 1. Display error message (no connection)
 * 2. Display error message (wrong email/password)
 * 3. Display error message (server off)
 * 4. Login successful, if login is successful then another request to server is made
 * it getting information if this user belongs to family if he does then map screen is displayed
 * if not then family setup screen is displayed
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks.LoginTask;
import com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks.SendFCMTokenTask;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.User;

public class Login extends AppCompatActivity implements LoginTask.AsyncResponse {
    protected static final String MY_PREFS_NAME = "FamilyCentreApplicationPrefFile";

    private Button btnlogin;
    private EditText email;
    private EditText password;

    private ConstraintLayout constraintLayout;
    private AnimationDrawable animationDrawable;

    protected String token;
    protected User user;
    protected int statuscode;
    protected boolean isUserFamilyMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        startBackgroundAnimation();

        btnlogin = (Button) findViewById(R.id.btnlogin);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        email.setText("1@1.pl");
        password.setText("111111");

        btnlogin.setOnClickListener(new View.OnClickListener() { // action listener for login button
            @Override
            public void onClick(View view) { // when login button is pressed then...

                if (password.length() >= 6 && android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {  // if passwords lenght is at least 6 characters and email is correct then...
                    login(); // call function to login, login function includes: 1. authenticating user, 2. getting user instance, 3. checking if user is a member of family
                }else{
                    displayErrMsg();
                }
            }
        });
    }

    private void displayErrMsg(){
        Toast.makeText(this, "Make sure that all boxes are filled in correctly.",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void processFinish(String token, int statuscode, User user, boolean isUserFamilyMember) {

        if (statuscode == 401) { // error 401 means that user has entered wrong credentials
            Toast.makeText(this, getResources().getText(R.string.errwrongcred),
                    Toast.LENGTH_LONG).show();
            email.setText("");
            password.setText("");
        } else if (statuscode == 0) { // server is offline
            Toast.makeText(this, getResources().getText(R.string.errserveroff),
                    Toast.LENGTH_LONG).show();
        } else if (statuscode == 200) {   //authorization successful new token generated

            SendFcmToken(user.getId(), token, FirebaseInstanceId.getInstance().getToken());

            this.token = token;
            this.statuscode = statuscode;
            this.user = user;
            this.isUserFamilyMember = isUserFamilyMember;
            Toast.makeText(this,  getResources().getText(R.string.loginsu),
                    Toast.LENGTH_LONG).show();

            if (isUserFamilyMember) { // User is already member of a family so open map screen


                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("token", token);
                editor.putLong("userid", user.getId());
                editor.putString("fname", user.getFname());
                editor.putString("lname", user.getLname());
                editor.putString("email", user.getEmail());
                editor.apply();

                Intent intent = new Intent(Login.this, Map.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Login.this.finish();


            } else if (!isUserFamilyMember) { // User is not a member of any family go to family creation/join

                Intent intent = new Intent(Login.this, Family_setup.class);
                intent.putExtra("user", user);
                intent.putExtra("token", token);
                startActivity(intent);
            }

        } else {
            Toast.makeText(this, getResources().getText(R.string.error) +" "+ statuscode,
                    Toast.LENGTH_LONG).show();
        }
    }

    public void SendFcmToken(Long userid, String token, String FCMtoken) {
        new SendFCMTokenTask(userid, token, FCMtoken).execute();
    }   // Start SendFcmToken Async Task

    public void login() {
        new LoginTask(this, email.getText().toString(), password.getText().toString()).execute();
    }

    private void startBackgroundAnimation(){
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