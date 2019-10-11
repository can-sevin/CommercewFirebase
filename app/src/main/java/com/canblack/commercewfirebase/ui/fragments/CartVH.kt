package com.canblack.commercewfirebase.ui.fragments

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.canblack.commercewfirebase.R
import com.canblack.commercewfirebase.ui.ItemClickListener

class CartVH(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener{

    lateinit var itemClick:ItemClickListener
    val img_product = itemView.findViewById<ImageView>(R.id.img_cart_product)
    val txt_name = itemView.findViewById<TextView>(R.id.txt_cart_product_name)
    val txt_price = itemView.findViewById<TextView>(R.id.txt_cart_product_price)
    val txt_quantity = itemView.findViewById<TextView>(R.id.txt_cart_product_quantity)

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClick = itemClickListener
    }

    override fun onClick(v: View) {

        itemClick.onClick(v,adapterPosition,false)

    }
}