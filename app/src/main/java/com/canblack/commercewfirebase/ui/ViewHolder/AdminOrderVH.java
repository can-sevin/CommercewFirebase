package com.canblack.commercewfirebase.ui.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.canblack.commercewfirebase.R;
import com.canblack.commercewfirebase.ui.ItemClickListener;

public class AdminOrderVH extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txt_name,txt_price,txt_address,txt_date,txt_quantity;
    public ItemClickListener listener;
    public Button btn_show;

    public AdminOrderVH(@NonNull View itemView) {
        super(itemView);
                txt_name = itemView.findViewById(R.id.txt_admin_orders_product_name);
                txt_address = itemView.findViewById(R.id.txt_admin_orders_product_address);
                txt_price = itemView.findViewById(R.id.txt_admin_orders_total_price);
                txt_date = itemView.findViewById(R.id.txt_admin_orders_product_date);
                btn_show = itemView.findViewById(R.id.btn_show_other_products);
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
