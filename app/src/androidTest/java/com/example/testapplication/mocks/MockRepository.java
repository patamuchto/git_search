package com.example.testapplication.mocks;

import com.example.testapplication.Repository;
import com.example.testapplication.models.RepoInfo;
import com.example.testapplication.models.SearchResultsContainer;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by vlad on 12/2/16.
 */

public class MockRepository extends Repository {
    private static final int PAGE_SIZE = 30;
    private List<RepoInfo> smallerIncomplete = new ArrayList<>();
    private List<RepoInfo> biggerIncomplete = new ArrayList<>();
    private List<RepoInfo> complete = new ArrayList<>();

    private int callNumber = 0;
    private final int smallerSize = 5;
    private final int biggerSize = 20;
    private final int completeSize = 30;

    public MockRepository(){
        for (int i=0;i<smallerSize;i++)
            smallerIncomplete.add(new RepoInfo());

        for (int j=0;j<biggerSize;j++)
            biggerIncomplete.add(new RepoInfo());

        for(int k=0;k<completeSize;k++)
            complete.add(new RepoInfo());
    }

    @Override
    public Observable<SearchResultsContainer> searchRepositories(String searchString, int page) {
        SearchResultsContainer container = null;
        switch (callNumber){
            case 0:
                container = new MockSearchResultContainer(smallerIncomplete,true,30);
                break;
            case 1:
                container = new MockSearchResultContainer(biggerIncomplete,true,30);
                break;
            default:
                container = new MockSearchResultContainer(complete,false,30);
                break;
        }
        callNumber++;
        return Observable.just(container);
    }

    private static class MockSearchResultContainer extends SearchResultsContainer{
        MockSearchResultContainer(List<RepoInfo> items, boolean incomplete, long totalCount){
            this.items = items;
            this.incomplete = incomplete;
            this.totalCount = totalCount;
        }
    }

    public int getSmallerSize() {
        return smallerSize;
    }

    public int getBiggerSize() {
        return biggerSize;
    }

    public int getCompleteSize() {
        return completeSize;
    }
}
