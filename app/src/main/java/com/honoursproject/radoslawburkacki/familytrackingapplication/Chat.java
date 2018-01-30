package com.honoursproject.radoslawburkacki.familytrackingapplication;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks.SendChatMessageTask;
import com.honoursproject.radoslawburkacki.familytrackingapplication.CustomAdapters.AdapterChatMessage;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.Message;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Chat extends AppCompatActivity implements SendChatMessageTask.AsyncResponse {

    private static final String TAG = "ChatActivity";


    FloatingActionButton fab;
    EditText message;

    private User user;
    private User receiver;
    private String token;

    private Date date = new Date();
    private DateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");

    ListView listOfMessages;

    List<Message> messages = new ArrayList<Message>();


    private ReflectiveTypeAdapterFactory.Adapter<Message> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        message = (EditText) findViewById(R.id.input);

        Intent i = getIntent();
        user = (User) i.getSerializableExtra("user");
        receiver = (User) i.getSerializableExtra("receiver");
        token = (String) i.getSerializableExtra("token");

        setTitle(receiver.getFname() + " " + receiver.getLname());

        final ListView listOfMessages = (ListView) findViewById(R.id.list_of_messages);

        messages.add(new Message((long) 0, user.getId(), receiver.getId(), "Message1 ", dateFormat.format(date)));
        messages.add(new Message((long) 0, user.getId(), receiver.getId(), "Message2 ", dateFormat.format(date)));
        messages.add(new Message((long) 0, user.getId(), receiver.getId(), "Message3 ", dateFormat.format(date)));


        AdapterChatMessage customAdapter = new AdapterChatMessage(this, R.layout.message, messages, user);

        listOfMessages.setAdapter(customAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                messages.add(new Message((long) 0, user.getId(), receiver.getId(), message.getText().toString(), dateFormat.format(date)));

                sendMessage(new Message((long) 0, user.getId(), receiver.getId(), message.getText().toString(), dateFormat.format(date)));

                message.setText("");

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(listOfMessages.getWindowToken(), 0);

            }
        });


    }

    public void sendMessage(Message m) {
        new SendChatMessageTask(this, m, token).execute();
    }

    @Override
    public void processFinish(int statuscode) {
    }


}
