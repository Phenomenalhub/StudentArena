package com.example.studentarena.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.studentarena.R;
import com.example.studentarena.adapter.MessageAdapter;
import com.example.studentarena.model.Message;
import com.example.studentarena.model.Post;
import com.example.studentarena.model.User;
import com.example.studentarena.utilities.MessageInterface;
import com.example.studentarena.utilities.MessageServices;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MessageFragment extends Fragment {
    private static final String TAG = "Message Fragment";
    RecyclerView rvMessages;
    protected MessageAdapter adapter;
    private User currentUser;
    private List<Message> messageList;

    public MessageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        messageList = new ArrayList<>();
        adapter = new MessageAdapter(messageList,getContext());
        rvMessages = view.findViewById(R.id.rvMessages);
        rvMessages.setAdapter(adapter);
        rvMessages.setHasFixedSize(true);
        rvMessages.setLayoutManager(new LinearLayoutManager(getContext()));
        currentUser = (User) ParseUser.getCurrentUser();
        getMessage();
    }

    private void getMessage() {
        ParseQuery<Message> query1 = ParseQuery.getQuery(Message.class);
        query1.whereEqualTo(Message.SENDER_KEY, (User) ParseUser.getCurrentUser());

        ParseQuery<Message> query2 = ParseQuery.getQuery(Message.class);
        query2.whereEqualTo(Message.RECEIVER_KEY, (User) ParseUser.getCurrentUser());

        List<ParseQuery<Message>> list = new ArrayList<ParseQuery<Message>>();
        list.add(query1);
        list.add(query2);

        ParseQuery<Message> query = ParseQuery.or(list);
        query.include(Message.POST_KEY);
        MessageServices newMessageService = new MessageServices(new MessageInterface() {
            @Override
            public void getProcessFinish(List<Message> output) {
                if (output == null) {
                    Toast.makeText(getContext(), "Message Loading Failed!", Toast.LENGTH_SHORT).show();
                } else {
                    messageList.clear();
                    Set<String> set = new HashSet<String>();
                    String postId;
                    for (Message m:output)
                    {
                            if (m.getTargetPost() == null) {
                                continue;
                            }
                            if (ParseUser.getCurrentUser().getObjectId().equals(m.getSender().getObjectId())) {
                                postId = ParseUser.getCurrentUser().getObjectId() + " " +
                                        m.getTargetPost().getObjectId() + " " + m.getReceiver().getObjectId();
                            } else {
                                postId = ParseUser.getCurrentUser().getObjectId() + " " +
                                        m.getTargetPost().getObjectId() + " " + m.getSender().getObjectId();
                            }
                        if (set.contains(postId)) {
                            continue;
                        } else {
                            messageList.add(m);
                            Log.i(TAG, "getProcessFinish: " + m.getCreatedAt());
                            set.add(postId);
                        }
                    }
                    if (messageList.size() == 0) {
                        rvMessages.setVisibility(View.GONE);
                    }
                    set.clear();
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void postProcessFinish(ParseException e) {
            }
        });
        newMessageService.getMessage(query);
    }
}