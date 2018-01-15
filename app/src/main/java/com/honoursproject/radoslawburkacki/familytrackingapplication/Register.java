package com.honoursproject.radoslawburkacki.familytrackingapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.User;


public class Register extends AppCompatActivity {

    EditText email;
    EditText pass;
    EditText pass2;
    Button btnnext;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = (EditText) findViewById(R.id.txtFamilyName);
        pass = (EditText) findViewById(R.id.txtPassword);
        pass2 = (EditText) findViewById(R.id.txtRePassword);
        btnnext = (Button) findViewById(R.id.createFamily);



        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (pass.getText().toString().length() >= 6 && pass.getText().toString().equals(pass2.getText().toString())) { // if password is
                    // at least 6 chars and both passwords are the same then...


                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) { // if email is valid then...


                        // need to add another if which will check if this email is already in db

                        // at this point everything is correct so we can proceed to next activity


                        User newUser = new User();
                        newUser.setEmail(email.getText().toString());
                        newUser.setPassword(pass.getText().toString());



                        Intent intent = new Intent(Register.this, Register2.class);
                        intent.putExtra("user",newUser);
                        startActivity(intent);
                        finish();


                    } else {
                        pass.setText("");
                        pass2.setText("");
                        email.setText("");
                    }


                } else {
                    pass.setText("");
                    pass2.setText("");
                }


            }
        });


    }
}
