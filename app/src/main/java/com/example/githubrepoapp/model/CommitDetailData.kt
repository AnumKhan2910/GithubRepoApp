package com.example.githubrepoapp.model
import com.google.gson.annotations.SerializedName


class CommitDetailData {

    @SerializedName("author")
    var authorData : AuthorData = AuthorData()
}