package com.canblack.commercewfirebase.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.canblack.commercewfirebase.R
import com.canblack.commercewfirebase.ui.AdapterCategory
import com.canblack.commercewfirebase.ui.Category
import com.canblack.commercewfirebase.ui.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView

class HomeFragment(user: FirebaseUser) : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var productRef: DatabaseReference
    var database = FirebaseDatabase.getInstance().getReference("Users")
    private var homeUser = user

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
        val img = viewHome.findViewById<CircleImageView>(R.id.home_image)
        val btn_logout = viewHome.findViewById<ImageView>(R.id.btn_home_back)
        val txt_welcome = viewHome.findViewById<TextView>(R.id.txt_welcome)

        re_cat.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        re_cat.adapter = AdapterCategory(getModels())


        database.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot) {
                val idemail = homeUser.email!!.replace(".", ",")
                if(p0.child(idemail).exists()){
                    val currentOnlineUser = p0.child(idemail).getValue(User::class.java)
                    txt_welcome.setText("Welcome " + currentOnlineUser!!.name)
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
