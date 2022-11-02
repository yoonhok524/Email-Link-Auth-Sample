package com.aaron.emaillinkauthsample.signin

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(): ViewModel() {

    fun signIn(email: String) {
        Log.d(TAG, "[sample] signIn - email: $email")
    }

    companion object {
        private const val TAG = "SignInViewModel"
    }

}