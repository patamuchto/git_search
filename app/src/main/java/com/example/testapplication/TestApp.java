package com.example.testapplication;

import android.app.Application;

import com.example.testapplication.injection.DaggerPresenterComponent;
import com.example.testapplication.injection.PresenterComponent;

/**
 * Created by vlad on 11/30/16.
 */

public class TestApp extends Application {
    private static TestApp instance;
    private PresenterComponent presenterComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        presenterComponent = DaggerPresenterComponent.builder().build();
    }

    public PresenterComponent getPresenterComponent(){
        return presenterComponent;
    }

    public static TestApp getInstance(){
        return instance;
    }
}
