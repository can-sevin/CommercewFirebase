package com.canblack.commercewfirebase.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseUser
import android.app.AlertDialog
import android.content.DialogInterface
import android.widget.Toast
import com.canblack.commercewfirebase.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_cart.*


class CartFragment : Fragment() {
    lateinit var options:Array<CharSequence>
    var cartUser: FirebaseUser? = null
    var re_cart : RecyclerView? = null
    lateinit var btn_next : Button
    data class Cart(val name:String,val price:Int,val pid:String,val quantity:Int,val discount:String)
    var totalPrice:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewCart = inflater.inflate(R.layout.fragment_cart, container, false)

        val txt_total_price = viewCart.findViewById<TextView>(R.id.txt_total_price)
        btn_next = viewCart.findViewById(R.id.btn_cart_next)
        re_cart = viewCart.findViewById(R.id.re_cartlist)


        txt_total_price.setText("Total Price:"+totalPrice)

        btn_next.setOnClickListener {
            val manager = activity!!.supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out
            )
            transaction.replace(R.id.main_frame,ConfirmFragment(totalPrice)).commit()
        }
        return viewCart
    }


    override fun onStart() {
        super.onStart()
        CheckOrderState()

        val cartListRef = FirebaseDatabase.getInstance().reference.child("Cart List")

        val optionss = FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View")
                .child(cartUser!!.email!!.replace(",",".")).child("Products"), Cart::class.java).build()

        val adapter:FirebaseRecyclerAdapter<Cart,CartVH> = object : FirebaseRecyclerAdapter<Cart,CartVH>(optionss){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartVH {
                val view = LayoutInflater.from(parent.context).inflate(com.canblack.commercewfirebase.R.layout.item_cart,parent,false)
                val holder = CartVH(view)
                return holder
            }

            override fun onBindViewHolder(p0: CartVH, p1: Int, p2: Cart) {

                p0.txt_quantity.setText("Quantity:"+p2.quantity)
                p0.txt_name.setText(p2.name)
                p0.txt_price.setText("Price:"+p2.price)

                val oneProductPrice = p2.price * p2.quantity
                totalPrice += oneProductPrice


                p0.itemView.setOnClickListener {
                    options = arrayOf<CharSequence>("Edit", "Remove")
                }
                val alertDialog = AlertDialog.Builder(activity)
                alertDialog.setTitle("Cart Options:")

                alertDialog.setItems(options,object : DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, i: Int) {
                        if(i == 0){
                            Toast.makeText(
                                activity,
                                "Click is working",
                                Toast.LENGTH_SHORT
                            ).show()
                            TODO("Pid gönderiliyor ProductDetail'da pid ile almayı hallet")
                        }
                        if(i == 1){
                            cartListRef.child("User View").child(cartUser!!.email!!.replace(".",","))
                                .child("Products").child(p2.pid).removeValue()
                                .addOnCompleteListener(object : OnCompleteListener<Void>{
                                    override fun onComplete(p0: Task<Void>) {
                                        Toast.makeText(
                                            activity, "Item removed", Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                        }
                    }
                })

            }
        }
        re_cart!!.adapter = adapter
        adapter.startListening()
    }

    fun CheckOrderState(){
        val ordersRef:DatabaseReference = FirebaseDatabase.getInstance().reference.child("Orders")
            .child(cartUser!!.email!!.replace(".",","))

            ordersRef.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if(p0.exists()){
                        val shippingState = p0.child("state").value.toString()
                        val userName = p0.child("name").value.toString()

                        if(shippingState == "shipped"){
                            txt_total_price.text = "Dear" + userName + "\n order is shipped successfully"
                            re_cart!!.visibility = View.GONE
                            btn_next.visibility = View.GONE
                        }

                        else if(shippingState == "not shipped"){
                            txt_total_price.text = "Shipping State = Not Shipped"
                            re_cart!!.visibility = View.GONE
                            btn_next.visibility = View.GONE

                        }
                    }
                }
            })
    }
}
