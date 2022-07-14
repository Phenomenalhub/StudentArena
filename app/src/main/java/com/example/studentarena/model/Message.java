package com.example.studentarena.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Message")
public class Message extends ParseObject {
    //public static final String KEY_USER = "user";
    public static final String BODY_KEY = "body";
    public static final String SENDER_KEY = "sender";
    public static final String RECEIVER_KEY = "receiver";

    public User getSender(){
        return (User) getParseUser(SENDER_KEY);
    }
    public void setSender(User sender){
        put(SENDER_KEY, sender);
    }
    public User getReceiver(){
        return (User) getParseUser(RECEIVER_KEY);
    }
    public void setReceiver(User receiver){
        put(RECEIVER_KEY, receiver);
    }
    public String getBody() {
        return getString(BODY_KEY);
    }
    public void setBody(String body) {
        put(BODY_KEY, body);
    }

}
