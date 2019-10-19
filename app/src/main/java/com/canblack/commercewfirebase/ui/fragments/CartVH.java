package com.canblack.commercewfirebase.ui.fragments;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.canblack.commercewfirebase.R;
import com.canblack.commercewfirebase.ui.ItemClickListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CartVH extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txt_name,txt_price,txt_quantity;
    public ItemClickListener listener;
    public Button btn_del;


    public CartVH(@NonNull View itemView) {
        super(itemView);
        txt_name = itemView.findViewById(R.id.txt_cart_product_name);
        txt_price = itemView.findViewById(R.id.txt_cart_product_price);
        txt_quantity = itemView.findViewById(R.id.txt_cart_product_quantity);
        btn_del = itemView.findViewById(R.id.btn_del_cart);
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
