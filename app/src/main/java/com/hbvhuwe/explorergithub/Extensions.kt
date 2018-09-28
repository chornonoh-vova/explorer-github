package com.hbvhuwe.explorergithub

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.isOnline(): Boolean {
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val netInfo = cm.activeNetworkInfo
    return netInfo != null && netInfo.isConnected
}

fun Fragment.isOnline(): Boolean {
    val cm = this.activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val netInfo = cm.activeNetworkInfo
    return netInfo != null && netInfo.isConnected
}

fun Fragment.showToast(text: String) {
    Toast.makeText(this.activity, text, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.showToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}