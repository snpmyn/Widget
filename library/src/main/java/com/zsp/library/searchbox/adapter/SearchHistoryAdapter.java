package com.zsp.library.searchbox.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zsp.library.R;
import com.zsp.library.searchbox.listener.OnItemClickOrDeleteClickListener;

import java.util.ArrayList;

/**
 * @decs: 搜索历史记录适配器
 * @author: 郑少鹏
 * @date: 2019/4/23 11:21
 */
public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> histories;
    private OnItemClickOrDeleteClickListener onItemClickOrDeleteClickListener;

    /**
     * constructor
     *
     * @param context   上下文
     * @param histories 历史记录
     */
    public SearchHistoryAdapter(Context context, ArrayList<String> histories) {
        this.context = context.getApplicationContext();
        this.histories = histories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.search_history_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        String history = histories.get(position);
        holder.searchHistoryItemTvName.setText(history);
        holder.searchHistoryItemTvName.setOnClickListener(view -> onItemClickOrDeleteClickListener.onItemClick(history));
        holder.searchHistoryItemIvDelete.setOnClickListener(view -> onItemClickOrDeleteClickListener.onItemDeleteClick(position, history));
    }

    @Override
    public int getItemCount() {
        return histories.size();
    }

    public void setOnItemClickListener(OnItemClickOrDeleteClickListener onItemClickOrDeleteClickListener) {
        this.onItemClickOrDeleteClickListener = onItemClickOrDeleteClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView searchHistoryItemTvName;
        private ImageView searchHistoryItemIvDelete;

        ViewHolder(View view) {
            super(view);
            searchHistoryItemTvName = view.findViewById(R.id.searchHistoryItemTvName);
            searchHistoryItemIvDelete = view.findViewById(R.id.searchHistoryItemIvDelete);
        }
    }
}
