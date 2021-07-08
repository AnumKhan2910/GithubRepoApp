package com.example.githubrepoapp.ui

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.githubrepoapp.R
import com.example.githubrepoapp.databinding.ActivityCommitDetailBinding
import com.example.githubrepoapp.utils.BarChart
import com.example.toastify.Toastify
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat


@AndroidEntryPoint
class CommitDetailActivity : AppCompatActivity() {


    private lateinit var binding: ActivityCommitDetailBinding
    private val viewModel : CommitDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommitDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        observeUIState()
        observeData()
        fetchData()
    }

    private fun fetchData() {
        intent.getStringExtra("name")?.let { viewModel.getCommitData(it) }
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

    @SuppressLint("SimpleDateFormat")
    private fun observeData(){
        viewModel.commitLiveData.observe(this, Observer { data ->

            val sdf = SimpleDateFormat("yyyy-MM")

            val map : LinkedHashMap<String, Int> = LinkedHashMap()
            for (d in data){
                if (!map.contains(d.commitData.authorData.date.substring(0,7))){
                    map[d.commitData.authorData.date.substring(0, 7)] = 1
                } else {
                    map[d.commitData.authorData.date.substring(0, 7)] =
                        map.getValue(d.commitData.authorData.date.substring(0,7)) + 1
                }
            }

            val listKeys: List<String> = ArrayList<String>(map.keys)
            val listValues: List<Int> = ArrayList<Int>(map.values)
            var count = 0

            val handler = Handler()
            val runnable: Runnable = object : Runnable {
                override fun run() {
                    try {
                        val key = listKeys[count]
                        val value = listValues[count]
                        showBarChart(
                            DateFormatSymbols().months[sdf.parse(key).month],
                            value,
                            data.size
                        )
                        count += 1
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        if (count != listKeys.size - 1) {
                            handler.postDelayed(this, 1500)
                        }
                    }
                }
            }
            handler.post(runnable)
        })
    }



    private fun showBarChart(month : String, value : Int, maxValue: Int) {
        val barChart = findViewById<BarChart>(R.id.barchart)
        barChart.setBarMaxValue(maxValue)
        barChart.addBar(month, value,
            if (value == 1) "commit" else "commits")
    }
}