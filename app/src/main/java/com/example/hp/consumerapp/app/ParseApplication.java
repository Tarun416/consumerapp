package com.example.hp.consumerapp.app;

import android.app.Application;
import android.provider.Settings;
import android.util.Log;

import com.example.hp.consumerapp.utils.ParseUtils;

/**
 * Created by rahul on 17/03/16.
 */
public class ParseApplication extends Application {


    private static ParseApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        // register with parse
       ParseUtils.registerParse(getApplicationContext());

    }


    public static synchronized ParseApplication getInstance() {
        return mInstance;
    }
        /*Parse.enableLocalDatastore(this);
        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
        ParseUser.enableAutomaticUser();
        ParseACL defauACL = new ParseACL();
        defauACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defauACL, true);*/


}


