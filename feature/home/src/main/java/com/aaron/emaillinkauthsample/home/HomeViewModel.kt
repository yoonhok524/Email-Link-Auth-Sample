package com.aaron.emaillinkauthsample.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaron.emaillinkauthsample.data.repo.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepo: AuthRepository
) : ViewModel() {

    private val emailLinkFlow = MutableStateFlow<AuthArgs?>(null)

    init {
        viewModelScope.launch {
            emailLinkFlow
                .filterNotNull()
                .distinctUntilChanged { old, new ->
                    old != new
                }
                .collectLatest { (email, emailLink) ->
                    if (emailLink.isBlank() || email.isBlank()) {
                        Log.i(TAG, "[sample] authenticate - emailLink or email is null or blank")
                        return@collectLatest
                    }

                    firebaseAuth.signInWithEmailLink(email, emailLink)
                        .addOnSuccessListener {
                            Log.d(TAG, "[sample] signInWithEmailLink - success")
                            userEmail = it.user?.email ?: ""
                        }
                        .addOnFailureListener { t ->
                            Log.e(TAG, "[sample] signInWithEmailLink - failed: ${t.message}", t)
                            userEmail = ""
                        }
                }
        }
    }

    private val firebaseAuth: FirebaseAuth by lazy { Firebase.auth }

    var userEmail: String by mutableStateOf(firebaseAuth.currentUser?.email ?: "")
        private set

    fun signIn(emailLink: String?) {
        val email = authRepo.getEmail()
        if (emailLink.isNullOrBlank() || email.isBlank()) {
            Log.i(TAG, "[sample] authenticate - emailLink or email is null or blank")
            return
        }

        viewModelScope.launch {
            emailLinkFlow.emit(AuthArgs(email, emailLink))
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
        userEmail = ""
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }

}

data class AuthArgs(
    val email: String,
    val emailLink: String
)