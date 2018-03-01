package com.honoursproject.radoslawburkacki.familytrackingapplication;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.Family;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.User;

import java.util.ArrayList;
import java.util.List;


public class FamilyDetails extends AppCompatActivity {

    Family family;
    User user;

    TextView lblFamilyName;
    TextView lblFamilyId;
    TextView lblFamilyCreator;
    TextView lblFamilyDetails;

    ListView list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_details);

        lblFamilyDetails = (TextView) findViewById(R.id.txtfamilydetails);
        lblFamilyName = (TextView) findViewById(R.id.txtfamilyname);
        lblFamilyId = (TextView) findViewById(R.id.txtfamilyid);
        lblFamilyCreator = (TextView) findViewById(R.id.txtfamilycreator);

        list = (ListView) findViewById(R.id.listOfFamilyMembers);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(getResources().getString(R.string.familydetails2));


        Intent i = getIntent();
        family = (Family) i.getSerializableExtra("family");
        user = (User) i.getSerializableExtra("user");

        Log.d("aaaa", family.getFamilyName());

        lblFamilyDetails.setText(getResources().getString(R.string.familydetails));
        lblFamilyId.setText(getResources().getString(R.string.familyid) + " " + family.getId());
        lblFamilyName.setText(getResources().getString(R.string.familyname) + " " + family.getFamilyName());
        for (User u : family.getFamilyMembers()) {
            if (family.getCreatorId().equals(u.getId())) {
                if (family.getCreatorId() == user.getId())
                    lblFamilyCreator.setText(getResources().getString(R.string.familycreator) + " " + u.getFname() + " " + u.getLname() + " (Me)");
                else
                    lblFamilyCreator.setText(getResources().getString(R.string.familycreator) + " " + u.getFname() + " " + u.getLname());
            }
        }


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

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
