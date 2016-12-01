package com.example.testapplication.main_screen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.testapplication.R;
import com.example.testapplication.TestApp;
import com.example.testapplication.main_screen.interfaces.IMainPresenter;
import com.example.testapplication.main_screen.interfaces.IMainView;
import com.example.testapplication.main_screen.list_components.RVAdapter;
import com.example.testapplication.models.RepoInfo;
import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity implements IMainView, RVAdapter.LazyLoadingListener{
    private static final String TAG = MainActivity.class.getSimpleName();

    private RVAdapter adapter;
    private String searchString;
    private View instructionsView;


    @Inject
    IMainPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TestApp.getInstance().getPresenterComponent().inject(this);

        instructionsView = findViewById(R.id.instructionsView);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RVAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        if (searchItem!=null) {
            SearchView searchView = (SearchView) searchItem.getActionView();

            if (searchString!=null) {
                searchView.setQuery(searchString, false);
                searchView.setIconified(false);
            }

            RxSearchView.queryTextChangeEvents(searchView)
                    .debounce(1, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            event -> presenter.search(event.queryText().toString()),
                            this::showError);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onStart(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onStop();
    }

    @Override
    public void setData(List<RepoInfo> data, boolean hasMore) {
        adapter.setData(data);
    }

    @Override
    public void addPage(List<RepoInfo> page, boolean hasMore) {
        adapter.addPage(page);
    }

    @Override
    public void updateProgressState(boolean show) {
        adapter.updateProgressState(show);
    }

    @Override
    public void restoreQuery(String query) {
        searchString = query;
    }

    @Override
    public void showError(Throwable t) {
        Log.e(TAG, "Error occurred:",t);
        Toast.makeText(this,t.getMessage(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void showInstructions() {
        instructionsView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideInstructions() {
        instructionsView.setVisibility(View.GONE);
    }

    @Override
    public void loadMore() {
        presenter.loadMore();
    }

}
