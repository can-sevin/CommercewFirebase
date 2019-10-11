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
import com.canblack.commercewfirebase.ui.User
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class ProductFragment(name:String,price:Double,img:String ,desc:String,cat:String,pid:String,quantity:Int,user:FirebaseUser) : Fragment() {
    val pname = name
    val pprice = price
    val pimg = img
    val productUser = user
    val pdesc = desc
    val pcat = cat
    val ppid = pid
    var pquantity = quantity
    val cartListRef = FirebaseDatabase.getInstance().reference.child("Card List")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewProduct = inflater.inflate(R.layout.fragment_product, container, false)
        val txt_name = viewProduct.findViewById<TextView>(R.id.txt_product_name)
        txt_name.setText(pname)
        val txt_desc = viewProduct.findViewById<TextView>(R.id.txt_product_desc)
        txt_desc.setText(pdesc)
        val txt_price = viewProduct.findViewById<TextView>(R.id.txt_product_price)
        txt_price.setText(pprice.toString())
        val img = viewProduct.findViewById<ImageView>(R.id.product_img)

        val btn_like = viewProduct.findViewById<FloatingActionButton>(R.id.btn_like)
        val btn_basket = viewProduct.findViewById<FloatingActionButton>(R.id.btn_add_basket)
        val quantity = viewProduct.findViewById<ElegantNumberButton>(R.id.product_quantity)
        pquantity = quantity.number.toInt()


        btn_basket.setOnClickListener {
            addingToCardList()
        }

        return viewProduct
    }

    private fun addingToCardList() {
        val calForDate = Calendar.getInstance()
        val currentDate = SimpleDateFormat("dd/MM/yyyy").format(calForDate.time)
        val currentTime = SimpleDateFormat("HH:mm:ss").format(calForDate.time)

        val cartMap = HashMap<String,Any>()
        cartMap.put("pname",pname)
        cartMap.put("price",pprice)
        cartMap.put("quantity",pquantity)
        cartMap.put("curDate",currentDate)
        cartMap.put("curTime",currentTime)
        cartMap.put("pid",ppid)
        cartMap.put("discount","")

        val idemail = productUser.email!!.replace(".", ",")
        cartListRef.child("User View").child(idemail)
            .child("Products").child(ppid).updateChildren(cartMap)
            .addOnCompleteListener(object : OnCompleteListener<Void>{
                override fun onComplete(p0: Task<Void>) {
                    if(p0.isSuccessful){
                        cartListRef.child("Admin View").child(idemail)
                            .child("Products").child(ppid).updateChildren(cartMap)
                            .addOnCompleteListener(object : OnCompleteListener<Void>{
                                override fun onComplete(p0: Task<Void>) {
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
                            })
                    }
                }
            })
    }
}
