package com.dpk.hammoq.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;


import com.dpk.hammoq.Activities.Dashbaord.DashBoardActivity;
import com.dpk.hammoq.Model.User;
import com.dpk.hammoq.R;
import com.dpk.hammoq.Activities.RegisterActivity.RegisterActivity;
import com.dpk.hammoq.SQLiteHelper;

import java.util.List;


public class UserListDataAdapter extends RecyclerView.Adapter<UserListDataAdapter.UserViewHolder> {

    private List<User> mList;

    private Context context;

    SQLiteHelper databaseHelper;

    private String mUser , mName, mEmail, mMobile, mPassword;

    public UserListDataAdapter(List<User> mList, SQLiteHelper databaseHelper) {
        this.mList = mList;
        this.databaseHelper = databaseHelper;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_recycler, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final UserViewHolder holder, final int position) {

        holder.tvName.setText(mList.get(position).getName());
        holder.tvEmail.setText(mList.get(position).getEmail());
        holder.tvPassword.setText(mList.get(position).getPassword());
        holder.tvMobile.setText(mList.get(position).getMobile());

        mUser = String.valueOf(mList.get(position).getId());
        mName = String.valueOf(mList.get(position).getName());
        mEmail = String.valueOf(mList.get(position).getEmail());
        mMobile = String.valueOf(mList.get(position).getMobile());
        mPassword = String.valueOf(mList.get(position).getPassword());

        holder.ivUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                Intent intent = new Intent(v.getContext(), RegisterActivity.class);
                intent.putExtra("user_id", mUser);
                intent.putExtra("name", mName);
                intent.putExtra("email", mEmail);
                intent.putExtra("mobile", mMobile);
                intent.putExtra("password", mPassword);
                intent.putExtra("status", "1");
                v.getContext().startActivity(intent);
            }
        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User user = new User();

                boolean deleteUser = databaseHelper.deleteUser(mUser);

                if (deleteUser = true)
                {
                    databaseHelper.deleteUser(mUser);

                    Toast.makeText(v.getContext(), "User Deleted", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(v.getContext(), DashBoardActivity.class);
                    v.getContext().startActivity(intent);
                    ((Activity)v.getContext()).finish();

                } else {
                    Toast.makeText(v.getContext(), "User Not Deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.v(UserListDataAdapter.class.getSimpleName(), "" + mList.size());
        return mList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView tvName, tvMobile, tvEmail, tvPassword;
        private ImageButton ivUpdate, ivDelete;
        SQLiteHelper sqLiteHelper;

        private UserViewHolder(View view) {
            super(view);
            tvName = (AppCompatTextView) view.findViewById(R.id.tvName);
            tvMobile = (AppCompatTextView) view.findViewById(R.id.tvMobile);
            tvEmail = (AppCompatTextView) view.findViewById(R.id.tvEmail);
            tvPassword = (AppCompatTextView) view.findViewById(R.id.tvPassword);
            ivUpdate = (ImageButton) view.findViewById(R.id.ivUpdate);
            ivDelete = (ImageButton) view.findViewById(R.id.ivDelete);
        }
    }
}
