package com.aaron.emaillinkauthsample

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.aaron.emaillinkauthsample.nav.Home
import com.aaron.emaillinkauthsample.ui.theme.EmailLinkAuthSampleTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val firebaseAuth: FirebaseAuth by lazy { Firebase.auth }

    private val emailLinkResultFlow = callbackFlow {
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this@MainActivity) { pendingDynamicLinkData: PendingDynamicLinkData? ->
                if (pendingDynamicLinkData != null) {
                    val emailLink = intent?.data?.toString()
                    if (emailLink != null && firebaseAuth.isSignInWithEmailLink(emailLink)) {
                        Log.d(TAG, "[sample] isSignInWithEmailLink - $emailLink")
                        intent = null
                        trySend(emailLink)
                    }
                } else {
                    Log.d(TAG, "[sample] setupDynamicLink.getDynamicLink - pendingDynamicLinkData is null")
                }
            }
            .addOnFailureListener(this@MainActivity) { t ->
                Log.w(TAG, "[sample] setupDynamicLink.getDynamicLink - failed: ${t.message}", t)
            }

        awaitClose {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Log.d(TAG, "[sample] emailLinkResultFlow - setContent")
            val navController = rememberNavController()

            lifecycleScope.launchWhenCreated {
                emailLinkResultFlow.collectLatest {
                    Log.d(TAG, "[sample] emailLinkResultFlow - emailLink: $it")
                    handleEmailLink(navController, it)
                }
            }

            EmailLinkAuthSampleTheme {
                MainScreen(navController)
            }
        }
    }

    @Composable
    private fun MainScreen(navController: NavHostController) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            SampleNavHost(navController)
        }
    }

    private fun handleEmailLink(navController: NavHostController, emailLink: String) {
        navController.currentBackStackEntry?.savedStateHandle?.apply {
            set("emailLink", emailLink)
        }
        navController.navigate(Home.route)
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
