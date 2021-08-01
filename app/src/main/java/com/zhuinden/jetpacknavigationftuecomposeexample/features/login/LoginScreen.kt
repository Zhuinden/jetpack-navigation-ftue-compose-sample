package com.zhuinden.jetpacknavigationftuecomposeexample.features.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(loginViewModel: LoginViewModel) {
    val username = loginViewModel.username.observeAsState("")
    val password = loginViewModel.password.observeAsState("")

    val isLoginEnabled = loginViewModel.isLoginEnabled.observeAsState(initial = false)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = username.value,
            placeholder = { Text("Username") },
            onValueChange = { value: String ->
                loginViewModel.username.value = value
            })

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password.value,
            placeholder = { Text("Password") },
            onValueChange = { value: String ->
                loginViewModel.password.value = value
            })

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { loginViewModel.onLoginClicked() }, enabled = isLoginEnabled.value) {
            Text(text = "LOGIN")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = { loginViewModel.onRegisterClicked() }) {
            Text(text = "REGISTER")
        }
    }
}