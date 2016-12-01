package com.example.testapplication.main_screen;

import android.util.Log;

import com.example.testapplication.Repository;
import com.example.testapplication.main_screen.interfaces.IMainPresenter;
import com.example.testapplication.main_screen.interfaces.IMainView;
import com.example.testapplication.models.RepoInfo;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by vlad on 11/30/16.
 */

public class MainPresenter implements IMainPresenter{
    private static final String TAG = MainPresenter.class.getSimpleName();

    private Repository repository;
    private IMainView view;
    private int currentPage = 0;

    private List<RepoInfo> cache;
    private String currentQuery;
    private boolean hasMore = true;

    public MainPresenter(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void onStart(IMainView view) {
        this.view = view;

        if (cache!=null&&cache.size()>0) {
            view.setData(cache, true);
            view.hideInstructions();
        }

        if (currentQuery!=null)
            view.restoreQuery(currentQuery);
    }

    @Override
    public void onStop() {
        view = null;
    }

    @Override
    public void loadMore() {
        if (view!=null)
            view.updateProgressState(true);

        repository.searchRepositories(currentQuery, ++currentPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(searchResultsContainer -> {
                            cache.addAll(searchResultsContainer.getItems());
                            hasMore = cache.size() < searchResultsContainer.getTotalCount();
                            if (view != null) {
                                if (currentPage == 1)
                                    view.setData(searchResultsContainer.getItems(), hasMore);
                                else
                                    view.addPage(searchResultsContainer.getItems(), hasMore);

                                view.updateProgressState(false);
                            }
                        },
                        throwable -> view.showError(throwable));
    }

    @Override
    public void search(String query) {
        if (currentQuery!=null && currentQuery.equals(query))
            return;

        if (query.length()<=2) {
            view.showInstructions();
            return;
        }

        view.hideInstructions();
        Log.e(TAG, "Searching for:"+query);

        cache = new ArrayList<>();
        currentQuery = query;
        hasMore = true;
        currentPage = 0;

        loadMore();
    }
}
