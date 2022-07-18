package com.example.studentarena.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.example.studentarena.model.User;

@ParseClassName("Message")
public class Message extends ParseObject {
    //public static final String KEY_USER = "user";
    public static final String BODY_KEY = "body";
    public static final String SENDER_KEY = "sender";
    public static final String RECEIVER_KEY = "receiver";
    public static final String POST_KEY = "target_post";

    public User getSender(){
        return (User) getParseObject(SENDER_KEY);
    }
    public void setSender(User sender){
        put(SENDER_KEY, sender);
    }
    public User getReceiver(){
        return (User) getParseObject(RECEIVER_KEY);
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
    public Post getTargetPost(){ return (Post) getParseObject(POST_KEY);}
    public void setTargetPost(Post targetPost){put(POST_KEY, targetPost);}

}
