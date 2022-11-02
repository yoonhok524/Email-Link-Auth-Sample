package com.aaron.emaillinkauthsample.signin

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat.startActivity
import dev.aaron.aak.compose.VerticalSpacer

@Composable
fun SignInScreen(
    vm: SignInViewModel
) {
    val uiState = vm.uiState

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        if (uiState is SignInUiState.Loading) {
            CircularProgressIndicator()
        }

        if (uiState is SignInUiState.Success) {
            SuccessPopup {
                vm.dismissPopup()
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val enabled = uiState is SignInUiState.ReadyForSignIn
            var email by remember { mutableStateOf("") }
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                },
                enabled = enabled,
                label = {
                    Text(text = "email")
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Email, contentDescription = "")
                }
            )

            Spacer(modifier = Modifier.size(32.dp))

            Button(
                onClick = {
                    vm.signIn(email)
                },
                enabled = enabled,
            ) {
                Text(text = "Sign In")
            }
        }
    }
}

@Composable
private fun SuccessPopup(
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = {
            onDismiss()
        }
    ) {
        Surface(
            modifier = Modifier.clip(RoundedCornerShape(16.dp))
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, start = 24.dp, end = 24.dp, bottom = 24.dp)
            ) {
                Text(
                    text = "Email Authentication",
                    style = MaterialTheme.typography.h6
                )

                VerticalSpacer(size = 16.dp)

                Text(
                    text = "A verification mail has been sent to abc. Please, open the email you received to complete the verification."
                )

                VerticalSpacer(size = 24.dp)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    val context = LocalContext.current
                    Button(
                        onClick = {
                            startActivity(context, intentForEmailApp, null)
                        }
                    ) {
                        Text("Open Email App")
                    }

                }
            }
        }
    }
}

private val intentForEmailApp = Intent(Intent.ACTION_MAIN)
    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    .addCategory(Intent.CATEGORY_APP_EMAIL)