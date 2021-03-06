package com.canblack.commercewfirebase.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.canblack.commercewfirebase.R
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class ProductFragment(name:String,price:Double,img:String ,desc:String,cat:String,pid:String,user:FirebaseUser) : Fragment() {
    val pname = name
    val pprice = price
    val pimg = img
    val productUser = user
    val pdesc = desc
    val pcat = cat
    var state = "Normal"
    val ppid = pid
    lateinit var quantity:ElegantNumberButton
    var pquantity:Int = 0
    val cartListRef = FirebaseDatabase.getInstance().reference.child("Card List")
    val wishListRef = FirebaseDatabase.getInstance().reference.child("Wish List")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewProduct = inflater.inflate(R.layout.fragment_product, container, false)
        val txt_name = viewProduct.findViewById<TextView>(R.id.txt_product_name)
        txt_name.text = pname
        val txt_desc = viewProduct.findViewById<TextView>(R.id.txt_product_desc)
        txt_desc.text = pdesc
        val txt_price = viewProduct.findViewById<TextView>(R.id.txt_product_price)
        txt_price.text = pprice.toString()
        val img = viewProduct.findViewById<ImageView>(R.id.product_img)

        val btn_like = viewProduct.findViewById<FloatingActionButton>(R.id.btn_like)
        val btn_basket = viewProduct.findViewById<FloatingActionButton>(R.id.btn_add_basket)
        quantity = viewProduct.findViewById(R.id.product_quantity)
        Picasso.get().load(pimg).into(img)

        btn_like.setOnClickListener {
            addToWishList()
        }

        btn_basket.setOnClickListener {
            addingToCardList()
            if(state == "Order Placed" || state == "Order Shipped")
            {
                Toast.makeText(context, "Your Order is shipped",
                    Toast.LENGTH_SHORT).show()
            } else {
                addingToCardList()
            }
        }
        return viewProduct
    }

    private fun addToWishList() {
        val calForDate = Calendar.getInstance()
        val currentDate = SimpleDateFormat("dd/MM/yyyy").format(calForDate.time)
        val currentTime = SimpleDateFormat("HH:mm:ss").format(calForDate.time)
        pquantity = quantity.number.toInt()

        val wishMap = HashMap<String,Any>()
        wishMap["pname"] = pname
        wishMap["price"] = pprice
        wishMap["quantity"] = pquantity
        wishMap["curDate"] = currentDate
        wishMap["curTime"] = currentTime
        wishMap["pid"] = ppid
        wishMap["pimg"] = pimg
        wishMap["discount"] = ""

        val idemail = productUser.email!!.replace(".", ",")
        wishListRef.child("User View").child(idemail)
            .child("Products").child(ppid).updateChildren(wishMap)
            .addOnCompleteListener { p0 ->
                if(p0.isSuccessful){
                        Toast.makeText(context, "Product Added to WishList",
                                    Toast.LENGTH_SHORT).show()
                        }
                }
            }

    private fun addingToCardList() {
        val calForDate = Calendar.getInstance()
        val currentDate = SimpleDateFormat("dd/MM/yyyy").format(calForDate.time)
        val currentTime = SimpleDateFormat("HH:mm:ss").format(calForDate.time)
        pquantity = quantity.number.toInt()

        val cartMap = HashMap<String,Any>()
        cartMap["pname"] = pname
        cartMap["price"] = pprice
        cartMap["quantity"] = pquantity
        cartMap["curDate"] = currentDate
        cartMap["curTime"] = currentTime
        cartMap["pid"] = ppid
        cartMap["pimg"] = pimg
        cartMap["discount"] = ""

        val idemail = productUser.email!!.replace(".", ",")
        cartListRef.child("User View").child(idemail)
            .child("Products").child(ppid).updateChildren(cartMap)
            .addOnCompleteListener { p0 ->
                if(p0.isSuccessful){
                    cartListRef.child("Admin View").child(idemail)
                        .child("Products").child(ppid).updateChildren(cartMap)
                        .addOnCompleteListener { p0 ->
                            if(p0.isSuccessful){
                                Toast.makeText(context, "Product Added to Basket",
                                    Toast.LENGTH_SHORT).show()
                                val manager = activity!!.supportFragmentManager
                                val transaction = manager.beginTransaction()
                                transaction.setCustomAnimations(
                                    R.anim.fade_in,
                                    R.anim.fade_out
                                )
                                transaction.replace(R.id.main_frame, HomeFragment(productUser)).commit()
                            }
                        }
                }
            }
    }



    override fun onStart() {
        super.onStart()
        CheckOrderState()
    }


    fun CheckOrderState(){
        val ordersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Orders")
            .child(productUser.email!!.replace(".",",")).child(ppid)

        ordersRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    val shippingState = p0.child("state").value.toString()

                    if(shippingState == "shipped"){
                        state = "Order Shipped"
                    }
                    else if(shippingState == "not shipped"){
                        state = "Order Placed"
                    }
                }
            }
        })
    }

}
