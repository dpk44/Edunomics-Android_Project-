package com.dpk.hammoq.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dpk.hammoq.GsonModule.PaymentDetailsModule.PaymentDetailsPkg;
import com.dpk.hammoq.GsonModule.PaymentDetailsModule.Result;
import com.dpk.hammoq.R;

import java.util.List;

public class PaymentDetailsSupplyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Result> mList;
    private Context mContext;

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvToken, tvDate, tvAmount;

        public ItemViewHolder(View view) {
            super(view);
            tvToken = itemView.findViewById(R.id.tvToken);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvAmount = itemView.findViewById(R.id.tvAmount);
        }
    }

    public PaymentDetailsSupplyAdapter(Context mContext, List<Result> mList) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v1 = inflater.inflate(R.layout.payment_transaction_layout, parent, false);
        viewHolder = new ItemViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final ItemViewHolder ItemViewHolder = (ItemViewHolder) holder;
        ItemViewHolder.setIsRecyclable(false);

        final Result mProductModel = mList.get(position);
        ItemViewHolder.tvToken.setText(mProductModel.getDate());
        ItemViewHolder.tvDate.setText(mProductModel.getReceiptUrl());
        ItemViewHolder.tvAmount.setText("$" + String.valueOf(mProductModel.getAmount()));

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return mList.size();
    }


}