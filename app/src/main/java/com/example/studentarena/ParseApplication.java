package com.example.studentarena;

import android.app.Application;

import com.example.studentarena.model.Post;
import com.example.studentarena.model.User;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(User.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.application_id))
                .clientKey(getString(R.string.client_key_id))
                .server(getString(R.string.server)).build());
    }
}
