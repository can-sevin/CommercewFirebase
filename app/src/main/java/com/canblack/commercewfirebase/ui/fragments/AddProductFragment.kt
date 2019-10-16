package com.canblack.commercewfirebase.ui.fragments

import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.canblack.commercewfirebase.R
import com.canblack.commercewfirebase.ui.AdapterCategory
import com.canblack.commercewfirebase.ui.Category
import com.canblack.commercewfirebase.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.*

class AddProductFragment(user: FirebaseUser,auth:FirebaseAuth) : Fragment() {

    var adminauth = auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val addProductView = inflater.inflate(R.layout.fragment_add_product, container, false)
        val edt_name = addProductView.findViewById<EditText>(R.id.edt_product_name)
        val edt_quantity = addProductView.findViewById<EditText>(R.id.edt_product_quantity)
        val edt_desc = addProductView.findViewById<EditText>(R.id.edt_product_desc)
        val edt_cat = addProductView.findViewById<EditText>(R.id.edt_product_category)
        val edt_price = addProductView.findViewById<EditText>(R.id.edt_product_price)
        val btn_back = addProductView.findViewById<ImageView>(R.id.btn_product_logout)
        val btn_img = addProductView.findViewById<Button>(R.id.btn_add_img)
        val btn_add = addProductView.findViewById<Button>(R.id.btn_add)

        btn_back.setOnClickListener {
            adminauth.signOut()
            val manager = activity!!.supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out
            )
            transaction.replace(R.id.main_frame, LoginFragment()).commit()
        }


        btn_img.setOnClickListener {
            (activity as MainActivity).OpenGallery()
        }

        /*recycler.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        recycler.adapter = AdapterCategory(getModel())
        */

        btn_add.setOnClickListener {
            if(edt_name.length() == 0 && edt_quantity.length() == 0 && edt_desc.length() == 0
                && edt_cat.length() == 0 && edt_price.length() == 0){
                Toast.makeText(context, "Please write blank area!",
                    Toast.LENGTH_SHORT).show()
            }else{
                val name = edt_name.text.toString()
                val quantity = edt_quantity.text.toString().toInt()
                val desc = edt_desc.text.toString()
                val cat = edt_cat.text.toString()
                val price = edt_price.text.toString().toDouble()
                val calendar = Calendar.getInstance()
                var currentDate = "non"
                var currentTime = "non"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    currentDate = SimpleDateFormat("MM dd, yyyy").format(calendar.time)
                    currentTime = SimpleDateFormat("HH:mm:ss a").format(calendar.time)
                } else {
                    Toast.makeText(context, "Your phone isn't Android 7.0 :(",
                        Toast.LENGTH_SHORT).show()
                }
                val productKey = currentDate+" "+ currentTime
                (activity as MainActivity).AddProduct(name,quantity,desc,cat,price,productKey)
            }
        }
        return addProductView
    }
/*
    fun getModel(): MutableList<Category> {

        val model = mutableListOf(
            Category("Bilgisayar",""),
            Category("Telefon",""),
            Category("Kulaklık",""),
            Category("Televizyon","")
        )
        return model
    }*/
}
