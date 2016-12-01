package com.example.testapplication;

import com.example.testapplication.http.GitHubApi;
import com.example.testapplication.models.SearchResultsContainer;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by vlad on 11/30/16.
 */

public class Repository {
    private GitHubApi api;

    public Repository(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .followRedirects(false)
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        api = retrofit.create(GitHubApi.class);
    }

    public Observable<SearchResultsContainer> searchRepositories(String searchString, int page){
        String searchParamsString = "\""+searchString+"\""+"in:name,description";
        return api.searchRepositories(searchParamsString,page);
    }
}
