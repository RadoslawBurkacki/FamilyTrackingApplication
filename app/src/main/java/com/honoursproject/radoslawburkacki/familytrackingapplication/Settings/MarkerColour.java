package com.honoursproject.radoslawburkacki.familytrackingapplication.Settings;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.honoursproject.radoslawburkacki.familytrackingapplication.CustomAdapters.AdapterSpinnerNames;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.Family;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.User;
import com.honoursproject.radoslawburkacki.familytrackingapplication.R;
import com.honoursproject.radoslawburkacki.familytrackingapplication.CustomAdapters.AdapterSpinnerColor;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MarkerColour extends AppCompatActivity {
    public static final String MY_PREFS_NAME = "FamilyCentreApplicationPrefFile";

    Button changeColor;
    SharedPreferences prefs;
    Family family;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_markercolours);

        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        changeColor = (Button) findViewById(R.id.btnchangecolor);

        family = getFamilyFromSharedPreferences();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(getResources().getString(R.string.colorsmarkersetting));

        final Spinner spinner = (Spinner) findViewById(R.id.spinnercolors);
        spinner.setAdapter(new AdapterSpinnerColor(this));

        final Spinner spinner1 = (Spinner) findViewById(R.id.spinnerMembers);
        spinner1.setAdapter(new AdapterSpinnerNames(this, family.getFamilyMembers(), prefs));


        changeColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                User u =(User) spinner1.getAdapter().getItem((int)spinner1.getSelectedItemId());


                String a = "colorForUser:" + u.getId();

                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putInt(a, (int)spinner.getAdapter().getItemId((int) spinner.getSelectedItemId()));
                editor.apply();

                displaySaveMessage(u);

                spinner1.setAdapter(new AdapterSpinnerNames(MarkerColour.this, family.getFamilyMembers(), prefs));
                spinner.setAdapter(new AdapterSpinnerColor(MarkerColour.this));
            }
        });


    }

    private void displaySaveMessage(User u){
        Toast.makeText(this, getResources().getString(R.string.coloursaved)+ " "+u.getFname() + " " +u.getLname(),
                Toast.LENGTH_LONG).show();
    }


    private Family getFamilyFromSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("Family", "");
        Family f = gson.fromJson(json, Family.class);
        return f;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
