package com.example.githubrepoapp.data.repository

import com.example.githubrepoapp.data.remote.NetworkCallListener
import com.example.githubrepoapp.data.remote.WebServices
import com.example.githubrepoapp.model.CommitData
import com.example.githubrepoapp.model.RepoData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class RepoDataRepository @Inject constructor(var webServices: WebServices) {


    fun getRepoData(networkCallListener: NetworkCallListener){
        val call : Call<List<RepoData>> = webServices.fetchRepos()
        call.enqueue(object : Callback<List<RepoData>>{
            override fun onFailure(call: Call<List<RepoData>>, t: Throwable) {
                networkCallListener.onFailure()
            }

            override fun onResponse(call: Call<List<RepoData>>, response: Response<List<RepoData>>) {
                if (response.isSuccessful){
                    networkCallListener.onSuccess(response)
                } else {
                    networkCallListener.onFailure()
                }
            }

        })
    }


    fun getCommitData(networkCallListener: NetworkCallListener, name: String){
        val call : Call<List<CommitData>> = webServices.fetchCommits(name)
        call.enqueue(object : Callback<List<CommitData>>{
            override fun onFailure(call: Call<List<CommitData>>, t: Throwable) {
                networkCallListener.onFailure()
            }

            override fun onResponse(call: Call<List<CommitData>>, response: Response<List<CommitData>>) {
                if (response.isSuccessful){
                    networkCallListener.onSuccess(response)
                } else {
                    networkCallListener.onFailure()
                }
            }

        })
    }
}