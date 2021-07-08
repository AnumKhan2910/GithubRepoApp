package com.example.githubrepoapp.ui

sealed class UIState {
    object LoadingState : UIState()
    object DataState : UIState()
    object ErrorState : UIState()
}