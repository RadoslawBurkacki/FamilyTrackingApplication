package com.honoursproject.radoslawburkacki.familytrackingapplication.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.User;
import com.honoursproject.radoslawburkacki.familytrackingapplication.R;

public class Join_family extends AppCompatActivity {

    EditText familyid;
    EditText familypassword;
    Button joinfamily;

    User user;
    String token;

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


                if(!familyid.getText().toString().equals("0") && !familyid.getText().toString().equals("") && familypassword.getText().toString().length() >= 6 ){



                }


            }
        });
    }
}
