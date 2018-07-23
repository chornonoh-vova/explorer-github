package com.hbvhuwe.explorergithub.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.hbvhuwe.explorergithub.App
import com.hbvhuwe.explorergithub.R
import com.hbvhuwe.explorergithub.model.GitHubFile
import com.hbvhuwe.explorergithub.showToast
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FileActivity : AppCompatActivity() {
    private lateinit var fileToShow: GitHubFile
    private val fileName by lazy {
        findViewById<TextView>(R.id.activity_file_name)
    }
    private val fileContent by lazy {
        findViewById<TextView>(R.id.activity_file_content)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file)

        setSupportActionBar(findViewById(R.id.toolbar_file))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState != null) {
            fileToShow = savedInstanceState.getSerializable("fileToShow") as GitHubFile
            fileName.text = fileToShow.name
            fileContent.text = savedInstanceState.getCharSequence("fileContent")
            findViewById<ProgressBar>(R.id.loading_panel_activity_file).visibility = View.GONE
        } else {
            fileToShow = intent.getSerializableExtra("fileToShow") as GitHubFile
            fileName.text = fileToShow.name
            val call = App.api.getFile(fileToShow.downloadUrl.toString())
            call.enqueue(fileCallback)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putSerializable("fileToShow", fileToShow)
        outState?.putCharSequence("fileContent", fileContent.text)
    }

    private val fileCallback = object : Callback<ResponseBody> {
        override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
            if (t != null) {
                showToast(t.message!!)
            }
        }

        override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
            if (response != null) {
                if (response.isSuccessful) {
                    fileContent.text = response.body()?.string()
                    findViewById<ProgressBar>(R.id.loading_panel_activity_file).visibility = View.GONE
                }
            }
        }

    }
}
