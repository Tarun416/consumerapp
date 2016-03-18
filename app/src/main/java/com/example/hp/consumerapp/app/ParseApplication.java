package com.example.hp.consumerapp.app;

import android.app.Application;
import android.provider.Settings;
import android.util.Log;

import com.example.hp.consumerapp.utils.ParseUtils;
import com.parse.ParseACL;
import com.parse.ParseUser;

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
       ParseUtils.registerParse(this);
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL=new ParseACL();
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

    }


  //  public static synchronized ParseApplication getInstance() {
     //   return mInstance;
   // }
        /*Parse.enableLocalDatastore(this);
        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
        ParseUser.enableAutomaticUser();
        ParseACL defauACL = new ParseACL();
        defauACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defauACL, true);*/

   /* public void onCreate() {
        super.onCreate();

        //    Parse.initialize(this, "6lGeZuvASHdCjO1d7IZdg20IHYOORRnlUtFxnZEO", "SfrZudDyznXPzsrYUJNk6O5VAWqFhc2NbIza8VGZ");
        // ParseInstallation.getCurrentInstallation().saveInBackground();
        ParseUtils.registerParse(this);

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL=new ParseACL();
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL,true);
    }*/


}


