package com.example.studentarena;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("WO7pgZgM1VlakaqvPjvF6oPnv5hdOhJWlwrea52u")
                .clientKey("6u58gIom9F6TUKd74yTnwDFe1GqRLBOzyvFm8QYV")
                .server("https://parseapi.back4app.com").build());
    }
}
