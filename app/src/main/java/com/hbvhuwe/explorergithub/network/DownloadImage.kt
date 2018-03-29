package com.hbvhuwe.explorergithub.network

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.widget.ImageView
import java.net.URL

class DownloadImage(private val image: ImageView?) : AsyncTask<String, Void, Bitmap>() {

    override fun doInBackground(vararg params: String?): Bitmap {
        val urlDisplay = params[0]
        val inputStream = URL(urlDisplay).openStream()
        return BitmapFactory.decodeStream(inputStream)
    }

    override fun onPostExecute(result: Bitmap?) {
        image?.layoutParams?.width = (result?.width?.times(0.75))?.toInt()
        image?.layoutParams?.height = (result?.height?.times(0.75))?.toInt()
        image?.setImageBitmap(result)
    }
}