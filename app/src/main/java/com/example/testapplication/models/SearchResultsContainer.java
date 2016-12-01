package com.example.testapplication.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vlad on 11/30/16.
 */

public class SearchResultsContainer {
    @SerializedName("total_count")
    private long totalCount;

    @SerializedName("incomplete_results")
    private boolean incomplete;

    private List<RepoInfo> items;

    public long getTotalCount() {
        return totalCount;
    }

    public boolean isIncomplete() {
        return incomplete;
    }

    public List<RepoInfo> getItems() {
        return items;
    }
}
