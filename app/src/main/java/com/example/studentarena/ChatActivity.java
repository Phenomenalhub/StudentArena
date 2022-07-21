package com.example.studentarena;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.studentarena.adapter.ChatAdapter;
import com.example.studentarena.model.Message;
import com.example.studentarena.model.Post;
import com.example.studentarena.model.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;

import org.parceler.Parcels;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;
    RecyclerView rvChat;
    private Button ibSend;
    private EditText etMessage;
    protected ChatAdapter cAdapter;
    private List<Message> chats;
    boolean mFirstLoad;
    private ImageView ivProfileImage;
    public User otherUser;
    public Post targetPost;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        otherUser = (User) Parcels.unwrap(getIntent().getParcelableExtra("otherUser"));
        targetPost = (getIntent().getParcelableExtra("targetPost"));
        if (otherUser != null) {
            setupMessagePosting(otherUser);
        }

    }
    // Setup message field and posting
    void setupMessagePosting(User otherUser) {
        etMessage = findViewById(R.id.etMessage);
        rvChat = findViewById(R.id.rvChat);
        ibSend = findViewById(R.id.ibSend);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        chats = new ArrayList<>();
        mFirstLoad = true;
        final String userId = ParseUser.getCurrentUser().getObjectId();
        cAdapter = new ChatAdapter(ChatActivity.this, userId, chats);
        rvChat.setAdapter(cAdapter);
        // associate the LayoutManager with the RecyclerView
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        linearLayoutManager.setReverseLayout(true);
        rvChat.setLayoutManager(linearLayoutManager);
        // When send button is clicked, create message object on Parse
        ibSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = etMessage.getText().toString();
                Message message = new Message();
                message.setSender((User) ParseUser.getCurrentUser());
                message.setReceiver(otherUser);
                message.setBody(data);
                message.setTargetPost(targetPost);
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast.makeText(ChatActivity.this, "Successfully created message on Parse",
                                Toast.LENGTH_SHORT).show();
                        chats.add(0,message);
                        cAdapter.notifyDataSetChanged();
                    }
                });
                etMessage.setText(null);
            }
        });
        refreshMessages();
        String websocketUrl = "wss://studentarena.b4a.io";

        ParseLiveQueryClient parseLiveQueryClient = null;
        try {
            parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient(new URI(websocketUrl));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        ParseQuery<Message> parseQuery = ParseQuery.getQuery(Message.class);
        // This query can even be more granular (i.e. only refresh if the entry was added by some other user)
        // parseQuery.whereNotEqualTo(USER_ID_KEY, ParseUser.getCurrentUser().getObjectId());

        // Connect to Parse server
        SubscriptionHandling<Message> subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);

        // Listen for CREATE events on the Message class
        subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, (query, object) -> {
            chats.add(0, object);

            // RecyclerView updates need to be run on the UI thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("live query", "run:");
                    cAdapter.notifyDataSetChanged();
                    rvChat.scrollToPosition(0);
                }
            });
        });
    }
    // Query messages from Parse so we can load them into the chat adapter
    void refreshMessages() {
        ParseQuery<Message> query1 = ParseQuery.getQuery(Message.class);
        query1.whereEqualTo(Message.SENDER_KEY, ParseUser.getCurrentUser());
        query1.whereEqualTo(Message.RECEIVER_KEY, otherUser);
        ParseQuery<Message> query2 = ParseQuery.getQuery(Message.class);
        query2.whereEqualTo(Message.SENDER_KEY, otherUser);
        query2.whereEqualTo(Message.RECEIVER_KEY, ParseUser.getCurrentUser());

        List<ParseQuery<Message>> listOfquery = new ArrayList<>();
        listOfquery.add(query1);
        listOfquery.add(query2);

        // Construct query to execute
        ParseQuery<Message> query = ParseQuery.or(listOfquery);
         //Configure limit and sort order
        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
        query.include(Message.SENDER_KEY);
        query.include(Message.RECEIVER_KEY);
        query.whereEqualTo(Message.POST_KEY, targetPost);
//      get the latest 50 messages, order will show up newest to oldest of this group
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<Message>() {
            public void done(List<Message> messages, ParseException e) {
                if (e == null) {
                    chats.clear();
                    chats.addAll(messages);
                    cAdapter.notifyDataSetChanged(); // update adapter
                    // Scroll to the bottom of the list on initial load
                    if (mFirstLoad) {
                        rvChat.scrollToPosition(0);
                        mFirstLoad = false;
                    }
                } else {
                    Log.e("message", "Error Loading Messages" + e);
                }
            }
        });
    }
}