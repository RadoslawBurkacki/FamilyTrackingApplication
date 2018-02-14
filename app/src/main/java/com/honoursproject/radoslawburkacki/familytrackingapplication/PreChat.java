package com.honoursproject.radoslawburkacki.familytrackingapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.honoursproject.radoslawburkacki.familytrackingapplication.CustomAdapters.AdapterFamilyMember;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.Family;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.User;

public class PreChat extends AppCompatActivity {
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private final String TAG = "PreChat";

    ListView listView;

    User user;
    Family family;
    String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_chat);

        listView = (ListView) findViewById(R.id.familymemberlist);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        token = prefs.getString("token",null);

        Intent i = getIntent();
        user = (User) i.getSerializableExtra("user");

        family = (Family) i.getSerializableExtra("family");

        for (User u : family.getFamilyMembers()) {
            if (u.getId() == user.getId()) {
                family.getFamilyMembers().remove(u);
                break;
            }
        }


        AdapterFamilyMember customAdapter = new AdapterFamilyMember(this, R.layout.row_prechat, family.getFamilyMembers());
        listView.setAdapter(customAdapter);


        setTitle("Chat with:");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



                Intent intent = new Intent(PreChat.this, Chat.class); // instance id? for recoognition of each unique chat??
                intent.putExtra("user", user);
                intent.putExtra("receiver", family.getFamilyMembers().get(i));
                startActivity(intent);

            }
        });

    }

}

