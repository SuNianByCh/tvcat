package com.tvcat.my.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tvcat.R;
import com.sunian.baselib.beans.VipChargeListBean;

import java.util.ArrayList;
import java.util.List;

public class VipHistoryAdapter extends RecyclerView.Adapter<VipHistoryAdapter.VipHistoryVH> {
    public List<VipChargeListBean> mDatas;

    public VipHistoryAdapter() {
        mDatas = new ArrayList<VipChargeListBean>();
    }

    @NonNull
    @Override
    public VipHistoryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_charge, parent, false);
        return new VipHistoryVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VipHistoryVH holder, int position) {


        VipChargeListBean vipChargeListBean = mDatas.get(position);

        holder.tvCode.setText("激活码：" + vipChargeListBean.getCode());
        holder.tvTime.setText(vipChargeListBean.getActive_time() + "激活");


    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class VipHistoryVH extends RecyclerView.ViewHolder {
        public TextView tvCode;
        public TextView tvTime;

        public VipHistoryVH(View itemView) {
            super(itemView);
            tvCode = itemView.findViewById(R.id.tv_code);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }

    public void addData(List<VipChargeListBean> list) {

        if (list == null || list.isEmpty())
            return;
        mDatas.clear();
        mDatas.addAll(list);
        notifyDataSetChanged();
    }
}
