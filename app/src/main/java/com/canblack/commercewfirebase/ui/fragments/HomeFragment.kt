package com.canblack.commercewfirebase.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.canblack.commercewfirebase.R
import com.canblack.commercewfirebase.ui.AdapterCategory
import com.canblack.commercewfirebase.ui.Category
import com.canblack.commercewfirebase.ui.Products
import com.canblack.commercewfirebase.ui.User
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class HomeFragment(user: FirebaseUser) : Fragment() {

    private lateinit var auth: FirebaseAuth
    var productRef:DatabaseReference = FirebaseDatabase.getInstance().reference.child("Products")
    var database = FirebaseDatabase.getInstance().getReference("Users")
    private var homeUser = user
    lateinit var model:Products

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewHome = inflater.inflate(R.layout.fragment_home, container, false)
        val re_cat = viewHome.findViewById<RecyclerView>(R.id.recycler_cat)
        val re_new = viewHome.findViewById<RecyclerView>(R.id.recycler_new)
        val img = viewHome.findViewById<CircleImageView>(R.id.profile_image)
        val btn_logout = viewHome.findViewById<ImageView>(R.id.btn_home_back)
        val txt_welcome = viewHome.findViewById<TextView>(R.id.txt_welcome)
        re_new.setHasFixedSize(true)
        re_new.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)


        re_cat.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        re_cat.adapter = AdapterCategory(getModels())

        val options = FirebaseRecyclerOptions.Builder<Products>()
            .setQuery(productRef,Products::class.java)
            .setLifecycleOwner(this)
            .build()

        img.setOnClickListener {
            val manager = activity!!.supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out
            )
            transaction.replace(R.id.main_frame, ProfileFragment(homeUser)).commit()
        }


        val recyclerAdapter = object : FirebaseRecyclerAdapter<Products,ProductVH>(options) {

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
                            transaction.replace(R.id.main_frame, ProductFragment(p2.name,p2.price,p2.image,p2.desc,p2.cat,p2.pid,p2.quantity,homeUser)).commit()
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
            auth.signOut()
            val manager = activity!!.supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out
            )
            transaction.replace(R.id.main_frame, LoginFragment()).commit()
        }

        return viewHome
    }

    //Kategoriyi Firebaseten Al!
    fun getModels(): MutableList<Category> {
        val catmodels = mutableListOf(
            Category("Bilgisayar"),
            Category("Kulaklık"),
            Category("Televizyon"),
            Category("Telefon")
        )
        return catmodels
    }

}
