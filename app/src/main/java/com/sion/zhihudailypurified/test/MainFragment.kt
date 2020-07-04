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

class MainFragment : Fragment() {

    lateinit var mView: View
    lateinit var mBanner: Banner
    lateinit var mBtn: Button
    lateinit var mViewPager2: ViewPager2

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        container?.setOnClickListener {
//            Log.d("MainFragment", "单击事件container")
//        }
//        mView = inflater.inflate(R.layout.fragment_main, container, false)
////        mView.setOnClickListener {
////            Log.d("MainFragment", "单击事件mView")
////        }
//        mBanner = mView.findViewById<Banner>(R.id.banner)
//        mBanner.isLoadFinish(false)
////        mBanner.setOnClickListener {
////            Log.d("MainFragment", "单击事件mBanner")
////        }
//        mBtn = mView.findViewById(R.id.btn)
////        mBtn.setOnClickListener { mBanner.setCurrentPosition(2) }
//        mViewPager2 = mBanner.findViewById(R.id.vp2)
////        mViewPager2.setOnClickListener {
////            Log.d("MainFragment", "单击事件mViewPager2")
////        }
//        return mView
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity as MainActivity).vm.obtainTopStories()
    }
}