package com.tvcat.homepage;

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
import com.tvcat.beans.HomeBean;
import com.tvcat.util.IClickBack;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private Context context;
    private List<HomeBean> homeBeans;
    private final LayoutInflater layoutInflater;

    private IClickBack<HomeBean> clickBack;


    public HomeAdapter(Context context, List<HomeBean> homeBeans) {
        this.context = context;
        this.homeBeans = new ArrayList<>();
        if (homeBeans != null)
            this.homeBeans.addAll(homeBeans);
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_home_page, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        HomeBean homeBean = homeBeans.get(position);
        Glide.with(context).load(homeBean.getIcon()).into(holder.imageView);
        holder.textView.setText(homeBean.getName());

        holder.itemView.setOnClickListener(v -> {
            if (clickBack != null)
                clickBack.callBack(position, homeBean);
        });
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position, @NonNull List<Object> payloads) {
        this.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return homeBeans.size();
    }


    public class HomeViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView;
        private final ImageView imageView;

        public HomeViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_name);
            imageView = itemView.findViewById(R.id.iv);
        }
    }


    public void clearAndRefresh(List<HomeBean> dates) {
        if (dates == null || dates.isEmpty())
            return;
        homeBeans.clear();
        homeBeans.addAll(dates);
        notifyDataSetChanged();
    }

    public void setClickBack(IClickBack<HomeBean> clickBack) {
        this.clickBack = clickBack;
    }
}
