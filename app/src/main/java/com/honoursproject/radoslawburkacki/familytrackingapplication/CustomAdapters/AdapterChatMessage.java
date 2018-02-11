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

import java.util.List;

public class AdapterChatMessage extends ArrayAdapter<Message> {

    User user;
    User receiver;

    public AdapterChatMessage(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public AdapterChatMessage(Context context, int resource, List<Message> chatMessages, User user, User receiver) {
        super(context, resource, chatMessages);
        this.user = user;
        this.receiver = receiver;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.message, null);
        }

        Message chatMessage = getItem(position);

        if (chatMessage != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.message_user);
            TextView tt2 = (TextView) v.findViewById(R.id.message_time);
            TextView tt3 = (TextView) v.findViewById(R.id.message_text);


            if (tt1 != null) {
                if(chatMessage.getFromId() == user.getId()){
                    tt1.setText(user.getFname() +" " +user.getLname()+": ");
                }
                else if(chatMessage.getFromId() == receiver.getId()){
                    tt1.setText(receiver.getFname() +" " +receiver.getLname()+": ");
                }

            }

            if (tt2 != null) {

                tt2.setText(chatMessage.getDate());
            }

            if (tt3 != null) {
                tt3.setText(chatMessage.getMessage());
            }


        }

        return v;
    }
}