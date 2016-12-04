package com.example.testapplication.main_screen.list_components;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testapplication.R;
import com.example.testapplication.models.RepoInfo;

import java.util.List;

/**
 * Created by vlad on 11/30/16.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ListItemViewHolder> {
    private static final String TAG = RVAdapter.class.getSimpleName();

    private static final int VIEW_TYPE_REPO = 0;
    private static final int VIEW_TYPE_PROGRESS = 1;

    private static final int LAZY_LOADING_OFFSET = 10;

    private boolean progressShown = false;
    private boolean hasMore = false;
    private List<RepoInfo> dataSet;
    private LazyLoadingListener listener;
    private Handler handler;

    public void setData(List<RepoInfo> dataSet, boolean hasMore){
        this.hasMore = hasMore;
        this.dataSet = dataSet;
        notifyDataSetChanged();
        Log.e(TAG, "Setting data");
    }

    public void addPage(List<RepoInfo> dataSet, boolean hasMore){
        this.hasMore = hasMore;
        int oldSize = this.dataSet.size();
        this.dataSet.addAll(dataSet);
        notifyItemRangeInserted(oldSize-1,dataSet.size());
    }

    public void updateProgressState(boolean show){
        if (progressShown==show)
            return;

        progressShown = show;

        int dataSetSize = 0;
        if (dataSet!=null)
            dataSetSize = dataSet.size();

        if (progressShown){
            notifyItemInserted(dataSetSize);
        }else{
            notifyItemRemoved(dataSetSize);
        }
    }

    @Override
    public int getItemCount() {
        int result = 0;
        if (dataSet!=null)
            result=dataSet.size();

        if (progressShown)
            result+=1;

        return result;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType==VIEW_TYPE_PROGRESS) {
            return new ListItemViewHolder(layoutInflater.inflate(R.layout.progress_item,parent,false));
        }else{
            return new ListItemViewHolder(layoutInflater.inflate(R.layout.repo_item,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position) {
        if (holder.getItemViewType()==VIEW_TYPE_REPO){
            ((ListItem)holder.itemView).setData(dataSet.get(position));
        }

        if (hasMore&&!progressShown&&listener!=null&&getItemCount()-position<LAZY_LOADING_OFFSET){
            handler.post(loadMoreNotifyRunnable);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (progressShown&&position==getItemCount()-1)
            return VIEW_TYPE_PROGRESS;

        return VIEW_TYPE_REPO;
    }

    private Runnable loadMoreNotifyRunnable = new Runnable() {
        @Override
        public void run() {
            listener.loadMore();
        }
    };

    public RVAdapter(@NonNull LazyLoadingListener listener) {
        this.listener = listener;
        handler = new Handler();
    }

    public interface LazyLoadingListener{
        void loadMore();
    }




    static class ListItemViewHolder extends RecyclerView.ViewHolder{
        public ListItemViewHolder(View itemView) {
            super(itemView);
        }
    }
}
