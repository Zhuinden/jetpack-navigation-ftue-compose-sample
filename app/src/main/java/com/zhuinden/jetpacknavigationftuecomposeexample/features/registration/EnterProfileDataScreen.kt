package com.zhuinden.jetpacknavigationftuecomposeexample.features.registration

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
fun EnterProfileDataScreen(registrationViewModel: RegistrationViewModel) {
    val fullName = registrationViewModel.fullName.observeAsState(initial = "")
    val bio = registrationViewModel.bio.observeAsState(initial = "")

    val isEnabled =
        registrationViewModel.isEnterProfileNextEnabled.observeAsState(initial = false)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = fullName.value,
            singleLine = true,
            placeholder = { Text("Full name") },
            onValueChange = { fullName ->
                registrationViewModel.fullName.value = fullName
            })

        Spacer(modifier = Modifier.height(8.dp))

        TextField(value = bio.value,
            singleLine = true,
            placeholder = { Text("Bio") },
            onValueChange = { bio ->
                registrationViewModel.bio.value = bio
            })

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            registrationViewModel.onEnterProfileNextClicked()
        }, enabled = isEnabled.value) {
            Text(text = "Next")
        }
    }
}