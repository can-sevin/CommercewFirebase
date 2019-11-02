package com.canblack.commercewfirebase.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.canblack.commercewfirebase.R
import com.canblack.commercewfirebase.ui.fragments.SplashFragment
import com.canblack.commercewfirebase.ui.fragments.StartFragment

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        if(savedInstanceState == null){
                val manager = supportFragmentManager
                val transaction = manager.beginTransaction()
                transaction.setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.fade_out
                )
                transaction.add(R.id.start_frame,
                    SplashFragment()
                ).commit()
            Handler().postDelayed({
                val mainIntent = Intent(this,MainActivity::class.java)
                startActivity(mainIntent)},3000)
        }else{
            val mainIntent = Intent(this,MainActivity::class.java)
            startActivity(mainIntent)
        }
    }
}
