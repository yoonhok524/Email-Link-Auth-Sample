package com.aaron.emaillinkauthsample.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.aaron.aak.compose.VerticalSpacer

@Composable
fun HomeScreen(
    vm: HomeViewModel,
    emailLink: String?,
    onNavigateSignIn: () -> Unit
) {
    vm.signIn(emailLink)
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val userEmail = vm.userEmail
        if (userEmail.isBlank()) {
            Button(onClick = { onNavigateSignIn() }) {
                Text(text = "Sign In")
            }
        } else {
            Text(text = "Welcome")
            VerticalSpacer(size = 16.dp)
            Text(
                text = userEmail,
                style = MaterialTheme.typography.h5
            )

            VerticalSpacer(size = 16.dp)

            Button(onClick = { vm.signOut() }) {
                Text(text = "Sign Out")
            }
        }
    }
}
