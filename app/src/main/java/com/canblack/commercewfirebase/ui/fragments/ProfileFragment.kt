package com.canblack.commercewfirebase.ui.fragments

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast

import com.canblack.commercewfirebase.R
import com.canblack.commercewfirebase.ui.MainActivity
import com.canblack.commercewfirebase.ui.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import de.hdodenhof.circleimageview.CircleImageView

class ProfileFragment(user: FirebaseUser) : Fragment() {

    private lateinit var auth: FirebaseAuth
    var database = FirebaseDatabase.getInstance().getReference("Users")
    private var profileUser = user
    private lateinit var myUrl:String
    private lateinit var imageUri: Uri
    private var checker:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val profileView = inflater.inflate(R.layout.fragment_profile, container, false)
        var edt_name = profileView.findViewById<EditText>(R.id.edt_setting_name)
        var edt_email = profileView.findViewById<EditText>(R.id.edt_setting_email)
        var edt_phone = profileView.findViewById<EditText>(R.id.edt_setting_phone)
        var edt_pass = profileView.findViewById<EditText>(R.id.edt_setting_pass)
        var btn_save = profileView.findViewById<Button>(R.id.btn_save_setting)
        var btn_back = profileView.findViewById<ImageView>(R.id.settings_btn_home_back)
        var img = profileView.findViewById<CircleImageView>(R.id.setting_profile_image)


        img.setOnClickListener {
            checker = "clicked"
            (activity as MainActivity).CropImage()
        }

        btn_back.setOnClickListener {
            val manager = activity!!.supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out
            )
            transaction.replace(R.id.main_frame, HomeFragment(profileUser)).commit()
        }


        btn_save.setOnClickListener {
            if (checker.equals("clicked")){
                if(TextUtils.isEmpty(edt_name.text.toString())){
                    Toast.makeText(
                        activity,
                        "Name is mandatory",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else if(TextUtils.isEmpty(edt_email.text.toString())){
                    Toast.makeText(
                        activity,
                        "Email is mandatory",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else if(TextUtils.isEmpty(edt_pass.text.toString())){
                    Toast.makeText(
                        activity,
                        "Password is mandatory",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else if(TextUtils.isEmpty(edt_phone.text.toString())){
                    Toast.makeText(
                        activity,
                        "Phone is mandatory",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }else if(checker.equals("clicked")){
                (activity as MainActivity).uploadImageProfile(edt_name,edt_email,edt_pass,edt_phone,img)
            }
            else{
                var userref = FirebaseDatabase.getInstance().reference.child("Users")

                val userrHashMap = HashMap<String,Any>()//Obje yerine Any kullanılıyor
                userrHashMap.put("phone",edt_phone.text.toString())
                userrHashMap.put("password",edt_pass.text.toString())
                userrHashMap.put("name",edt_name!!.text.toString())
                userrHashMap.put("email",edt_email!!.text.toString())

                val manager = activity!!.supportFragmentManager
                val transaction = manager.beginTransaction()
                transaction.setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.fade_out
                )
                transaction.replace(R.id.main_frame, LoginFragment()).commit()
                Toast.makeText(activity, "Profile info updated success",
                    Toast.LENGTH_SHORT).show()
            }
        }

        userInfoDisplay(edt_name,edt_email,edt_pass,edt_phone,img)

        return profileView
    }

    private fun userInfoDisplay(edtName: EditText?, edtEmail: EditText?, edtPass: EditText?, edtPhone: EditText?, img: CircleImageView?) {
        var UsersRef = FirebaseDatabase.getInstance().reference.child("Users").child(profileUser.email.toString().replace(".", ","))
        UsersRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    if(p0.child("image").exists()){
                        var image = p0.child("image").getValue().toString()
                        var name = p0.child("name").getValue().toString()
                        var pass = p0.child("pass").getValue().toString()
                        var email = p0.child("email").getValue().toString()
                        var phone = p0.child("phone").getValue().toString()

                        Picasso.get().load(image).into(img)
                        edtName!!.setText(name)
                        edtEmail!!.setText(email)
                        edtPass!!.setText(pass)
                        edtPhone!!.setText(phone)
                    }
                }
            }
        })
    }


}
