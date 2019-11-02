package com.canblack.commercewfirebase.ui.fragments

import android.animation.ArgbEvaluator
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import com.canblack.commercewfirebase.R
import com.canblack.commercewfirebase.ui.MainActivity
import com.canblack.commercewfirebase.ui.viewholder.ViewPager
import com.canblack.commercewfirebase.ui.viewholder.ViewPagerVH

class StartFragment : Fragment() {

    lateinit var models:List<ViewPager>
    lateinit var adapter:ViewPagerVH
    lateinit var viewPager:androidx.viewpager.widget.ViewPager
    lateinit var colors:IntArray
    lateinit var btnStart:Button
    lateinit var argbEvaluator: ArgbEvaluator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (models as ArrayList<ViewPager>).add(ViewPager("Sayfa 1",R.drawable.pager1))
        (models as ArrayList<ViewPager>).add(ViewPager("Sayfa 2",R.drawable.pager2))
        (models as ArrayList<ViewPager>).add(ViewPager("Sayfa 3",R.drawable.pager3))
        (models as ArrayList<ViewPager>).add(ViewPager("Sayfa 4",R.drawable.pager4))
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewStart = inflater.inflate(R.layout.fragment_start, container, false)
        adapter = ViewPagerVH(models,context!!)
        viewPager = viewStart.findViewById(R.id.viewPager)
        viewPager.adapter = adapter
        viewPager.setPadding(130,0,130,0)
        btnStart = viewStart.findViewById<Button>(R.id.btn_start)

        var colors_temp: () -> Int = {
            resources.getColor(R.color.primaryTextColor)
            resources.getColor(R.color.primaryTextColor)
            resources.getColor(R.color.primaryTextColor)
            resources.getColor(R.color.primaryTextColor)
        }

        btnStart.setOnClickListener {
            val mainIntent = Intent(context, MainActivity::class.java)
            startActivity(mainIntent)
        }

/*
        viewPager.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                if(position < (adapter.count - 1) && position < (colors.size - 1)){
                    viewPager.setBackgroundColor(
                        argbEvaluator.evaluate(
                            positionOffset,colors[position],colors[position + 1]
                        ) as Int
                    )
                } else {
                    viewPager.setBackgroundColor(colors[colors.size - 1])
                }
            }
            override fun onPageSelected(position: Int) {
            }
        })
*/
        return viewStart
    }
}
