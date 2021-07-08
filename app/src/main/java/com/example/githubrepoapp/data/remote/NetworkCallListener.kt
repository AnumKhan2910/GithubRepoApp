package com.example.githubrepoapp.data.remote

import retrofit2.Response

interface NetworkCallListener {
    fun onSuccess(response: Response<*>?)
    fun onFailure()
}