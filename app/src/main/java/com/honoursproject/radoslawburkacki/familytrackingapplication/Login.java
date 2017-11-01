package com.honoursproject.radoslawburkacki.familytrackingapplication;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks.LoginTask;

public class Login extends AppCompatActivity implements LoginTask.AsyncResponse {

    Button btnlogin;
    EditText email;
    EditText password;
    String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnlogin = (Button) findViewById(R.id.btnlogin);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        btnlogin.setOnClickListener(new View.OnClickListener() { // action listener for login button
            @Override
            public void onClick(View view) { // when login button is pressed then...

                if (password.length() >= 6 && android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {  // if passwords lenght is at least 6 characters and email is correct then...

                    login();

                }

            }
        });
    }

    @Override
    public void processFinish(String token, int statuscode) {

        if (statuscode==401) { // error 401 means that user has entered wrong credentials
            Toast.makeText(this, "You have entered wrong email or password. Please try again.",
                    Toast.LENGTH_LONG).show();
            email.setText("");
            password.setText("");
        }
        else if(statuscode==0){ // server is offline
            Toast.makeText(this, "Sorry server is currently offline.",
                    Toast.LENGTH_LONG).show();
        }
        else if(statuscode==200){
            this.token = token;
        }
        else{
            Toast.makeText(this, "Error " +statuscode,
                    Toast.LENGTH_LONG).show();
        }
    }


    public void login() {
        new LoginTask(this, email.getText().toString(), password.getText().toString()).execute();
    }
}
