package com.honoursproject.radoslawburkacki.familytrackingapplication.CustomAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.Message;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.User;
import com.honoursproject.radoslawburkacki.familytrackingapplication.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterSettings extends ArrayAdapter<String> {

    List<String> settingsList =  new ArrayList<>();
    List<String> settingsDescList =  new ArrayList<>();

    public AdapterSettings(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public AdapterSettings(Context context, int resource, List<String> settingsList, List<String> settingsDescList) {
        super(context, resource, settingsList);
        this.settingsList = settingsList;
        this.settingsDescList = settingsDescList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.row_settings, null);
        }

        String setting = getItem(position);

        if (setting != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.title);
            TextView tt2 = (TextView) v.findViewById(R.id.desc);


            if (tt1 != null) {

                tt1.setText(settingsList.get(position));


            }

            if (tt2 != null) {

                tt2.setText(settingsDescList.get(position));
            }


        }

        return v;
    }
}