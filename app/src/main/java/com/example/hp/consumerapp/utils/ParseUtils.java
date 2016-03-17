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
    private static final String APPLICATION_ID="";
    private static final String CLIENT_KEY="";
    //private static final String APPLICATION_ID="";
    private static String TAG = ParseUtils.class.getSimpleName();
    private static String deviceToken;
    public static void verifyParseConfiguration(Context context) {
        if (TextUtils.isEmpty(APPLICATION_ID) || TextUtils.isEmpty(CLIENT_KEY)) {
            Toast.makeText(context, "Please configure your Parse Application ID and Client Key in AppConfig.java", Toast.LENGTH_LONG).show();
            ((Activity) context).finish();
        }
    }

    public static void registerParse(Context context, String dt) {

        deviceToken = dt;
        // initializing parse library
        Parse.initialize(context, APPLICATION_ID, CLIENT_KEY);
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParseUser.enableAutomaticUser();



        ParsePush.subscribeInBackground("PARSE_CHANNEL", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.e(TAG, "Successfully subscribed to Parse!");
            }
        });
    }

    public static void subscribeWithEmail(String email) {
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        Log.d("Email", email);
        installation.put("email", email);
        installation.saveInBackground();
        ParsePush push = new ParsePush();
        push.setChannel("AndroidParsePush");
        push.setMessage("Hey");
        push.sendInBackground(new SendCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                }
            }
        });
    }
}

