package com.hbvhuwe.explorergithub.ui

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.hbvhuwe.explorergithub.App
import com.hbvhuwe.explorergithub.Const
import com.hbvhuwe.explorergithub.R
import com.hbvhuwe.explorergithub.model.File
import com.hbvhuwe.explorergithub.showToast
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FileActivity : BaseActivity() {
    private val fileUrl by lazy {
        intent.getStringExtra(Const.FILE_URL_KEY)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file)

        println(fileUrl)
    }
}
