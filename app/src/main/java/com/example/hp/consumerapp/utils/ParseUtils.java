package com.example.hp.consumerapp.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SendCallback;

/**
 * Created by rahul on 17/03/16.
 */
public class ParseUtils {
   // i5YGC0IKzNbkgZhbOXUQgCM2mmt4FaEWQEL9g3xY
  //  eTQi6jRbry5kcxJ07ioGXQRetV2LP5kNoio42V6S
    private static final String APPLICATION_ID = "yzCJSEpMYxI2BMMX0bzVJp1f3uQ1GrAzthFwtRPy";
    private static final String CLIENT_KEY = "YkGd1WAfVLhUC4ltdpPwZJWiuqS3TZS5eYA0DLLo";
    private static final String PARSE_CHANNEL = "mVisa";
    //private static final String APPLICATION_ID="";
    private static String TAG = ParseUtils.class.getSimpleName();
    private static String deviceToken;
    public static void verifyParseConfiguration(Context context) {
        if (TextUtils.isEmpty(APPLICATION_ID) || TextUtils.isEmpty(CLIENT_KEY)) {
            Toast.makeText(context, "Please configure your Parse Application ID and Client Key in AppConfig.java", Toast.LENGTH_LONG).show();
            ((Activity) context).finish();
        }
    }

    public static void registerParse(Context context) {

        //deviceToken = dt;
        // initializing parse library
        Parse.initialize(context, APPLICATION_ID, CLIENT_KEY);
        ParseInstallation.getCurrentInstallation().saveInBackground();
      //  ParseUser.enableAutomaticUser();



        ParsePush.subscribeInBackground(PARSE_CHANNEL, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                String devicetoken=(String)ParseInstallation.getCurrentInstallation().get("deviceToken");
//                Log.d("token",devicetoken);
                Log.d(TAG, "Successfully subscribed to Parse!");
            }
        });
    }

    public static void subscribeWithEmail() {
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
      //  installation.put("email",email);
        //Log.d("Email", email);
        //installation.put("email", email);
        installation.saveInBackground();
        ParsePush push = new ParsePush();
       // ouch_dzW0O4HlfS
       // mVisa_GAB2bDQdkj
        push.setChannel("touch_dzW0O4HlfS");
        push.setMessage("Payment Received Successfully");
        push.sendInBackground(new SendCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                    Log.d("error",e.getMessage());
                }else {
                    Log.d("sucks","ggg");
                }
            }
        });
    }
}

