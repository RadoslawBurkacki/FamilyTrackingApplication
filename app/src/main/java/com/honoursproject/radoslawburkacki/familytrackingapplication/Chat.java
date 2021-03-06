package com.honoursproject.radoslawburkacki.familytrackingapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import com.honoursproject.radoslawburkacki.familytrackingapplication.AsyncTasks.SendChatMessageTask;
import com.honoursproject.radoslawburkacki.familytrackingapplication.CustomAdapters.AdapterChatMessage;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Database.dbHandler;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.Message;
import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Chat extends AppCompatActivity implements SendChatMessageTask.AsyncResponse {

    private dbHandler db;


    private AdapterChatMessage adapterChatMessage;

    private FloatingActionButton fab;
    private EditText message;
    private RecyclerView listOfMessages;

    private User user;
    private User receiver;
    private String token;

    private DateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");

    private List<Message> messages = new ArrayList<Message>();

    private MyBroadcastReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        listOfMessages = (RecyclerView) findViewById(R.id.list_of_messages);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        message = (EditText) findViewById(R.id.input);
        db = new dbHandler(this);

        SharedPreferences prefs = getSharedPreferences(ServerValues.MY_PREFS_NAME, MODE_PRIVATE);
        token = prefs.getString("token", null);

        Intent i = getIntent();
        user = (User) i.getSerializableExtra("user");
        receiver = (User) i.getSerializableExtra("receiver");

        messages.add(new Message((long) 0, user.getId(), receiver.getId(), message.getText().toString(), dateFormat.format(new Date())));

        setUpChat();

        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (message.getText().toString().trim().length() > 0){

                    Message m = new Message((long) 0, user.getId(), receiver.getId(), message.getText().toString(), dateFormat.format(new Date()));
                    messages.add(m);    //display message
                    db.addMessage(m);   // save message to db
                    sendMessage(m);     // send message to server
                    message.setText("");    //

                    int newMsgPosition = adapterChatMessage.getItemCount()-1;

                    // Notify recycler view insert one new data.
                    adapterChatMessage.notifyItemInserted(newMsgPosition);

                    // Scroll RecyclerView to the last message.
                    listOfMessages.scrollToPosition(newMsgPosition);



                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(message.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                else {

                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        myReceiver = new MyBroadcastReceiver();
        final IntentFilter intentFilter = new IntentFilter("newmessage");
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (myReceiver != null)
            LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
        myReceiver = null;
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            setUpChat();
        }
    }


    private void sendMessage(Message m) {
        new SendChatMessageTask(this, m, token).execute();
    }

    @Override
    public void processFinish(int statuscode) {
    }

    private void setUpChat() {
        messages.clear();
        setTitle(receiver.getFname() + " " + receiver.getLname());

        List<Message> last20Messages = db.get20lastMessages(receiver.getId(), user.getId());


        for (Message cn : last20Messages) {

            Log.d("Reading: ", cn.toString());

            messages.add(cn);

        }



        listOfMessages.setAdapter(adapterChatMessage = new AdapterChatMessage(this, messages,user,receiver));
        listOfMessages.setLayoutManager(new LinearLayoutManager(this));


        int newMsgPosition = adapterChatMessage.getItemCount()-1;

        // Notify recycler view insert one new data.
        adapterChatMessage.notifyItemInserted(newMsgPosition);

        // Scroll RecyclerView to the last message.
        listOfMessages.scrollToPosition(newMsgPosition);


    }


}
