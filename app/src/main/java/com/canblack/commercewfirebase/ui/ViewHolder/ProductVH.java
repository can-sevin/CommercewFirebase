package com.canblack.commercewfirebase.ui.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.canblack.commercewfirebase.R;
import com.canblack.commercewfirebase.ui.ItemClickListener;

public class ProductVH extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName,txtProductDesc,txtPhoneNameSec,txtPhoneDescSec;
    public ImageView imageView,PhoneSecimageView;
    private ItemClickListener listener;

    public ProductVH(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.img_new_product);
        txtProductName = itemView.findViewById(R.id.txt_new_name);
        txtProductDesc = itemView.findViewById(R.id.txt_new_price);
        PhoneSecimageView = itemView.findViewById(R.id.img_phone_product_sec);
        txtPhoneNameSec = itemView.findViewById(R.id.txt_phone_name_sec);
        txtPhoneDescSec = itemView.findViewById(R.id.txt_phone_price_sec);
    }

    public void setItemClickListener(ItemClickListener listner){
        this.listener = listner;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v,getAdapterPosition(),false);
    }
}
