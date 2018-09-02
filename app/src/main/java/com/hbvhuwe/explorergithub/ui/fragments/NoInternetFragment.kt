package com.hbvhuwe.explorergithub.ui.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.hbvhuwe.explorergithub.R

class NoInternetFragment : Fragment() {
    private lateinit var retryButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_no_internet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retryButton = view.findViewById(R.id.retry_button)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        retryButton.setOnClickListener {
            (this.activity as IRetryActivity).retry()
        }
    }

    interface IRetryActivity {
        fun retry()
    }
}
