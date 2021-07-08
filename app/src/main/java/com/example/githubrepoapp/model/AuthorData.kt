package com.example.githubrepoapp.model
import com.google.gson.annotations.SerializedName


class AuthorData {

    @SerializedName("name")
    var name : String = ""

    @SerializedName("date")
    var date : String = ""
}