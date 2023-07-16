package com.example.boulderate;

import android.app.Application;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Imager.initHelper(this);
    }
}

