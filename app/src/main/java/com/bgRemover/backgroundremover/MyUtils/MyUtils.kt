package com.bgRemover.backgroundremover.MyUtils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.bgRemover.backgroundremover.R

object MyUtils {

    fun isNetworkAvailable(mContext: Context): Boolean {
        val connectivityManager =
            mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetworkInfo: NetworkInfo? = null
        try {
            activeNetworkInfo = connectivityManager.activeNetworkInfo
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun colorList(mContext: Context ):ArrayList<Int>{
        val mColorlist=ArrayList<Int>()
        mColorlist.add(mContext.resources.getColor(R.color.color14))
        mColorlist.add(mContext.resources.getColor(R.color.color13))
        mColorlist.add(mContext.resources.getColor(R.color.color12))
        mColorlist.add(mContext.resources.getColor(R.color.color10))
        mColorlist.add(mContext.resources.getColor(R.color.color8))
        mColorlist.add(mContext.resources.getColor(R.color.color9))
        mColorlist.add(mContext.resources.getColor(R.color.color11))
        mColorlist.add(mContext.resources.getColor(R.color.color1))
        mColorlist.add(mContext.resources.getColor(R.color.color2))
        mColorlist.add(mContext.resources.getColor(R.color.color3))
        mColorlist.add(mContext.resources.getColor(R.color.color4))
        mColorlist.add(mContext.resources.getColor(R.color.color5))
        mColorlist.add(mContext.resources.getColor(R.color.color6))
        mColorlist.add(mContext.resources.getColor(R.color.color7))

        return mColorlist
    }
}