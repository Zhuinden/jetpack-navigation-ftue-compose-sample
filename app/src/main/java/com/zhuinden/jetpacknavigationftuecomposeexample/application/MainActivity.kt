/*
 * Copyright 2020 Gabor Varadi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zhuinden.jetpacknavigationftuecomposeexample.application

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.zhuinden.jetpacknavigationftuecomposeexample.core.navigation.NavigationDispatcher
import com.zhuinden.jetpacknavigationftuecomposeexample.features.login.LoginScreen
import com.zhuinden.jetpacknavigationftuecomposeexample.features.login.LoginViewModel
import com.zhuinden.jetpacknavigationftuecomposeexample.features.profile.ProfileScreen
import com.zhuinden.jetpacknavigationftuecomposeexample.features.profile.ProfileViewModel
import com.zhuinden.jetpacknavigationftuecomposeexample.features.registration.CreateLoginCredentialsScreen
import com.zhuinden.jetpacknavigationftuecomposeexample.features.registration.EnterProfileDataScreen
import com.zhuinden.jetpacknavigationftuecomposeexample.features.registration.RegistrationViewModel
import com.zhuinden.liveevent.observe
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var authenticationManager: AuthenticationManager

    @Inject
    lateinit var navigationDispatcher: NavigationDispatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            AppNavGraph(authenticationManager, navController)

            val lifecycleOwner = LocalLifecycleOwner.current
            val context = LocalContext.current

            DisposableEffect(key1 = Unit, effect = {
                navigationDispatcher.navigationCommands.observe(lifecycleOwner) { command ->
                    navController.command(context)
                }

                onDispose {
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (isFinishing) { // for sample's sake
            authenticationManager.clearRegistration()
        }
    }
}