package com.example.testapplication.injection;

import com.example.testapplication.main_screen.MainPresenter;
import com.example.testapplication.Repository;
import com.example.testapplication.main_screen.interfaces.IMainPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by vlad on 11/30/16.
 */
@Module
public class PresenterModule {
    @Provides @Singleton
    public Repository provideRepository(){
        return new Repository();
    }

    @Provides @Singleton
    public IMainPresenter provideMainPresenter(){
        return new MainPresenter(new Repository());
    }
}
