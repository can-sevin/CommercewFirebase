package com.canblack.commercewfirebase.ui.fragments;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.canblack.commercewfirebase.R;
import com.canblack.commercewfirebase.ui.ItemClickListener;

public class ProductVH extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName,txtProductDesc;
    public ImageView imageView;
    public ItemClickListener listener;
    public ProductVH(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.img_new_product);
        txtProductName = itemView.findViewById(R.id.txt_new_name);
        txtProductDesc = itemView.findViewById(R.id.txt_new_price);
    }

    public void setItemClickListener(ItemClickListener listner){
        this.listener = listner;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v,getAdapterPosition(),false);
    }
}
