package com.canblack.commercewfirebase.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.canblack.commercewfirebase.R
import com.canblack.commercewfirebase.ui.viewholder.ProductVH
import com.canblack.commercewfirebase.ui.viewholder.SliderAdapter
import com.canblack.commercewfirebase.ui.model.Products
import com.canblack.commercewfirebase.ui.model.User
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class HomeFragment(user: FirebaseUser) : Fragment() {

    private lateinit var auth: FirebaseAuth
    var productRef:DatabaseReference = FirebaseDatabase.getInstance().reference.child("Products")
    var database = FirebaseDatabase.getInstance().getReference("Users")
    private var homeUser = user
    lateinit var model: Products

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewHome = inflater.inflate(R.layout.fragment_home, container, false)
        val re_phone = viewHome.findViewById<RecyclerView>(R.id.recycler_phone)
        val sliderView = viewHome.findViewById<SliderView>(R.id.imageSlider)
        val btn_basket = viewHome.findViewById<FloatingActionButton>(R.id.btn_home_add_basket)
        val re_new = viewHome.findViewById<RecyclerView>(R.id.recycler_new)
        val img = viewHome.findViewById<CircleImageView>(R.id.profile_image)
        val btn_logout = viewHome.findViewById<ImageView>(R.id.btn_home_back)
        val txt_welcome = viewHome.findViewById<TextView>(R.id.txt_welcome)
        val btn_fav = viewHome.findViewById<FloatingActionButton>(R.id.btn_home_wish)
        val btn_msg = viewHome.findViewById<FloatingActionButton>(R.id.btn_home_msg)

        re_new.setHasFixedSize(true)
        re_new.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        re_phone.setHasFixedSize(true)
        re_phone.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)


        sliderView.sliderAdapter = SliderAdapter(context)
        sliderView.scrollTimeInSec = 4
        sliderView.startAutoCycle()
        sliderView.setIndicatorAnimation(IndicatorAnimations.SLIDE)
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)


        val options = FirebaseRecyclerOptions.Builder<Products>()
            .setQuery(productRef, Products::class.java)
            .setLifecycleOwner(this)
            .build()

        val phone = FirebaseRecyclerOptions.Builder<Products>()
            .setQuery(productRef.orderByChild("cat").equalTo("Telefon"),
                Products::class.java)
            .setLifecycleOwner(this)
            .build()

        btn_msg.setOnClickListener {

        }

        btn_fav.setOnClickListener {
            val manager = activity!!.supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out
            )
            transaction.replace(R.id.main_frame, WishFragment(homeUser),"homeback").commit()
        }

        btn_basket.setOnClickListener {
            val manager = activity!!.supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out
            )
            transaction.replace(R.id.main_frame, CartFragment(homeUser),"homeback").commit()
        }

        img.setOnClickListener {
            val manager = activity!!.supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out
            )
            transaction.replace(R.id.main_frame, ProfileFragment(homeUser),"homeback").commit()
        }

        val recyclerPhoneAdapter = object : FirebaseRecyclerAdapter<Products, ProductVH>(phone){

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductVH {
                val phoneRecyclerView =
                    LayoutInflater.from(activity).inflate(R.layout.item_phone, parent, false)
                return ProductVH(phoneRecyclerView)
            }
            override fun onBindViewHolder(p0: ProductVH, p1: Int, p2: Products) {
                    val placeid = getRef(p1).key.toString()
                    productRef.child(placeid).addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(p4: DataSnapshot) {
                            p0.txtPhoneNameSec.text = p2.name
                            p0.txtPhoneDescSec.text = p2.price.toString() + "TL"
                            Picasso.get().load(p2.image).into(p0.PhoneSecimageView)

                            p0.itemView.setOnClickListener {
                                val manager = activity!!.supportFragmentManager
                                val transaction = manager.beginTransaction()
                                transaction.setCustomAnimations(
                                    R.anim.fade_in,
                                    R.anim.fade_out
                                )
                                transaction.replace(
                                    R.id.main_frame,
                                    ProductFragment(
                                        p2.name,
                                        p2.price,
                                        p2.image,
                                        p2.desc,
                                        p2.cat,
                                        p2.pid,
                                        homeUser
                                    ),"homeback"
                                ).commit()
                            }
                        }

                        override fun onCancelled(p0: DatabaseError) {
                            Toast.makeText(
                                activity,
                                "Error Occurred " + p0.toException(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })

            }
        }

        val recyclerAdapter = object : FirebaseRecyclerAdapter<Products, ProductVH>(options) {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): ProductVH {
                val homeRecyclerView =
                    LayoutInflater.from(activity).inflate(R.layout.item_new, parent, false)
                return ProductVH(homeRecyclerView)
            }

            override fun onBindViewHolder(
                p0: ProductVH,
                p1: Int,
                p2: Products
            ) {
                val placeid = getRef(p1).key.toString()
                productRef.child(placeid).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(p4: DataSnapshot) {
                        p0.txtProductName.text = p2.name
                        p0.txtProductDesc.text = p2.price.toString() + "TL"
                        Picasso.get().load(p2.image).into(p0.imageView)

                        p0.itemView.setOnClickListener {
                            val manager = activity!!.supportFragmentManager
                            val transaction = manager.beginTransaction()
                            transaction.setCustomAnimations(
                                R.anim.fade_in,
                                R.anim.fade_out
                            )
                            transaction.replace(R.id.main_frame, ProductFragment(p2.name,p2.price,p2.image,p2.desc,p2.cat,p2.pid,homeUser),"homeback").commit()
                        }
                    }

                    override fun onCancelled(p0: DatabaseError) {
                        Toast.makeText(
                            activity,
                            "Error Occurred " + p0.toException(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })

            }
        }
        re_new.adapter = recyclerAdapter
        recyclerAdapter.startListening()

        re_phone.adapter = recyclerPhoneAdapter
        recyclerPhoneAdapter.startListening()

        database.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot) {
                val idemail = homeUser.email!!.replace(".", ",")
                if(p0.child(idemail).exists()){
                    val currentOnlineUser = p0.child(idemail).getValue(User::class.java)
                    txt_welcome.text = "Welcome " + currentOnlineUser!!.name
                    }
                }
            })

        btn_logout.setOnClickListener {
            val builder = AlertDialog.Builder(this.context!!)
            builder.setTitle("Logout?")
            builder.setMessage("Are you want to logout?")
            builder.setPositiveButton("YES"){dialog, which ->
                auth.signOut()
                val manager = activity!!.supportFragmentManager
                val transaction = manager.beginTransaction()
                transaction.setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.fade_out
                )
                transaction.replace(R.id.main_frame, LoginFragment(),"Login").commit()
            }
            builder.setNegativeButton("No"){dialog,which ->
                dialog.cancel()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
        return viewHome
    }

}
