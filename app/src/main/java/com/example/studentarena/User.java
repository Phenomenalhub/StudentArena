package com.example.studentarena;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseUser;

@ParseClassName("_User")
public class User extends ParseUser {
    public static final String KEY_FIRST_NAME = "firstName";
    public static final String KEY_LAST_NAME = "lastName";

    public void setKeyFirstName(String firstName)
    {
        put(KEY_FIRST_NAME,firstName);
    }
    public void setKeyLastName(String lastName)
    {
        put(KEY_LAST_NAME,lastName);
    }

}
