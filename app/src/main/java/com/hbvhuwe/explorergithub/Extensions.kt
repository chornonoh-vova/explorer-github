package com.hbvhuwe.explorergithub

import android.support.v4.app.Fragment
import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast

fun Fragment.isOnline(): Boolean {
    val cm = this.activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val netInfo = cm.activeNetworkInfo
    return netInfo != null && netInfo.isConnectedOrConnecting
}

fun Fragment.showToast(text: String) {
    Toast.makeText(this.activity, text, Toast.LENGTH_SHORT).show()
}