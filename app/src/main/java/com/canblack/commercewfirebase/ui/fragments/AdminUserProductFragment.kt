package com.canblack.commercewfirebase.ui.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.canblack.commercewfirebase.R
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AdminUserProductFragment(date:String,time:String,user:FirebaseUser) : Fragment() {

    var adminUser = user
    var productsList:RecyclerView? = null

    var cartListRef:DatabaseReference = FirebaseDatabase.getInstance().
        reference.child("Cart List").child("Admin View").child(adminUser.email!!.replace(".",","))

    var productsRef:DatabaseReference = FirebaseDatabase.getInstance().
        reference.child("Products")


    override fun onStart() {
        super.onStart()
    }


    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_user_product, container, false)


    }
}
