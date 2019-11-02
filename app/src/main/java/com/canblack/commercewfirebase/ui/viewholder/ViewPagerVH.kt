package com.canblack.commercewfirebase.ui.viewholder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.canblack.commercewfirebase.R

class ViewPagerVH(models:List<ViewPager>,pagercontext: Context) : PagerAdapter(){
    private var models:List<ViewPager> = models
    private var layoutInflaterr:LayoutInflater? = null
    private var pagerContext:Context? = pagercontext

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return models.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflaterr = LayoutInflater.from(pagerContext)
        val view = LayoutInflater.from(pagerContext).inflate(R.layout.item_pager,container,false)

        val pager_img = view.findViewById<ImageView>(R.id.pager_img)
        val pager_txt = view.findViewById<TextView>(R.id.pager_txt)

        pager_img.setImageResource(models[position].img)
        pager_txt.text = models[position].desc
        container.addView(view,0)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }
}

data class ViewPager (val desc:String, val img:Int)
