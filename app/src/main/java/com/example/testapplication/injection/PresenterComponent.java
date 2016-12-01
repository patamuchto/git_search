package com.example.testapplication.injection;

import com.example.testapplication.main_screen.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by vlad on 11/30/16.
 */
@Singleton
@Component(modules = {PresenterModule.class})
public interface PresenterComponent {

    void inject(MainActivity activity);
}
