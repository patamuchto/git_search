package com.example.testapplication.main_screen.interfaces;

import com.example.testapplication.models.RepoInfo;

import java.util.List;

/**
 * Created by vlad on 11/30/16.
 */

public interface IMainView {
    void setData(List<RepoInfo> data, boolean hasMore);
    void addPage(List<RepoInfo> page, boolean hasMore);
    void updateProgressState(boolean show);
    void restoreQuery(String query);
    void showError(Throwable t);
    void showInstructions();
    void hideInstructions();
}
