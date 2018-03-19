package com.honoursproject.radoslawburkacki.familytrackingapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks.DeleteUserFromFamilyTask;
import com.honoursproject.radoslawburkacki.familytrackingapplication.CustomAdapters.AdapterFamilyDetails;
import com.honoursproject.radoslawburkacki.familytrackingapplication.CustomAdapters.AdapterFamilyMember;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.Family;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.User;

import java.util.ArrayList;
import java.util.List;


public class FamilyDetails extends AppCompatActivity implements DeleteUserFromFamilyTask.AsyncResponse {

    Family family;
    User user;
    String token;

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
        token = (String) i.getSerializableExtra("token");

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

        final AdapterFamilyDetails customAdapter = new AdapterFamilyDetails(this, R.layout.row_prechat, family.getFamilyMembers());
        list.setAdapter(customAdapter);

        if (user.getId() == family.getCreatorId()) // user is the creator
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(final AdapterView<?> adapterView, View view, int i, long l) {
                    final User u = (User) adapterView.getItemAtPosition(i);


                    final CharSequence options[] = new CharSequence[]{"Remove user from family", "Cancel"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(FamilyDetails.this);
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if (options[i].equals("Remove user from family")) {

                                removeUserFromFamily(u);

                                family.getFamilyMembers().remove(u);
                                customAdapter.notifyDataSetChanged();

                            } else if (options[i].equals("Cancel")) {

                            }


                        }
                    });
                    if(u.getId()!=user.getId()) // cannot remove him self
                    builder.show();

                }
            });


    }

    private Family getFamilyFromSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences(ServerValues.MY_PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("Family", "");
        Family f = gson.fromJson(json, Family.class);
        return f;
    }

    public void removeUserFromFamily(User u) {
        new DeleteUserFromFamilyTask(this, u,family.getId(), token).execute();
    }

    @Override
    public void processFinish(Integer statuscode) {
        if (statuscode == 202) {
            Toast.makeText(this, "User has been removed",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "User couldn't be removed",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
