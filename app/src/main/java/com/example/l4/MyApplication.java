package com.example.l4;

import android.app.Application;

import com.example.l4.di.AppComponent;
import com.example.l4.di.DaggerAppComponent;
import com.example.l4.di.DaggerModule;

public class MyApplication extends Application {

    private static AppComponent di;

    @Override
    public void onCreate() {
        super.onCreate();
        di = DaggerAppComponent.builder().daggerModule(new DaggerModule(getApplicationContext())).build();
    }

    public static AppComponent getDi(){
        return di;
    }
}