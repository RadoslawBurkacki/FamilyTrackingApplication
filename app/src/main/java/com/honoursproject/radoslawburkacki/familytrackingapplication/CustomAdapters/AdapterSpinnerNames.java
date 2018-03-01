package com.honoursproject.radoslawburkacki.familytrackingapplication.CustomAdapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.User;
import com.honoursproject.radoslawburkacki.familytrackingapplication.R;


import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by radek on 27/02/2018.
 */

public class AdapterSpinnerNames extends BaseAdapter {
    public static final String MY_PREFS_NAME = "FamilyCentreApplicationPrefFile";

    List<User> userList = new ArrayList<User>();
    Context context;
    SharedPreferences prefs;

    public AdapterSpinnerNames(Context context, List<User> list, SharedPreferences pref) {
        this.context = context;
        this.userList = list;

        prefs = pref;

    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public User getItem(int arg0) {
        return userList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int pos, View view, ViewGroup parent) {
        User u = userList.get(pos);
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, null);
        TextView txv = (TextView) view.findViewById(android.R.id.text1);
        txv.setBackgroundColor(getMarkerColour((int)u.getId()));
        txv.setId((int) u.getId());
        txv.setTextSize(20f);
        txv.setText(u.getFname() + " " + u.getFname());
        return view;
    }

    public Integer getMarkerColour(int userid) {

        String a = "colorForUser:" + userid;
        int i = prefs.getInt(a, 6);


        switch (i) {
            case 0:
                return context.getResources().getColor(R.color.azure);
            case 1:
                return context.getResources().getColor(R.color.blue);
            case 2:
                return context.getResources().getColor(R.color.cyan);
            case 3:
                return context.getResources().getColor(R.color.green);
            case 4:
                return context.getResources().getColor(R.color.magnata);
            case 5:
                return context.getResources().getColor(R.color.orange);
            case 6:
                return context.getResources().getColor(R.color.red);
            case 7:
                return context.getResources().getColor(R.color.rose);
            case 8:
                return context.getResources().getColor(R.color.violet);
            case 9:
                return context.getResources().getColor(R.color.yellow);

        }
         return context.getResources().getColor(R.color.red);
    }


}
