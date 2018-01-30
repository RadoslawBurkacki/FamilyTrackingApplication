package com.honoursproject.radoslawburkacki.familytrackingapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.Family;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FamilyDetails extends AppCompatActivity {

    Family family;

    TextView lblFamilyName;
    TextView lblFamilyId;
    ListView list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_details);

        lblFamilyName = (TextView) findViewById(R.id.lblFamilyName);
        lblFamilyId = (TextView) findViewById(R.id.lblFamilyId);

        list = (ListView) findViewById(R.id.listOfFamilyMembers);

        Intent i = getIntent();
        family = (Family) i.getSerializableExtra("family");

        lblFamilyName.setText("Family Name: " + family.getFamilyName());
        lblFamilyId.setText("Family unique identifier is: " + family.getId());

        list.setBackgroundColor(0xFFD3D3D3);


        final List<String> family_list = new ArrayList<String>();

        int membernumber = 1;

        for (User u : family.getFamilyMembers()) {
            family_list.add(membernumber + ". " + u.getFname() + " " + u.getLname());
            membernumber++;
        }

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, family_list);

        list.setAdapter(arrayAdapter);


        arrayAdapter.notifyDataSetChanged();


    }
}
