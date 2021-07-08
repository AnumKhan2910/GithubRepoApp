package com.example.githubrepoapp.data.remote

import com.example.githubrepoapp.model.CommitData
import com.example.githubrepoapp.model.RepoData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WebServices {

    @GET("/users/mralexgray/repos")
    fun fetchRepos() : Call<List<RepoData>>

    @GET("/repos/mralexgray/{repoName}/commits")
    fun fetchCommits(@Path("repoName") name : String) : Call<List<CommitData>>
}