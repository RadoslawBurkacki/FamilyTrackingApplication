package com.honoursproject.radoslawburkacki.familytrackingapplication.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.honoursproject.radoslawburkacki.familytrackingapplication.R;

public class Family_setup extends AppCompatActivity {

    Button btnjoin;
    Button btncreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_setup);

        btnjoin = (Button) findViewById(R.id.joinfamily);
        btncreate = (Button) findViewById(R.id.createnewfamily);


        btnjoin.setOnClickListener(new View.OnClickListener() { // action listener for login button
            @Override
            public void onClick(View view) { // when join button is pressed...

                startActivity(new Intent(Family_setup.this, Join_family.class)); // open new activity called Register

            }
        });

        btncreate.setOnClickListener(new View.OnClickListener() { // action listener for login button
            @Override
            public void onClick(View view) { // when create button is pressedd...

                startActivity(new Intent(Family_setup.this, Create_family.class)); // open new activity called Register

            }
        });
    }
}
