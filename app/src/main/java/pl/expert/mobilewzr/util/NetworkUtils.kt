package pl.expert.mobilewzr.util

import android.content.Context
import android.net.ConnectivityManager

abstract class NetworkUtils {

    companion object {

        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

    }
}