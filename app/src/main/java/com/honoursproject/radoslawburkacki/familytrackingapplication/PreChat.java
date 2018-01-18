package com.honoursproject.radoslawburkacki.familytrackingapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.Family;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PreChat extends AppCompatActivity {

    private final String TAG = "PreChat";

    ListView listView;

    User user;
    Family family;
    String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_chat);

        listView = (ListView) findViewById(R.id.listview);

        Intent i = getIntent();
        user = (User) i.getSerializableExtra("user");
        token = (String) i.getSerializableExtra("token");
        family = (Family) i.getSerializableExtra("family");

        for (User u : family.getFamilyMembers()) {
            if (u.getId() == user.getId()) {
                family.getFamilyMembers().remove(u);
                break;
            }
        }

        AdapterFamilyMember customAdapter = new AdapterFamilyMember(this, R.layout.row_prechat, family.getFamilyMembers());
        listView.setAdapter(customAdapter);


        setTitle("Send message to:");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(PreChat.this, Chat.class); // instance id? for recoognition of each unique chat??
                intent.putExtra("user", user);
                intent.putExtra("receiver", family.getFamilyMembers().get(i));
                intent.putExtra("token", token);
                startActivity(intent);

            }
        });

    }

}

