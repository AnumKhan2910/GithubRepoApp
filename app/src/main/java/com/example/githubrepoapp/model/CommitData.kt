package com.example.githubrepoapp.model

import com.google.gson.annotations.SerializedName

class CommitData {

    @SerializedName("commit")
    var commitData : CommitDetailData = CommitDetailData()
}