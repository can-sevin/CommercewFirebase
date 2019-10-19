package com.canblack.commercewfirebase.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseUser
import androidx.recyclerview.widget.LinearLayoutManager
import com.canblack.commercewfirebase.R
import com.google.firebase.database.*


class CartFragment(user: FirebaseUser) : Fragment() {
    var cartUser: FirebaseUser = user
    var re_cart : RecyclerView? = null
    var re_layout = LinearLayoutManager(context)
    lateinit var btn_next : Button
    var totalPrice:Int = 0
    var txt_msg:TextView? = null
    var txt_total_price:TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewCart = inflater.inflate(R.layout.fragment_cart, container, false)
        txt_total_price = viewCart.findViewById(R.id.txt_total_price)
        txt_msg = viewCart.findViewById(R.id.txt_cart_msg)
        btn_next = viewCart.findViewById(R.id.btn_cart_next)
        re_cart = viewCart.findViewById(R.id.re_cartlist)
        re_cart!!.layoutManager = re_layout
        re_cart!!.setHasFixedSize(true)


        btn_next.setOnClickListener {
            val manager = activity!!.supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out
            )
            transaction.replace(R.id.main_frame,ConfirmFragment(totalPrice,cartUser)).commit()
        }

        val cartListRef = FirebaseDatabase.getInstance().reference.child("Card List")
        val optionss = FirebaseRecyclerOptions.Builder<Cart>()
            .setQuery(cartListRef.child("User View")
                .child(cartUser.email!!.replace(".",",")).child("Products"), Cart::class.java).build()

        val adapter = object : FirebaseRecyclerAdapter<Cart,CartVH>(optionss){
            fun DelItem(pos:Int) {
                snapshots.getSnapshot(pos).ref.removeValue()
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartVH {
                val view = LayoutInflater.from(activity).inflate(R.layout.item_cart,parent,false)
                return CartVH(view)
            }

            override fun onBindViewHolder(p0: CartVH, p1: Int, p2: Cart) {
                CheckOrderState(p2.pid)
                p0.txt_quantity.text = "Quantity:"+p2.quantity
                p0.txt_name.text = p2.pname
                p0.txt_price.text = "Price:"+p2.price

                val oneProductPrice = p2.price *p2.quantity

                totalPrice = totalPrice + oneProductPrice
                txt_total_price!!.text = "Total Price:" + totalPrice

                p0.btn_del.setOnClickListener {
                    FirebaseDatabase.getInstance().reference.child("Card List").child("User View")
                        .child(cartUser.email!!.replace(".",",")).child("Products")
                        .child(p2.pid).removeValue()

                        .addOnCompleteListener {
                            Toast.makeText(activity, "Item Deleted",
                                Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
        re_cart!!.adapter = adapter
        adapter.startListening()
        return viewCart
    }

    fun CheckOrderState(pid:String){
        val ordersRef:DatabaseReference = FirebaseDatabase.getInstance().reference.child("Orders")
            .child(cartUser.email!!.replace(".",",")).child(pid)

            ordersRef.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if(p0.exists()){
                        val shippingState = p0.child("state").value.toString()
                        val userName = p0.child("name").value.toString()

                        if(shippingState == "Shipped"){
                            txt_msg!!.text = "Dear" + userName + "\n order is shipped successfully"
                            btn_next.visibility = View.VISIBLE
                            re_cart!!.visibility = View.GONE
                            txt_total_price!!.visibility = View.GONE
                            btn_next.visibility = View.GONE
                        }

                        else if(shippingState == "Not Shipped"){
                            txt_msg!!.text = "Shipping State = Not Shipped"
                            btn_next.visibility = View.VISIBLE
                            re_cart!!.visibility = View.GONE
                            txt_total_price!!.visibility = View.GONE
                            btn_next.visibility = View.GONE
                        }
                    }
                }
            })
    }

}

