package com.canblack.commercewfirebase.ui.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.canblack.commercewfirebase.R
import com.canblack.commercewfirebase.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth

class AdminFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val adminView = inflater.inflate(R.layout.fragment_admin, container, false)
        val btn_log = adminView.findViewById<Button>(R.id.btn_admin_log)
        val btn_back = adminView.findViewById<ImageView>(R.id.btn_admin_back)
        val edt_email = adminView.findViewById<EditText>(R.id.edt_admin_email)
        val edt_pass = adminView.findViewById<EditText>(R.id.edt_admin_pass)
        val edt_name = adminView.findViewById<EditText>(R.id.edt_admin_name)
        val edt_phone = adminView.findViewById<EditText>(R.id.edt_admin_phone)
        val txt_forgot = adminView.findViewById<TextView>(R.id.txt_admin_forgot)

        txt_forgot.setOnClickListener {
            val manager = activity!!.supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out
            )
            transaction.replace(R.id.main_frame, ForgotFragment()).commit()
        }

        btn_back.setOnClickListener {
            val manager = activity!!.supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out
            )
            transaction.replace(R.id.main_frame, LoginFragment()).commit()
        }

        btn_log.setOnClickListener {
            if(edt_email!= null && edt_pass.length() > 5)
                (activity as MainActivity).AdminRegister(edt_email.text.toString(),
                    edt_pass.text.toString(),edt_name.text.toString(),edt_phone.text.toString())
            else{
                Toast.makeText(context, "Please fill the blank lines",
                    Toast.LENGTH_SHORT).show()
            }
        }
        return adminView
    }
}
