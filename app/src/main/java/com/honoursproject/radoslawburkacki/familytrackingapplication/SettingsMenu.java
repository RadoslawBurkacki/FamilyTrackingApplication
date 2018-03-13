package com.honoursproject.radoslawburkacki.familytrackingapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import android.os.Bundle;
import android.app.Activity;


import com.honoursproject.radoslawburkacki.familytrackingapplication.CustomAdapters.AdapterSettings;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Database.dbHandler;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Settings.MarkerColour;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class SettingsMenu extends AppCompatActivity {
    public static final String MY_PREFS_NAME = "FamilyCentreApplicationPrefFile";

    private ListView listView;
    private dbHandler db;
    private List<String> settings = new ArrayList<>();
    private List<String> settingsdesc = new ArrayList<>();
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_menu);

        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        listView = (ListView) findViewById(R.id.listsettings);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(getResources().getString(R.string.settings));

        setUpSettings();

        AdapterSettings adapterSettings = new AdapterSettings(this, R.layout.row_settings, settings, settingsdesc);
        listView.setAdapter(adapterSettings);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (settings.get(i).toString().equals(getResources().getString(R.string.settingmarkerseset))) {
                    Intent intent = new Intent(SettingsMenu.this, MarkerColour.class); // instance id? for recoognition of each unique chat??
                    startActivity(intent);
                } else if (settings.get(i).toString().equals(getResources().getString(R.string.language))) {

                    final CharSequence colors[] = new CharSequence[]{"English", "Polski"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingsMenu.this);
                    builder.setItems(colors, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if (colors[i].equals("Polski")) {
                                saveLanguageToSharedpref("pl");

                                Locale locale = new Locale("pl");
                                Resources res = getResources();
                                DisplayMetrics dm = res.getDisplayMetrics();
                                Configuration conf = res.getConfiguration();
                                conf.locale = locale;
                                res.updateConfiguration(conf, dm);


                                overridePendingTransition(0, 0);
                                Intent refresh = new Intent(SettingsMenu.this, Map.class);
                                refresh.addFlags(refresh.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(refresh);
                                finish();


                            } else if (colors[i].equals("English")) {
                                saveLanguageToSharedpref("en");

                                Locale locale = new Locale("en");
                                Resources res = getResources();
                                DisplayMetrics dm = res.getDisplayMetrics();
                                Configuration conf = res.getConfiguration();
                                conf.locale = locale;
                                res.updateConfiguration(conf, dm);



                                overridePendingTransition(0, 0);
                                Intent refresh = new Intent(SettingsMenu.this, Map.class);
                                refresh.addFlags(refresh.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(refresh);
                                finish();

                            }


                        }
                    });
                    builder.show();
                } else if (settings.get(i).toString().equals(getResources().getString(R.string.clearchathistory))) {

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    db = new dbHandler(SettingsMenu.this);
                                    db.removeAllMesagess();
                                    Toast.makeText(SettingsMenu.this, getResources().getString(R.string.allmsgremoved),
                                            Toast.LENGTH_LONG).show();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingsMenu.this);
                    builder.setMessage(getResources().getString(R.string.removechatmessages)).setPositiveButton(getResources().getString(R.string.yes), dialogClickListener)
                            .setNegativeButton(getResources().getString(R.string.no), dialogClickListener).show();

                }


            }
        });

    }

    private void saveLanguageToSharedpref(String arg) {
        editor.putString("language", arg);
        editor.apply();
    }

    private void setUpSettings() {
        settings.add(getResources().getString(R.string.settingmarkerseset));
        settingsdesc.add(getResources().getString(R.string.settingmarkerdesc));
        settings.add(getResources().getString(R.string.language));
        settingsdesc.add(getResources().getString(R.string.languagedesc));
        settings.add(getResources().getString(R.string.clearchathistory));
        settingsdesc.add(getResources().getString(R.string.clearchathistorydesc));


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
