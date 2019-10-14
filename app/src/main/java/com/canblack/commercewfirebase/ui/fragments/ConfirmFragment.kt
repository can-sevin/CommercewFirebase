package com.canblack.commercewfirebase.ui.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import com.canblack.commercewfirebase.R
import com.canblack.commercewfirebase.ui.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class ConfirmFragment(total:Int) : Fragment() {

    lateinit var edt_name: EditText
    lateinit var edt_phone: EditText
    lateinit var edt_address: EditText
    lateinit var edt_city: EditText
    lateinit var btn_confirm: Button
    var total = total
    lateinit var userConfirm : FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val confirmView = inflater.inflate(R.layout.fragment_confirm, container, false)
        edt_name = confirmView.findViewById(R.id.edt_confirm_name)
        edt_phone = confirmView.findViewById(R.id.edt_confirm_number)
        edt_address = confirmView.findViewById(R.id.edt_confirm_address)
        edt_city = confirmView.findViewById(R.id.edt_confirm_city)
        btn_confirm = confirmView.findViewById(R.id.btn_confrimm)


        btn_confirm.setOnClickListener {

            if(TextUtils.isEmpty(edt_name.text.toString()))
            {
                Toast.makeText(
                    activity,
                    "Fill name area",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else if(TextUtils.isEmpty(edt_city.text.toString()))
            {
                Toast.makeText(
                    activity,
                    "Fill city area",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else if(TextUtils.isEmpty(edt_address.text.toString()))
            {
                Toast.makeText(
                    activity,
                    "Fill address area",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else if(TextUtils.isEmpty(edt_phone.text.toString()))
            {
                Toast.makeText(
                    activity,
                    "Fill phone area",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                ConfirmOrder()
            }
        }

        return confirmView
    }

    private fun ConfirmOrder() {
        val calForDate = Calendar.getInstance()
        val currentDate = SimpleDateFormat("dd/MM/yyyy").format(calForDate.time)
        val currentTime = SimpleDateFormat("HH:mm:ss").format(calForDate.time)


        val ordersRef = FirebaseDatabase.getInstance().reference.child("Orders")
        val ordersMap = HashMap<String,Any>()
        ordersMap.put("Phone",edt_phone.text.toString())
        ordersMap.put("Address",edt_address.text.toString())
        ordersMap.put("Name",edt_name.text.toString())
        ordersMap.put("City",edt_city.text.toString())
        ordersMap.put("TotalPrice",total)
        ordersMap.put("Date",currentDate)
        ordersMap.put("Time",currentTime)
        ordersMap.put("State","Not Shipped")

        ordersRef.updateChildren(ordersMap).addOnCompleteListener(object : OnCompleteListener<Void> {
            override fun onComplete(p0: Task<Void>) {
                    if(p0.isSuccessful){
                        FirebaseDatabase.getInstance().reference.child("Card List")
                            .child("User View")
                            .child(userConfirm.email!!.replace(".",",0"))
                            .removeValue()
                            .addOnCompleteListener(object : OnCompleteListener<Void>{
                                override fun onComplete(p0: Task<Void>) {
                                    if(p0.isSuccessful){
                                        Toast.makeText(
                                            activity,
                                            "Final order successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        val manager = activity!!.supportFragmentManager

                                        /*
                                            İntentlere addFlag eklenerek geri gidilmesi engelleniyor!
                                            Fragmentdada bu yöntemi Bul!
                                        */

                                        val transaction = manager.beginTransaction()
                                        transaction.setCustomAnimations(
                                            R.anim.fade_in,
                                            R.anim.fade_out
                                        )
                                        transaction.replace(R.id.main_frame,HomeFragment(userConfirm)).commit()
                                    }
                                }
                            })
                    }
            }
        })
    }
}