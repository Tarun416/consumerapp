package com.example.hp.consumerapp;

import android.app.Application;
import android.content.Context;

/**
 * Created by rahul on 10/03/16.
 */
public class DemoApplication extends Application {
    private static  DemoApplication demoApplication = null;
    @Override
    public void onCreate() {
        super.onCreate();
        demoApplication =this;
    }
    public static Context geContext(){
        return  demoApplication.getApplicationContext();
    }
}
