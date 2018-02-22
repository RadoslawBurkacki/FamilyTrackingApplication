package com.honoursproject.radoslawburkacki.familytrackingapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks.CreateFamilyTask;
import com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks.GetFamilyTask;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.Family;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.User;


public class Create_family extends AppCompatActivity implements CreateFamilyTask.AsyncResponse, GetFamilyTask.AsyncResponse {
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    EditText familyname;
    Button createfamily;
    EditText pass;
    EditText pass2;

    User user;
    String token;
    Family newFamily;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_family);

        Intent i = getIntent();
        user = (User) i.getSerializableExtra("user");
        token = (String) i.getSerializableExtra("token");

        familyname = (EditText) findViewById(R.id.txtFamilyName);
        createfamily = (Button) findViewById(R.id.createFamily);
        pass = (EditText) findViewById(R.id.txtPassword);
        pass2 = (EditText) findViewById(R.id.txtRePassword);

        familyname.setText(user.getLname());

        createfamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (pass.getText().toString().length() >= 6 && pass.getText().toString().equals(pass2.getText().toString())) { // if password is
                    // at least 6 chars and both passwords are the same then...

                    newFamily = new Family(user.getId(), familyname.getText().toString(), pass.getText().toString(), null);

                    createNewFamily();


                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString("token", token);
                    editor.putLong("userid", user.getId());
                    editor.putString("fname", user.getFname());
                    editor.putString("lname", user.getLname());
                    editor.putString("email", user.getEmail());
                    editor.apply();


                    Intent intent = new Intent(Create_family.this, Map.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    Create_family.this.finish();



                }

            }
        });

    }

    void GetFamily() {
        new GetFamilyTask(this, user, token).execute();
    }

    @Override
    public void processFinish(Family f) {

        final Toast toast = Toast.makeText(getBaseContext(), "Family created successfully\nFamily unique identifier is: " + f.getId() + ".\nDetails about family can be found in MyFamily tab",Toast.LENGTH_SHORT);
        toast.show();
        new CountDownTimer(10000, 1000)
        {
            public void onTick(long millisUntilFinished) {toast.show();}
            public void onFinish() {toast.cancel();}
        }.start();
    }

    @Override
    public void processFinish(int stautscode) {

        if (stautscode == 201) {// family created
            GetFamily();

        } else {

        }

    }


    void createNewFamily() {
        new CreateFamilyTask(this, newFamily, token).execute();
    }

}
