package com.honoursproject.radoslawburkacki.familytrackingapplication.CustomAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.User;
import com.honoursproject.radoslawburkacki.familytrackingapplication.R;

import java.util.List;

public class AdapterFamilyMember extends ArrayAdapter<User> {

    public AdapterFamilyMember(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public AdapterFamilyMember(Context context, int resource, List<User> users) {
        super(context, resource, users);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.row_prechat, null);
        }

        User p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.textView3aa);


            if (tt1 != null) {
                tt1.setText(p.getFname() + " "+p.getLname());
            }


        }

        return v;
    }
}