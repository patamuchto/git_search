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

public class MainPresenter implements IMainPresenter {
    private static final String TAG = MainPresenter.class.getSimpleName();

    private static final int PAGE_SIZE = 30;

    private Repository repository;
    private IMainView view;
    private int currentPage = 0;

    private List<RepoInfo> cache;
    private List<RepoInfo> incompletePage;
    private String currentQuery;
    private boolean hasMore = true;

    public MainPresenter(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void onStart(IMainView view) {
        this.view = view;

        if (cache != null && cache.size() > 0) {
            view.setData(cache, true);
            view.hideInstructions();
        }

        if (currentQuery != null)
            view.restoreQuery(currentQuery);
    }

    @Override
    public void onStop() {
        view = null;
    }

    @Override
    public void loadMore() {
        if (view != null)
            view.updateProgressState(true);

        repository.searchRepositories(currentQuery, ++currentPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        searchResultsContainer -> {
                            processNewPage(searchResultsContainer.getItems(),
                                    searchResultsContainer.isIncomplete(),
                                    searchResultsContainer.getTotalCount());
                        },
                        throwable -> {
                            currentPage--;
                            view.showError(throwable);
                        }
                );
    }

    private void processNewPage(List<RepoInfo> newPage, boolean isIncomplete, long totalCount){
        /*В случае, если запрос выполняется слишком долго, гитхаб присылает уже найденные
        * результаты с флагом incomplete. В этом случае в следующий раз запрашивается эта же страница,
        * и данные объединяются*/

        List<RepoInfo> newBatch;
        if (incompletePage != null) {
            newBatch = calculateDelta(newPage);
        } else {
            newBatch = newPage;
        }

        if (isIncomplete && newPage.size() < PAGE_SIZE) {
            if (incompletePage != null)
                incompletePage.addAll(newBatch);
            else
                incompletePage = newBatch;

            currentPage--;
        } else {
            incompletePage = null;
        }

        cache.addAll(newBatch);

        hasMore = cache.size() < totalCount;
        if (view != null) {
            if (currentPage == 1)
                view.setData(newBatch, hasMore);
            else
                view.addPage(newBatch, hasMore);

            view.updateProgressState(false);
        }
    }

    private List<RepoInfo> calculateDelta(List<RepoInfo> newList) {
        List<RepoInfo> result = new ArrayList<>();

        if (newList.size() > incompletePage.size()) {
            result.addAll(newList.subList(incompletePage.size(), newList.size()));
        }
        return result;
    }

    @Override
    public void search(String query) {
        if (currentQuery != null && currentQuery.equals(query))
            return;

        if (query.length() <= 2) {
            if (view!=null)
                view.showInstructions();
            return;
        }

        if (view!=null)
            view.hideInstructions();
        Log.e(TAG, "Searching for:" + query);

        cache = new ArrayList<>();
        currentQuery = query;
        hasMore = true;
        currentPage = 0;

        loadMore();
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public List<RepoInfo> getCache() {
        return cache;
    }

    public List<RepoInfo> getIncompletePage() {
        return incompletePage;
    }

    public boolean isHasMore() {
        return hasMore;
    }
}
