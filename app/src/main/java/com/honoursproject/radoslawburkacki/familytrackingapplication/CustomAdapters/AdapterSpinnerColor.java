package com.honoursproject.radoslawburkacki.familytrackingapplication.CustomAdapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.honoursproject.radoslawburkacki.familytrackingapplication.R;

import java.util.ArrayList;

/**
 * Created by radek on 27/02/2018.
 */

public class AdapterSpinnerColor extends BaseAdapter {
    ArrayList<Integer> colors;
    ArrayList<String> colornames = new ArrayList<String>();
    Context context;


    public AdapterSpinnerColor(Context context) {
        this.context = context;

        colornames.add(context.getResources().getString(R.string.color0));
        colornames.add(context.getResources().getString(R.string.color1));
        colornames.add(context.getResources().getString(R.string.color2));
        colornames.add(context.getResources().getString(R.string.color3));
        colornames.add(context.getResources().getString(R.string.color4));
        colornames.add(context.getResources().getString(R.string.color5));
        colornames.add(context.getResources().getString(R.string.color6));
        colornames.add(context.getResources().getString(R.string.color7));
        colornames.add(context.getResources().getString(R.string.color8));
        colornames.add(context.getResources().getString(R.string.color9));

        colors = new ArrayList<Integer>();
        int retrieve[] = context.getResources().getIntArray(R.array.androidcolors);
        for (int re : retrieve) {
            colors.add(re);
        }
    }

    @Override
    public int getCount() {
        return colors.size();
    }

    @Override
    public String getItem(int arg0) {
        return colornames.get(arg0);
    }


    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int pos, View view, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, null);
        TextView txv = (TextView) view.findViewById(android.R.id.text1);
        txv.setBackgroundColor(colors.get(pos));
        txv.setTextSize(20f);
        txv.setText(colornames.get(pos));
        return view;
    }

}
