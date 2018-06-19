package com.tvcat.my;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tvcat.R;
import com.tvcat.beans.LookHistoryBean;
import com.tvcat.util.IClickBack;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {


    private final Context context;
    public List<LookHistoryBean> mDates;
    public IClickBack<LookHistoryBean> iClickBack;

    public HistoryAdapter(Context context) {
        mDates = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_look_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {

        LookHistoryBean lookHistoryBean = mDates.get(position);
        holder.itemView.setOnClickListener(v -> {
            if (iClickBack != null) {
                iClickBack.callBack(position, lookHistoryBean);
            }
        });

        if (lookHistoryBean.getTitle() == null || "".equals(lookHistoryBean.getTitle().trim()))
            holder.tvTitle.setVisibility(View.GONE);
        else {
            holder.tvTitle.setVisibility(View.VISIBLE);
            holder.tvTitle.setText(lookHistoryBean.getTitle());
        }
        holder.tvTime.setText("观看时间：" + lookHistoryBean.getTime());
        if (lookHistoryBean.getProvider() == null)
            return;
        String url = lookHistoryBean.getProvider().getIcon();
        Glide.with(context).load(url).into(holder.iv);

    }

    @Override
    public int getItemCount() {
        return mDates.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView tvTime, tvTitle;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvTitle = itemView.findViewById(R.id.tv_name);
        }
    }

    public void clearRefresh(List<LookHistoryBean> data) {
        if (data == null || data.isEmpty())
            return;

        mDates.clear();
        mDates.addAll(data);
        notifyDataSetChanged();

    }

    public void addMore(List<LookHistoryBean> data) {
        if (data == null || data.isEmpty())
            return;

        mDates.addAll(data);
        notifyDataSetChanged();

    }

    public void setiClickBack(IClickBack<LookHistoryBean> iClickBack) {
        this.iClickBack = iClickBack;
    }
}
