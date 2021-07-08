package com.example.githubrepoapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubrepoapp.data.remote.NetworkCallListener
import com.example.githubrepoapp.data.repository.RepoDataRepository
import com.example.githubrepoapp.model.RepoData
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(var repoDataRepository: RepoDataRepository) : ViewModel() {


    private var _uiState = MutableLiveData<UIState>()
    var uiStateLiveData: LiveData<UIState> = _uiState

    private var _repoData = MutableLiveData<List<RepoData>>()
    var repoLiveData: LiveData<List<RepoData>> = _repoData

    init {
        getRepoData()
    }

    private fun getRepoData(){
        _uiState.value = UIState.LoadingState
        repoDataRepository.getRepoData(object : NetworkCallListener{
            override fun onSuccess(response: Response<*>?) {
                _uiState.value = UIState.DataState
                val data = response?.body() as List<RepoData>
                _repoData.postValue(data)
            }

            override fun onFailure() {
                _uiState.value = UIState.ErrorState
            }
        })
    }
}