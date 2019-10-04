package com.canblack.commercewfirebase.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView

import com.canblack.commercewfirebase.R
import com.canblack.commercewfirebase.ui.MainActivity

class ForgotFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val forgotView = inflater.inflate(R.layout.fragment_forgot, container, false)
        val btn_log = forgotView.findViewById<ImageView>(R.id.btn_log)
        val btn_send = forgotView.findViewById<Button>(R.id.btn_forgot)
        val edt_mail = forgotView.findViewById<EditText>(R.id.edt_forgot_email)

        btn_send.setOnClickListener {
            (activity as MainActivity).Forgot(edt_mail.text.toString())
        }

        btn_log.setOnClickListener {
            val manager = activity!!.supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out
            )
            transaction.replace(R.id.main_frame, LoginFragment()).commit()
        }

        return forgotView

    }

}
