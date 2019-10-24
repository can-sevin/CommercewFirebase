package com.canblack.commercewfirebase.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.canblack.commercewfirebase.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class WishFragment(user:FirebaseUser) : Fragment() {

    var wishUser: FirebaseUser = user
    var re_wish : RecyclerView? = null
    var re_layout = LinearLayoutManager(context)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewWish = inflater.inflate(R.layout.fragment_wish, container, false)
        re_wish = viewWish.findViewById(R.id.re_cartlist)
        re_wish!!.layoutManager = re_layout
        re_wish!!.setHasFixedSize(true)

        val cartListRef = FirebaseDatabase.getInstance().reference.child("Card List")
        val wishListRef = FirebaseDatabase.getInstance().reference.child("Wish List")

        val optionss = FirebaseRecyclerOptions.Builder<Cart>()
            .setQuery(wishListRef.child("User View")
                .child(wishUser.email!!.replace(".",",")).child("Products"), Cart::class.java).build()

        val adapter = object : FirebaseRecyclerAdapter<Cart, CartVH>(optionss) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartVH {
                val view = LayoutInflater.from(activity).inflate(R.layout.item_wish,parent,false)
                return CartVH(view)
            }

            override fun onBindViewHolder(p0: CartVH, p1: Int, p2: Cart) {
                p0.txt_wish_name.text = p2.pname
                p0.txt_wish_price.text = "Price:"+p2.price

                p0.btn_wish_del.setOnClickListener {
                    FirebaseDatabase.getInstance().reference.child("Wish list").child("User View")
                        .child(wishUser.email!!.replace(".",",")).child("Products")
                        .child(p2.pid).removeValue()
                        .addOnCompleteListener {
                            Toast.makeText(activity, "Item Deleted",
                                Toast.LENGTH_SHORT).show()
                        }
                }

                p0.btn_wish_shop.setOnClickListener {
                    val calForDate = Calendar.getInstance()
                    val currentDate = SimpleDateFormat("dd/MM/yyyy").format(calForDate.time)
                    val currentTime = SimpleDateFormat("HH:mm:ss").format(calForDate.time)

                    val wishMap = HashMap<String,Any>()
                    wishMap["pname"] = p2.pname
                    wishMap["price"] = p2.price
                    wishMap["quantity"] = p2.quantity
                    wishMap["curDate"] = currentDate
                    wishMap["curTime"] = currentTime
                    wishMap["pid"] = p2.pid
                    wishMap["discount"] = ""

                    val idemail = wishUser.email!!.replace(".", ",")
                    cartListRef.child("User View").child(idemail)
                        .child("Products").child(p2.pid).updateChildren(wishMap)
                        .addOnCompleteListener { p0->
                            if(p0.isSuccessful){
                                cartListRef.child("Admin View").child(idemail)
                                    .child("Products").child(p2.pid).updateChildren(wishMap)
                                    .addOnCompleteListener { p0 ->
                                        if(p0.isSuccessful){
                                            Toast.makeText(context, "Product Added to Basket",
                                                Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            }
                        }
                }
            }

        }
            re_wish!!.adapter = adapter
            adapter.startListening()
            return viewWish
    }
}
