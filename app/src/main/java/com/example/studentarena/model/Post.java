package com.example.studentarena.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

@ParseClassName("Post")
public class Post extends ParseObject {

    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_CONTACT = "contact";
    public static final String KEY_TITLE = "title";
    public static final String KEY_PRICE = "price";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_LOCATION = "location";

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
    public void setPrice(Double price){put(KEY_PRICE, price);}
    public Double getPrice() {
        return getDouble(KEY_PRICE);
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
    public String getAddress(){return getString(KEY_ADDRESS);}
    public void setAddress(String address){put(KEY_ADDRESS, address);}
    public ParseGeoPoint getLocation(){return getParseGeoPoint(KEY_LOCATION);}
    public void setLocation(ParseGeoPoint parseGeoPoint){put(KEY_LOCATION, parseGeoPoint);}
}

