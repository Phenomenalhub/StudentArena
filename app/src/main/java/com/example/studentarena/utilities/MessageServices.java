package com.example.studentarena.utilities;

import android.util.Log;

import com.example.studentarena.model.Message;
import com.example.studentarena.utilities.MessageInterface;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

public class MessageServices {
    public static final String TAG = "MessageServices";
    public MessageInterface delegate = null;
    public MessageServices(MessageInterface asyncResponse) {
        delegate = asyncResponse;
    }

    public void getMessage(ParseQuery<Message> messageParseQuery) {

        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);

        if (messageParseQuery != null) {
            query = messageParseQuery;
        }
        query.include(Message.BODY_KEY);
        query.include(Message.RECEIVER_KEY);
        query.include(Message.SENDER_KEY);
        query.include(Message.KEY_CREATED_AT);
        query.orderByDescending(Message.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Message>() {
            @Override
            public void done(List<Message> messages, ParseException e) {
                if (e==null) {
                    delegate.getProcessFinish(messages);
                } else {
                    Log.i(TAG, "done: " + e.toString());
                    delegate.getProcessFinish(null);
                }
            }
        });

    }

    public void sendMessage(Message message) {
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                delegate.postProcessFinish(e);
            }
        });
    }

}
