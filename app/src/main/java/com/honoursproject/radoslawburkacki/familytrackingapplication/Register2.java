package com.honoursproject.radoslawburkacki.familytrackingapplication;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Register2 extends AppCompatActivity {

    EditText t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        t = (EditText) findViewById(R.id.editText2);

        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog();// problem z dodatniem daty
                dialog.show();

            }
        });
    }
}
