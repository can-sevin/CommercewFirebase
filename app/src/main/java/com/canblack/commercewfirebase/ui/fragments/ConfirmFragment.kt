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
import android.widget.TextView
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

class ConfirmFragment(total:Int,user:FirebaseUser) : Fragment() {

    lateinit var edt_name: EditText
    lateinit var edt_phone: EditText
    lateinit var edt_address: EditText
    lateinit var edt_city: EditText
    lateinit var btn_confirm: Button
    lateinit var txt_total:TextView
    var total = total
    var userConfirm : FirebaseUser = user

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
        txt_total = confirmView.findViewById(R.id.txt_total_price_confirm)

        txt_total.setText("Total Price: "+total.toString())

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
        val currentDate = SimpleDateFormat("MM dd, yyyy").format(calForDate.time)
        val currentTime = SimpleDateFormat("HH:mm:ss a").format(calForDate.time)

        val ordersRef = FirebaseDatabase.getInstance().reference.child("Orders")
            .child(userConfirm.email!!.replace(".",","))
            .child("$currentDate $currentTime")

        val ordersMap = HashMap<String,Any>()
        ordersMap["Phone"] = edt_phone.text.toString()
        ordersMap["Address"] = edt_address.text.toString()
        ordersMap["Name"] = edt_name.text.toString()
        ordersMap["City"] = edt_city.text.toString()
        ordersMap["TotalPrice"] = total.toString()
        ordersMap["Date"] = currentDate
        ordersMap["Time"] = currentTime
        ordersMap["State"] = "Not Shipped"

        ordersRef.updateChildren(ordersMap).addOnCompleteListener { p0 ->
            if(p0.isSuccessful){
                FirebaseDatabase.getInstance().reference.child("Card List")
                    .child("User View")
                    .child(userConfirm.email!!.replace(".",","))
                    .removeValue()
                    .addOnCompleteListener { p0 ->
                        if(p0.isSuccessful){
                            Toast.makeText(
                                activity,
                                "Final order successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            val manager = activity!!.supportFragmentManager
                            val transaction = manager.beginTransaction()
                            transaction.setCustomAnimations(
                                R.anim.fade_in,
                                R.anim.fade_out
                            )
                            transaction.replace(R.id.main_frame,HomeFragment(userConfirm),"Home").commit()
                        }
                    }
            }
        }
    }
}
