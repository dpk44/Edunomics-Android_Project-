package com.dpk.hammoq.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.dpk.hammoq.Activities.RegisterActivity.RegisterActivity;
import com.dpk.hammoq.Fragments.PaymentDetailsFragment;
import com.dpk.hammoq.Fragments.UserLoginEditFragment;
import com.dpk.hammoq.GsonModule.PaymentDetailsModule.Result;
import com.dpk.hammoq.GsonModule.SettingLoginClientPassModule.Password;
import com.dpk.hammoq.R;
import com.dpk.hammoq.Utils.Appcontants;

import java.util.List;

public class SettingLoginClientPassSupplyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Password> mList;
    private Context mContext;

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvSite, tvUsername, tvPassword;
        Button btnEdit;

        public ItemViewHolder(View view) {
            super(view);
            tvSite = itemView.findViewById(R.id.tvSite);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvPassword = itemView.findViewById(R.id.tvPassword);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }

    public SettingLoginClientPassSupplyAdapter(Context mContext, List<Password> mList) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v1 = inflater.inflate(R.layout.client_password_layout, parent, false);
        viewHolder = new ItemViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final ItemViewHolder ItemViewHolder = (ItemViewHolder) holder;
        ItemViewHolder.setIsRecyclable(false);

        final Password mProductModel = mList.get(position);
        ItemViewHolder.tvSite.setText(mProductModel.getWebsite());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Appcontants.WEB_TYPE, String.valueOf(mProductModel.getWebsite()));
        editor.apply();

        ItemViewHolder.tvUsername.setText(mProductModel.getUsername());
        ItemViewHolder.tvPassword.setText(mProductModel.getPassword());

        ItemViewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemViewHolder.itemView.setEnabled(false);

                Bundle bundle = new Bundle();
                bundle.putString("website", mProductModel.getWebsite());
                bundle.putString("username", mProductModel.getUsername());
                bundle.putString("password", mProductModel.getPassword());

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment myFragment = new UserLoginEditFragment();
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, myFragment).addToBackStack(null).commit();
            }
        });

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