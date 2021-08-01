package com.zhuinden.jetpacknavigationftuecomposeexample.features.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    username: String
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Hello $username!")
    }

    DisposableEffect(Unit) {
        profileViewModel.activationCheck.observe(lifecycleOwner) {}

        onDispose {}
    }
}
