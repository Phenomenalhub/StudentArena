package com.example.studentarena.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.studentarena.R;
import com.example.studentarena.adapter.ChatAdapter;
import com.example.studentarena.adapter.PostsAdapter;
import com.example.studentarena.model.Message;
import com.example.studentarena.model.Post;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment {
    private static final String TAG = "Message Fragment";
    RecyclerView rvMessages;
    protected ChatAdapter cAdapter;
    private List<Message> chats;
    private List<String> messageList;

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
        rvMessages = view.findViewById(R.id.rvMessages);
        rvMessages.setHasFixedSize(true);
        rvMessages.setLayoutManager(new LinearLayoutManager(getContext()));
        messageList = new ArrayList<>();


    }
}