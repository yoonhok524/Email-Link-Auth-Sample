package com.aaron.emaillinkauthsample.signin

sealed interface SignInUiState {
    object ReadyForSignIn: SignInUiState
    object Loading: SignInUiState
    object Success: SignInUiState
}