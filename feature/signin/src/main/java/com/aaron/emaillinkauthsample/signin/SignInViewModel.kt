package com.aaron.emaillinkauthsample.signin

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.aaron.emaillinkauthsample.data.repo.AuthRepository
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepo: AuthRepository
) : ViewModel() {

    var uiState by mutableStateOf<SignInUiState>(SignInUiState.ReadyForSignIn)
        private set

    fun signIn(email: String) {
        Log.d(TAG, "[sample] signIn - email: $email")

        uiState = SignInUiState.Loading

        val actionCodeSettings = actionCodeSettings {
            url = "https://emaillinkauthsample.page.link"
            handleCodeInApp = true
            setAndroidPackageName(
                "com.aaron.emaillinkauthsample",
                true,
                null
            )
        }
        Firebase.auth.sendSignInLinkToEmail(email, actionCodeSettings)
            .addOnCompleteListener { task ->
                Log.d(TAG, "[sample] signIn.sendSignInLinkToEmail - success: ${task.isSuccessful}")
                uiState = if (task.isSuccessful) {
                    authRepo.saveEmail(email)
                    SignInUiState.Success
                } else {
                    authRepo.saveEmail("")
                    SignInUiState.ReadyForSignIn
                }
            }
            .addOnFailureListener { t ->
                Log.e(TAG, "[sample] signIn.sendSignInLinkToEmail - failed: ${t.message}", t)
                authRepo.saveEmail("")
                uiState = SignInUiState.ReadyForSignIn
            }
    }

    fun dismissPopup() {
        uiState = SignInUiState.ReadyForSignIn
    }

    companion object {
        private const val TAG = "SignInViewModel"
    }

}