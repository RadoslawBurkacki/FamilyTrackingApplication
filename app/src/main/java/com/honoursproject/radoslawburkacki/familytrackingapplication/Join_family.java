package com.honoursproject.radoslawburkacki.familytrackingapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks.JoinFamilyTask;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.User;



public class Join_family extends AppCompatActivity implements JoinFamilyTask.AsyncResponse {

    EditText familyid;
    EditText familypassword;
    Button joinfamily;


    User user;
    String token;
    int statuscode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_family);

        Intent i = getIntent();
        user = (User) i.getSerializableExtra("user");
        token = (String) i.getSerializableExtra("token");

        familyid = (EditText) findViewById(R.id.familyid);
        familypassword = (EditText) findViewById(R.id.familypassword);
        joinfamily = (Button) findViewById(R.id.btnjoinFamily);


        joinfamily.setOnClickListener(new View.OnClickListener() { // action listener for login button
            @Override
            public void onClick(View view) { // when join button is pressed...


                if (!familyid.getText().toString().equals("0") && !familyid.getText().toString().equals("") && familypassword.getText().toString().length() >= 6) {


                    JoinFamily();


                }


            }
        });
    }


    @Override
    public void processFinish(int statuscode) {

        this.statuscode = statuscode;

        if (statuscode == 201) {
            Toast.makeText(this, "You have joined family.",
                    Toast.LENGTH_LONG).show();

            Intent intent = new Intent(Join_family.this, Map.class);
            intent.putExtra("user",user);
            intent.putExtra("token", token);
            startActivity(intent);

        } else if (statuscode == 404) {
            Toast.makeText(this, "Family does not exist",
                    Toast.LENGTH_LONG).show();
        } else if (statuscode == 403) {
            Toast.makeText(this, "Wrong family join password",
                    Toast.LENGTH_LONG).show();
        } else if (statuscode == 409) {
            Toast.makeText(this, "You are already a member of family",
                    Toast.LENGTH_LONG).show();
        } else {

        }


    }

    public void JoinFamily() {
        new JoinFamilyTask(this, user, token, familyid.getText().toString(), familypassword.getText().toString()).execute();
    }


}
