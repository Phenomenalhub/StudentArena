package com.example.studentarena;

import android.app.Application;

import com.example.studentarena.model.Message;
import com.example.studentarena.model.Post;
import com.example.studentarena.model.User;
import com.parse.Parse;
import com.parse.ParseObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Use for monitoring Parse network traffic
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        // any network interceptors must be added with the Configuration Builder given this syntax
        builder.networkInterceptors().add(httpLoggingInterceptor);
        ParseObject.registerSubclass(Message.class);
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(User.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.application_id))
                .clientKey(getString(R.string.client_key_id))
                .server(getString(R.string.server)).build());
    }
}
