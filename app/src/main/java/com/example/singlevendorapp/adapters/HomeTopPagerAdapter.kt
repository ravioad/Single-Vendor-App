package com.example.singlevendorapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.singlevendorapp.R
import com.example.singlevendorapp.activities.ProductActivity
import com.example.singlevendorapp.models.IntroPageItem
import com.example.singlevendorapp.models.ProductModel

class HomeTopPagerAdapter(val context: Context, val mList: ArrayList<ProductModel>) :
    PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layoutScreen: View = inflater.inflate(R.layout.home_auto_pager_top, null)
        val img: ImageView = layoutScreen.findViewById(R.id.home_top_viewpager_image)
        img.setOnClickListener {
            val intent = Intent(context, ProductActivity::class.java).apply {
                putExtra("product", mList[position])
            }
            context.startActivity(intent)
        }
        Glide.with(context).load(mList[position].image).fitCenter().into(img)
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
        unbindDrawable(`object`)
    }

    private fun unbindDrawable(view: View){
        if(view.background != null){
            view.background.callback = null
        }
        if(view is ViewGroup){
            for (i in 0 until (view).childCount){
                unbindDrawable(view.getChildAt(i))
            }
            view.removeAllViews()
        }
    }
}