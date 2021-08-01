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
package com.zhuinden.jetpacknavigationftuecomposeexample.features.login

import androidx.lifecycle.*
import com.zhuinden.eventemitter.EventEmitter
import com.zhuinden.eventemitter.EventSource

import com.zhuinden.jetpacknavigationftuecomposeexample.R
import com.zhuinden.jetpacknavigationftuecomposeexample.application.AuthenticationManager
import com.zhuinden.jetpacknavigationftuecomposeexample.application.Routes
import com.zhuinden.jetpacknavigationftuecomposeexample.core.navigation.NavigationDispatcher
import com.zhuinden.livedatavalidatebykt.validateBy
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authenticationManager: AuthenticationManager,
    private val navigationDispatcher: NavigationDispatcher,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val errorEmitter: EventEmitter<String> = EventEmitter()
    val errorEvents: EventSource<String> get() = errorEmitter

    val username: MutableLiveData<String> = savedStateHandle.getLiveData("username", "")
    val password: MutableLiveData<String> = savedStateHandle.getLiveData("password", "")

    val isLoginEnabled: LiveData<Boolean> = validateBy(
        username.map { it.isNotBlank() },
        password.map { it.isNotBlank() }
    )

    fun onLoginClicked() {
        if (isLoginEnabled.value!!) {
            val username = username.value!!

            authenticationManager.saveRegistration(username)

            navigationDispatcher.emit {
                navigate("${Routes.NAVIGATION_LOGGED_IN}/${username}") {
                    popUpTo(Routes.NAVIGATION_LOGGED_OUT) {
                        inclusive = true
                    }
                }
            }
        } else {
            errorEmitter.emit("Invalid username or password!")
        }
    }

    fun onRegisterClicked() {
        navigationDispatcher.emit {
            navigate(Routes.NAVIGATION_REGISTRATION)
        }
    }
}