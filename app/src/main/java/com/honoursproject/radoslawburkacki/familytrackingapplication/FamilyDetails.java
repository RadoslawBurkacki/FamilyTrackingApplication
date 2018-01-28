package com.honoursproject.radoslawburkacki.familytrackingapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.Family;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.User;

public class FamilyDetails extends AppCompatActivity {

    Family family;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_details);


        Intent i = getIntent();
        family = (Family) i.getSerializableExtra("family");


    }
}
