package com.example.testapplication.http;

import com.example.testapplication.models.SearchResultsContainer;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by vlad on 11/30/16.
 */

public interface GitHubApi {
    @GET("/search/repositories")
    Observable<SearchResultsContainer> searchRepositories(@Query("q") String searchString, @Query("page") int page);
}
