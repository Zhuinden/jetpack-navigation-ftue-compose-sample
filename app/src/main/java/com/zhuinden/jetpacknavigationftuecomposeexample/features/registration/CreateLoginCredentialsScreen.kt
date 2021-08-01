package com.zhuinden.jetpacknavigationftuecomposeexample.features.registration

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun CreateLoginCredentialsScreen(registrationViewModel: RegistrationViewModel) {
    val username = registrationViewModel.username.observeAsState(initial = "")
    val password = registrationViewModel.password.observeAsState(initial = "")

    val isEnabled =
        registrationViewModel.isRegisterAndLoginEnabled.observeAsState(initial = false)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = username.value,
            singleLine = true,
            placeholder = { Text("Username") },
            onValueChange = { username ->
                registrationViewModel.username.value = username
            })

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password.value,
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            placeholder = { Text("Password") },
            onValueChange = { password ->
                registrationViewModel.password.value = password
            })

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            registrationViewModel.onRegisterAndLoginClicked()
        }, enabled = isEnabled.value) {
            Text(text = "Register and login")
        }
    }
}
