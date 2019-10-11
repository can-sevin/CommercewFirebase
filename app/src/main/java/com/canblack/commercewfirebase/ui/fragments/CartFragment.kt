package com.canblack.commercewfirebase.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.canblack.commercewfirebase.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class CartFragment : Fragment() {

    var cartUser: FirebaseUser? = null
    var re_cart : RecyclerView? = null
    data class Cart(val name:String,val price:Int,val pid:String,val quantity:Int,val discount:String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewCart = inflater.inflate(R.layout.fragment_cart, container, false)

        val txt_total_price = viewCart.findViewById<TextView>(R.id.txt_total_price)
        val btn_next = viewCart.findViewById<Button>(R.id.btn_cart_next)
        re_cart = viewCart.findViewById<RecyclerView>(R.id.re_cartlist)

        return viewCart

    }

    override fun onStart() {
        super.onStart()
        val cartListRef = FirebaseDatabase.getInstance().reference.child("Cart List")

        val optionss = FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View")
                .child(cartUser!!.email!!.replace(",",".")).child("Products"), Cart::class.java).build()

        val adapter:FirebaseRecyclerAdapter<Cart,CartVH> = object : FirebaseRecyclerAdapter<Cart,CartVH>(optionss){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartVH {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart,parent,false)
                val holder = CartVH(view)
                return holder
            }

            override fun onBindViewHolder(p0: CartVH, p1: Int, p2: Cart) {

                p0.txt_quantity.setText("Quantity:"+p2.quantity)
                p0.txt_name.setText(p2.name)
                p0.txt_price.setText("Price:"+p2.price)
            }
        }
        re_cart!!.adapter = adapter
        adapter.startListening()
    }
}
