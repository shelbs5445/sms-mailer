package com.android.google.settings;

import android.app.Application;

import com.theah64.safemail.SafeMail;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        SafeMail.init("9dsyI89Pty");
    }
}
