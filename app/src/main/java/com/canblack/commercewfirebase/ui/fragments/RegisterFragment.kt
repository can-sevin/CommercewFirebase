package com.canblack.commercewfirebase.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.canblack.commercewfirebase.R
import com.canblack.commercewfirebase.ui.MainActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.wallet.IsReadyToPayRequest
import com.google.android.gms.wallet.PaymentsClient
import com.google.android.gms.wallet.Wallet
import com.google.android.gms.wallet.WalletConstants

class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewRegister = inflater.inflate(R.layout.fragment_register, container, false)
        val edt_email = viewRegister.findViewById<EditText>(R.id.edt_res_email)
        val edt_pass = viewRegister.findViewById<EditText>(R.id.edt_res_pass)
        val edt_name = viewRegister.findViewById<EditText>(R.id.edt_res_name)
        val edt_phone = viewRegister.findViewById<EditText>(R.id.edt_res_phone)
        val btn_register = viewRegister.findViewById<Button>(R.id.btn_register)
        val btn_log = viewRegister.findViewById<ImageView>(R.id.btn_log)
        val txt_forgot = viewRegister.findViewById<TextView>(R.id.txt_forgot)


        btn_log.setOnClickListener {
            val manager = activity!!.supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out
            )
            transaction.replace(R.id.main_frame, LoginFragment()).commit()
        }

        btn_register.setOnClickListener {
            if(edt_email!= null && edt_pass.length() > 5)
                (activity as MainActivity).Register(edt_email.text.toString(),
                    edt_pass.text.toString(),edt_name.text.toString(),edt_phone.text.toString())
            else{
                Toast.makeText(context, "Alanları eksiksiz doldurun!",
                    Toast.LENGTH_SHORT).show()
            }
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

        return viewRegister
    }
}
