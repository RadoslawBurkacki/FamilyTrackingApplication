package com.honoursproject.radoslawburkacki.familytrackingapplication.CustomAdapters;

/**
 * Created by radek on 19/03/2018.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.User;
import com.honoursproject.radoslawburkacki.familytrackingapplication.R;

import java.util.List;

public class AdapterFamilyDetails extends ArrayAdapter<User> {

    public AdapterFamilyDetails(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public AdapterFamilyDetails(Context context, int resource, List<User> users) {
        super(context, resource, users);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(android.R.layout.simple_list_item_1, null);
        }

        User p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(android.R.id.text1);


            if (tt1 != null) {
                tt1.setText((position+1)+". "+p.getFname() + " "+p.getLname());
            }


        }

        return v;
    }
}