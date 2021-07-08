package com.example.githubrepoapp.utils

import com.example.githubrepoapp.model.RepoData

interface ItemClickListener {
    fun onItemClicked(data: RepoData)
}