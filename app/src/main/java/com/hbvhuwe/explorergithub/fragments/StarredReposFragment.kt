package com.hbvhuwe.explorergithub.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hbvhuwe.explorergithub.App
import com.hbvhuwe.explorergithub.R
import com.hbvhuwe.explorergithub.adapters.ReposAdapter
import com.hbvhuwe.explorergithub.isOnline
import com.hbvhuwe.explorergithub.models.GitHubRepo
import com.hbvhuwe.explorergithub.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StarredReposFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var reposAdapter: ReposAdapter
    private lateinit var repos: ArrayList<GitHubRepo>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_repos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.repositories_view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            @Suppress("UNCHECKED_CAST")
            repos = savedInstanceState.getSerializable("starredRepos") as ArrayList<GitHubRepo>
            setupRecycler()
        } else {
            if (isOnline()) {
                val call = App.client.getStarredRepos()
                call.enqueue(starredReposCallback)
            } else {
                showToast("Internet not available")
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("starredRepos", repos)
    }

    companion object {
        fun newInstance() = StarredReposFragment()
    }

    private fun setupRecycler() {
//        recyclerView.isNestedScrollingEnabled = false
        val layoutManager = LinearLayoutManager(this.context)
        recyclerView.layoutManager = layoutManager
        reposAdapter = ReposAdapter(repos.toTypedArray())
        recyclerView.adapter = reposAdapter
        recyclerView.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
    }

    private val starredReposCallback = object : Callback<List<GitHubRepo>> {
        override fun onFailure(call: Call<List<GitHubRepo>>?, t: Throwable?) {
            showToast("Network error: " + t?.message)
        }

        override fun onResponse(call: Call<List<GitHubRepo>>?, response: Response<List<GitHubRepo>>?) {
            if (response != null) {
                if (response.isSuccessful) {
                    val reposResponse = response.body()!!
                    println(reposResponse.toString())
                    repos = ArrayList()
                    repos.addAll(reposResponse)
                    setupRecycler()
                }
            }
        }

    }
}
