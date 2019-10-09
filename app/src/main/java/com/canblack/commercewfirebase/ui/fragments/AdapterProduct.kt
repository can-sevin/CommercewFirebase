package com.canblack.commercewfirebase.ui.fragments
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.canblack.commercewfirebase.R
import com.canblack.commercewfirebase.ui.ItemClickListener
import com.squareup.picasso.Picasso

class AdapterProduct(val ProductList:MutableList<Product>):RecyclerView.Adapter<AdapterProduct.ModelViewHolder>(){

    data class Product (var name:String,var price:String,var image:String)

    class ModelViewHolder(productview: View) : RecyclerView.ViewHolder(productview) {
        var listner: ItemClickListener? = null
        val pproductView = productview
        val productName : TextView = productview.findViewById(R.id.txt_new_name)
        val productPrice : TextView = productview.findViewById(R.id.txt_new_price)
        val productImage : ImageView = productview.findViewById(R.id.img_new_product)

        fun bindItems(item:Product){
            productName.setText(item.name)
            productPrice.setText(item.price)
        }

        fun setItemClickListener(listner:ItemClickListener){
            this.listner = listner
            listner.onClick(pproductView,adapterPosition,false)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_new,parent,false)
        return ModelViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ProductList.size
    }

    override fun onBindViewHolder(holder: ModelViewHolder, position: Int) {
        holder.bindItems(ProductList.get(position))
    }
}