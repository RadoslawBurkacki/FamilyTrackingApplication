package com.honoursproject.radoslawburkacki.familytrackingapplication;


import android.content.Intent;
import android.content.SharedPreferences;
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

    public static final String MY_PREFS_NAME = "MyPrefsFile";

    Button btnlogin;
    EditText email;
    EditText password;

    String token;
    User user;
    int statuscode;
    boolean isUserFamilyMember;

    // to login the following steps must be made
    // 1. validate user credentials
    // 2. ger user instance
    // 3. check if a user is a part of family - if he is then go to main screen if not then go to family creation


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                }
            }
        });
    }

    @Override
    public void processFinish(String token, int statuscode, User user, boolean isUserFamilyMember) {

        if (statuscode == 401) { // error 401 means that user has entered wrong credentials
            Toast.makeText(this, "You have entered wrong email or password. Please try agaain.",
                    Toast.LENGTH_LONG).show();
            email.setText("");
            password.setText("");
        } else if (statuscode == 0) { // server is offline
            Toast.makeText(this, "Sorry server is currently offline.",
                    Toast.LENGTH_LONG).show();
        } else if (statuscode == 200) {   //authorization successful new token generated

            SendFcmToken(user.getId(), token, FirebaseInstanceId.getInstance().getToken());

            this.token = token;
            this.statuscode = statuscode;
            this.user = user;
            this.isUserFamilyMember = isUserFamilyMember;
            Toast.makeText(this, "Login successful",
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
            Toast.makeText(this, "Error " + statuscode,
                    Toast.LENGTH_LONG).show();
        }
    }


    public void SendFcmToken(Long userid, String token, String FCMtoken) {
        new SendFCMTokenTask(userid, token, FCMtoken).execute();
    }   // Start SendFcmToken Async Task



    public void login() {
        new LoginTask(this, email.getText().toString(), password.getText().toString()).execute();
    }
}