package com.honoursproject.radoslawburkacki.familytrackingapplication;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks.LoginTask;

public class Login extends AppCompatActivity {

    Button btnlogin;
    EditText email;
    EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnlogin = (Button) findViewById(R.id.btnlogin);
        email =(EditText) findViewById(R.id.email);
        password =(EditText) findViewById(R.id.password);

        btnlogin.setOnClickListener(new View.OnClickListener() { // action listener for login button
            @Override
            public void onClick(View view) { // when login button is pressed then...

                if(password.length() >= 6 && android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){  // if passwords lenght is at least 6 characters and email is correct then...

                    login();

                }

            }
        });
    }

    public void login() {
        //new LoginTask(this, email, password).execute();
    }
}
