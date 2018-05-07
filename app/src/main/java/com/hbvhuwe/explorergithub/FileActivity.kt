package com.hbvhuwe.explorergithub

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.hbvhuwe.explorergithub.models.GitHubFile
import com.hbvhuwe.explorergithub.network.DownloadFile
import com.hbvhuwe.explorergithub.network.LoadInfo

class FileActivity : AppCompatActivity(), LoadInfo {
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
            findViewById<ProgressBar>(R.id.loading_panel_activity_file)
        } else {
            fileToShow = intent.getSerializableExtra("fileToShow") as GitHubFile
            fileName.text = fileToShow.name
            DownloadFile(this).execute(fileToShow.downloadUrl.toString())
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putSerializable("fileToShow", fileToShow)
        outState?.putCharSequence("fileContent", fileContent.text)
    }

    override fun onLoadInfoCallback(result: String?) {
        fileContent.text = result
        findViewById<ProgressBar>(R.id.loading_panel_activity_file).visibility = View.GONE
    }

    override fun onErrorCallback() {
        showToast("Error while loading file content")
    }

}
