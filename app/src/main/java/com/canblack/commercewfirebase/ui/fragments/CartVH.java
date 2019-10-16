package com.canblack.commercewfirebase.ui.fragments;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.canblack.commercewfirebase.R;
import com.canblack.commercewfirebase.ui.ItemClickListener;

public class CartVH extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txt_name,txt_price,txt_quantity;
    private ItemClickListener listener;

    public CartVH(@NonNull View itemView) {
        super(itemView);
        txt_name = itemView.findViewById(R.id.txt_cart_product_name);
        txt_price = itemView.findViewById(R.id.txt_cart_product_price);
        txt_quantity = itemView.findViewById(R.id.txt_cart_product_quantity);

    }

    public void setItemClickListener(ItemClickListener listner){
        this.listener = listner;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v,getAdapterPosition(),false);
    }
}
