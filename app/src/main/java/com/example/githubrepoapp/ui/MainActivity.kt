package com.example.githubrepoapp.ui

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubrepoapp.adapters.RepoListAdapter
import com.example.githubrepoapp.databinding.ActivityMainBinding
import com.example.githubrepoapp.model.RepoData
import com.example.githubrepoapp.utils.ItemClickListener
import com.example.toastify.Toastify
import dagger.hilt.android.AndroidEntryPoint

/*
   Author: Anum Khan
   Start Time: 6/20/2021 12:43 PM
   End Time: 6/20/2021 4:52 PM
 */

@AndroidEntryPoint
class MainActivity : AppCompatActivity() , ItemClickListener {


    lateinit var adapter : RepoListAdapter
    private val viewModel : MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initAdapter()
        observeUIState()
        observeList()
    }


    private fun initAdapter() {
        adapter = RepoListAdapter(this, this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = adapter
    }

    private fun observeUIState() {
        val progressDialog : ProgressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading....")

        viewModel.uiStateLiveData.observe(this, Observer {state ->
            when(state){
                UIState.LoadingState -> {
                    progressDialog.show()
                }

                UIState.DataState -> {
                    Toastify.showToast(this, Toastify.SUCCESS, "Data Loaded", Toast.LENGTH_SHORT)
                    progressDialog.hide()
                }

                UIState.ErrorState -> {
                    Toastify.showToast(this, Toastify.FAILED, "No Data Found", Toast.LENGTH_SHORT)
                    progressDialog.hide()
                }
            }

        })
    }

    private fun observeList(){
        viewModel.repoLiveData.observe(this, Observer { data ->
            adapter.submitList(data)
        })
    }

    override fun onItemClicked(data: RepoData) {
        val intent = Intent(this, CommitDetailActivity::class.java)
        intent.putExtra("name", data.name)
        startActivity(intent)
    }

}