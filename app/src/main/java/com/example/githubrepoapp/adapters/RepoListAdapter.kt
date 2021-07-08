package com.example.githubrepoapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.githubrepoapp.R
import com.example.githubrepoapp.model.RepoData
import com.example.githubrepoapp.utils.ItemClickListener

class RepoListAdapter(var context: Context, var itemClickListener: ItemClickListener) : ListAdapter<RepoData, RecyclerView.ViewHolder>(DiffCallBack()){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_repo, parent, false)
        return RepoViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RepoViewHolder){
            holder.repoName.text = currentList[position].name
            holder.view.setOnClickListener {
                itemClickListener.onItemClicked(currentList[position])
            }
        }
    }

    class RepoViewHolder(var view : View) : RecyclerView.ViewHolder(view){
        val repoName: TextView = view.findViewById(R.id.name)
    }


    class DiffCallBack : DiffUtil.ItemCallback<RepoData>(){
        override fun areItemsTheSame(oldItem: RepoData, newItem: RepoData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RepoData, newItem: RepoData): Boolean {
            return oldItem == newItem
        }

    }
}