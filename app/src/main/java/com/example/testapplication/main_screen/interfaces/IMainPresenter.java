package com.example.testapplication.main_screen.interfaces;

/**
 * Created by vlad on 11/30/16.
 */

public interface IMainPresenter {
    void onStart(IMainView view);
    void onStop();
    void loadMore();
    void search(String query);
}
