package com.sion.zhihudailypurified.test

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.sion.zhihudailypurified.R
import com.sion.zhihudailypurified.components.banner.Banner
import com.sion.zhihudailypurified.view.adapter.TopBannerAdapter

class MainFragment : Fragment() {

    lateinit var mView: View
    lateinit var mBanner: Banner
//    lateinit var mBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_main, container, false)
//        mView.setOnClickListener {
//            Log.d("MainFragment", "单击事件mView")
//        }
        mBanner = mView.findViewById<Banner>(R.id.banner)
        mBanner.setAdapter(
            TopBannerAdapter(
                (activity as MainActivity).vm.topStories.value!!,
                activity!!
            )
        )
        mBanner.setCurrentPosition(0)
//        mBanner.setOnClickListener {
//            Log.d("MainFragment", "单击事件mBanner")
//        }
//        mBtn = mView.findViewById(R.id.btn)
//        mBtn.setOnClickListener { mBanner.setCurrentPosition(2) }
        return mView
    }

}