package com.example.studentarena.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("Post")
public class Post extends ParseObject {

    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_CONTACT = "contact";
    public static final String KEY_TITLE = "title";
    public static final String KEY_PRICE = "price";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";

    public void setDescription(String description){
        put(KEY_DESCRIPTION, description);
    }
    public ParseFile getImage(){
        return getParseFile(KEY_IMAGE);
    }
    public void setImage(ParseFile parseFile){
        put(KEY_IMAGE, parseFile);
    }
    public User getUser(){
        return (User) getParseUser(KEY_USER);
    }
    public void setUser(User user){
        put(KEY_USER, user);
    }
    public void setContact(String contact){put(KEY_CONTACT, contact);}
    public void setTitle(String title){put(KEY_TITLE, title);}
    public void setPrice(String price){put(KEY_PRICE, price);}
    public String getPrice() {
        return getString(KEY_PRICE);
    }
    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }
    public String getContact(){
        return getString(KEY_CONTACT);
    }
    public String getTitle() {
        return getString(KEY_TITLE);
    }
}

