package com.example.githubrepoapp.model

import com.google.gson.annotations.SerializedName


class RepoData {

    @SerializedName("id")
    var id : Long = 0L

    @SerializedName("name")
    var name : String = ""

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }
}