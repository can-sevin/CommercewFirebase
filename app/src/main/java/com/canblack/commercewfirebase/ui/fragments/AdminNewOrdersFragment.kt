package com.canblack.commercewfirebase.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.canblack.commercewfirebase.R
import com.canblack.commercewfirebase.ui.ViewHolder.AdminOrderVH
import com.canblack.commercewfirebase.ui.model.AdminOrders
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AdminNewOrdersFragment(user: FirebaseUser) : Fragment() {

    var userAdmin = user
    var ordersList: RecyclerView? = null
    var ordersRef : DatabaseReference = FirebaseDatabase.getInstance().reference

    var userName:TextView? = null
    var userPhoneNumber:TextView? = null
    var userTotalPrice:TextView? = null
    var userDateTime:TextView? = null
    var userAddress:TextView? = null
    var btn_show:Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewNewOrder = inflater.inflate(R.layout.fragment_admin_new_orders, container, false)
        ordersList = viewNewOrder.findViewById(R.id.re_order_cartlist)
        ordersList!!.layoutManager = LinearLayoutManager(context)

        return viewNewOrder
    }


    override fun onStart() {
        super.onStart()
        val options = FirebaseRecyclerOptions.Builder<AdminOrders>()
            .setQuery(ordersRef.child("Orders"),
                AdminOrders::class.java)
            .setLifecycleOwner(this)
            .build()

        val adapter = object : FirebaseRecyclerAdapter<AdminOrders, AdminOrderVH>(options){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminOrderVH {
                val viewNewOrders = LayoutInflater.from(activity)
                    .inflate(R.layout.item_admin_orders,parent,false)
                return AdminOrderVH(viewNewOrders)
            }

            override fun onBindViewHolder(p0: AdminOrderVH, p1: Int, p2: AdminOrders) {
                p0.txt_name.text = "Name:" + p2.Name
                p0.txt_address.text = "Address:"+p2.Address
                p0.txt_date.text = "Time:"+p2.Time
                p0.txt_price.text = "Total:"+p2.TotalPrice

                p0.btn_show.setOnClickListener {
                    val manager = activity!!.supportFragmentManager
                    val transaction = manager.beginTransaction()
                    transaction.setCustomAnimations(
                        R.anim.fade_in,
                        R.anim.fade_out
                    )
                    transaction.replace(R.id.main_frame, AdminUserProductFragment(p2.Date,p2.Time,userAdmin)).commit()
                }
            }
        }
        ordersList!!.adapter = adapter
        adapter.startListening()
    }

/*
    class AdminOrdersVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var userName = itemView.findViewById<TextView>(R.id.txt_admin_orders_product_name)
        var userAddress = itemView.findViewById<TextView>(R.id.txt_admin_orders_product_address)
        var userTotalPrice = itemView.findViewById<TextView>(R.id.txt_admin_orders_total_price)
        var userDateTime = itemView.findViewById<TextView>(R.id.txt_admin_orders_product_date)
        var btn_show = itemView.findViewById<TextView>(R.id.btn_show_other_products)
    }
*/
}
