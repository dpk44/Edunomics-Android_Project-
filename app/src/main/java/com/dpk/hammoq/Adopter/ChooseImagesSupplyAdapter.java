package com.dpk.hammoq.Adopter;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dpk.hammoq.R;

import java.util.List;


public class ChooseImagesSupplyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ChooseImages> mList;
    private List<Uri> chooseImagesURI;
    private Context mContext;
    private ImageView im_url;
    private CardView im_delete_image;

    public class ItemViewHolder extends RecyclerView.ViewHolder {


        public ItemViewHolder(View view) {
            super(view);

            im_url = itemView.findViewById(R.id.im_url);
            im_delete_image = itemView.findViewById(R.id.im_delete_image);

        }
    }

    public ChooseImagesSupplyAdapter(Context mContext, List<ChooseImages> mList,List<Uri> chooseImagesURI) {
        this.mList = mList;
        this.mContext = mContext;
        this.chooseImagesURI = chooseImagesURI;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v1 = inflater.inflate(R.layout.choose_image_row, parent, false);
        viewHolder = new ItemViewHolder(v1);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final ItemViewHolder ItemViewHolder = (ItemViewHolder) holder;
        ItemViewHolder.setIsRecyclable(false);

        final ChooseImages mProductModel = mList.get(position);

        Glide.with(mContext).load(mProductModel.getPath()).placeholder(R.drawable.ic_launcher_background).into(im_url);


        im_delete_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mList.size() != 0){

                    mList.remove(position);
//                    chooseImagesURI.remove(position);
                    notifyDataSetChanged();

                    Toast.makeText(mContext, "Remove", Toast.LENGTH_SHORT).show();

                }

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