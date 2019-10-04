package com.canblack.commercewfirebase.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.canblack.commercewfirebase.R

class AdapterCategory (val CategoryList:MutableList<Category>) : RecyclerView.Adapter<AdapterCategory.ModelViewHolder>(){

    class ModelViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val cat_name : TextView = view.findViewById(R.id.txt_cat)

        fun bindItems(item: Category){
            cat_name.setText(item.name)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterCategory.ModelViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.category_item,parent,false)
            return ModelViewHolder(view)
    }

    override fun getItemCount(): Int {
            return  CategoryList.size
    }

    override fun onBindViewHolder(holder: AdapterCategory.ModelViewHolder, position: Int) {
            holder.bindItems(CategoryList.get(position))
    }

}