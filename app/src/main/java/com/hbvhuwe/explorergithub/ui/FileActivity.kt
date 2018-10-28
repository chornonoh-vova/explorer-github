package com.hbvhuwe.explorergithub.ui

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hbvhuwe.explorergithub.App
import com.hbvhuwe.explorergithub.Const
import com.hbvhuwe.explorergithub.R
import com.hbvhuwe.explorergithub.viewmodel.FileViewModel

class FileActivity : BaseActivity() {
    private lateinit var fileViewModel: FileViewModel
    private val fileContent by lazy {
        findViewById<TextView>(R.id.activity_file_content)
    }
    private val loading by lazy {
        findViewById<ProgressBar>(R.id.loading_panel_activity_file)
    }
    private val fileUrl by lazy {
        intent.getStringExtra(Const.FILE_URL_KEY)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file)

        setSupportActionBar(findViewById(R.id.toolbar_file))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = fileUrl.substring(fileUrl.lastIndexOf('/') + 1)

        fileViewModel = ViewModelProviders.of(this).get(FileViewModel::class.java)
        App.netComponent?.inject(fileViewModel)
        fileViewModel.init(fileUrl)

        fileViewModel.getFile().observe(this, Observer {
            fileContent.setTypeface(Typeface.MONOSPACE)
            fileContent.text = it
            loading.visibility = View.GONE
        })
    }
}
