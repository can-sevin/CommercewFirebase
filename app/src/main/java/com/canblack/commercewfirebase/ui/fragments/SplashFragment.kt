package com.canblack.commercewfirebase.ui.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView

import com.canblack.commercewfirebase.R
class SplashFragment : Fragment() {
    /*val manager = fragmentManager
    val transaction = manager!!.beginTransaction()*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val splashView= inflater.inflate(R.layout.fragment_splash, container, false)
        val img = splashView.findViewById<ImageView>(R.id.img_logo)
        val anim = AnimationUtils.loadAnimation(context,R.anim.bottom_anim)
        img.startAnimation(anim)
        return splashView

        /*Handler().postDelayed({
            transaction.replace(R.id.start_frame,StartFragment()).commit()
        }, 3000)*/
    }
}
