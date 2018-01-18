package com.honoursproject.radoslawburkacki.familytrackingapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.Family;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.User;

public class Chat extends AppCompatActivity {

    User user;
    User receiver;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent i = getIntent();
        user = (User) i.getSerializableExtra("user");
        receiver = (User) i.getSerializableExtra("receiver");
        token = (String) i.getSerializableExtra("token");

        setTitle(receiver.getFname()+" " +receiver.getLname());


    }
}
