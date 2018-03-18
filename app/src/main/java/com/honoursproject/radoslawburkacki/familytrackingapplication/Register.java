package com.honoursproject.radoslawburkacki.familytrackingapplication;

/**
 * Radoslaw Burkacki Honours Project - Family Centre Application
 *
 * Register
 * This class is allows user to register. It controls all the logic which is behind the UI which allows user to register.
 * It gets data from user(email, password). When the next button is pressed it checks if the data is correct if its not
 * then appropriate error message is displayed, if it is correct then it displays register2 activity
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
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
        btnnext = (Button) findViewById(R.id.btnnext);

        email.setText("1@1.pl");
        pass.setText("111111");
        pass2.setText("111111");

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
                        intent.putExtra("user", newUser);
                        startActivity(intent);
                        finish();

                    } else {
                        pass.setText("");
                        pass2.setText("");
                        email.setText("");
                        showMessage(getResources().getString(R.string.erremail));
                    }
                } else if(pass.getText().toString().equals("") || pass2.getText().toString().equals("") || email.getText().toString().equals("")  ){
                    showMessage(getResources().getString(R.string.errempty));

                } else if (pass.getText().toString().length() < 6) {
                    pass.setText("");
                    pass2.setText("");
                    showMessage(getResources().getString(R.string.errpasswordtooshort));

                }else {
                    pass.setText("");
                    pass2.setText("");
                    showMessage(getResources().getString(R.string.errpasswords));
                }
            }
        });
    }

    private void showMessage(String msg) {
        Toast.makeText(this, msg,
                Toast.LENGTH_LONG).show();

    }
}
