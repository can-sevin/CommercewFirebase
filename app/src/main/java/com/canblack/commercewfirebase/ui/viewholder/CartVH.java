package com.canblack.commercewfirebase.ui.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.canblack.commercewfirebase.R;
import com.canblack.commercewfirebase.ui.ItemClickListener;

public class CartVH extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txt_name,txt_price,txt_quantity,txt_wish_name,txt_wish_price;
    public ItemClickListener listener;
    public Button btn_del,btn_wish_del,btn_wish_shop;


    public CartVH(@NonNull View itemView) {
        super(itemView);
        txt_name = itemView.findViewById(R.id.txt_cart_product_name);
        txt_price = itemView.findViewById(R.id.txt_cart_product_price);
        txt_quantity = itemView.findViewById(R.id.txt_cart_product_quantity);
        btn_del = itemView.findViewById(R.id.btn_del_cart);
        txt_wish_name = itemView.findViewById(R.id.txt_wish_product_name);
        txt_wish_price = itemView.findViewById(R.id.txt_wish_product_price);
        btn_wish_del = itemView.findViewById(R.id.btn_del_wish);
        btn_wish_shop = itemView.findViewById(R.id.btn_shop_wish);
    }

    public void setItemClickListener(ItemClickListener listner){
        this.listener = listner;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v,getAdapterPosition(),false);
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT){
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };
}
