package com.canblack.commercewfirebase.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.canblack.commercewfirebase.R
import com.canblack.commercewfirebase.ui.MainActivity

class LoginFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewLogin = inflater.inflate(R.layout.fragment_login, container, false)
        val edt_email = viewLogin.findViewById<EditText>(R.id.edt_log_email)
        val edt_pass = viewLogin.findViewById<EditText>(R.id.edt_log_pass)
        val btn_register = viewLogin.findViewById<Button>(R.id.btn_register)
        val btn_log = viewLogin.findViewById<Button>(R.id.btn_log)
        val txt_forgot = viewLogin.findViewById<TextView>(R.id.txt_forgot)

        btn_log.setOnClickListener {
            (activity as MainActivity).Login(edt_email.text.toString(),edt_pass.text.toString())
        }

        txt_forgot.setOnClickListener {
            val manager = activity!!.supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out
            )
            transaction.replace(R.id.main_frame, ForgotFragment()).commit()
        }

        btn_register.setOnClickListener {
                val manager = activity!!.supportFragmentManager
                val transaction = manager.beginTransaction()
                transaction.setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.fade_out
                )
                transaction.replace(R.id.main_frame, RegisterFragment()).commit()
            }
        return viewLogin
    }
}
