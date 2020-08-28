package com.example.singlevendorapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.singlevendorapp.R
import com.example.singlevendorapp.models.IntroPageItem

class IntroViewPagerAdapter(val context: Context, val mList: List<IntroPageItem>) :
    PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layoutScreen: View = inflater.inflate(R.layout.intro_page, null)
        val img: ImageView = layoutScreen.findViewById(R.id.intro_img)
        val title: TextView = layoutScreen.findViewById(R.id.intro_title)
        val description: TextView = layoutScreen.findViewById(R.id.intro_description)
        Glide.with(context).load(mList[position].getImageResource()).into(img)
        title.text = mList[position].getTitle()
        description.text = mList[position].getDescription()
        container.addView(layoutScreen)
        return layoutScreen
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return mList.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}