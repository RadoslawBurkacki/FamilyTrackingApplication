package com.honoursproject.radoslawburkacki.familytrackingapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks.CreateFamilyTask;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.Family;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.User;
import com.honoursproject.radoslawburkacki.familytrackingapplication.R;


public class Create_family extends AppCompatActivity implements CreateFamilyTask.AsyncResponse {

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

                    newFamily = new Family(user.getId(), familyname.getText().toString(), pass.getText().toString(),null);

                    createNewFamily();


                    Intent intent = new Intent(Create_family.this, Map.class);
                    intent.putExtra("user",user);
                    intent.putExtra("token", token);
                    startActivity(intent);


                }

            }
        });


    }


    @Override
    public void processFinish(int statuscode, Family family) {
        Log.d("1", "@@@@@@@@@@@@@@@@@"+statuscode);

        if(statuscode == 400){ // if user is already a member of family then family wasnt

        }
        else if(statuscode == 201){ // family created

        }

    }


    void createNewFamily() {
        new CreateFamilyTask(this, newFamily, token).execute();
    }

}
