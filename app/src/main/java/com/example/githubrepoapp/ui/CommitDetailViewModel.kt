package com.example.githubrepoapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubrepoapp.data.remote.NetworkCallListener
import com.example.githubrepoapp.data.repository.RepoDataRepository
import com.example.githubrepoapp.model.CommitData
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CommitDetailViewModel @Inject constructor(var repository: RepoDataRepository) : ViewModel(){


    private var _uiState = MutableLiveData<UIState>()
    var uiStateLiveData: LiveData<UIState> = _uiState

    private var _commitData = MutableLiveData<List<CommitData>>()
    var commitLiveData: LiveData<List<CommitData>> = _commitData


    fun getCommitData(name : String){
        _uiState.value = UIState.LoadingState
        repository.getCommitData(object : NetworkCallListener{
            override fun onSuccess(response: Response<*>?) {
                _uiState.value = UIState.DataState
                val data = response?.body() as List<CommitData>
                _commitData.postValue(data)
            }

            override fun onFailure() {
                _uiState.value = UIState.ErrorState
            }
        }, name)
    }

}