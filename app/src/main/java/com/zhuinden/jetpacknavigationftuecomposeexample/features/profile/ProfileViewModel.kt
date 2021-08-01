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
package com.zhuinden.jetpacknavigationftuecomposeexample.features.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

import com.zhuinden.jetpacknavigationftuecomposeexample.R
import com.zhuinden.jetpacknavigationftuecomposeexample.application.AuthenticationManager
import com.zhuinden.jetpacknavigationftuecomposeexample.application.Routes
import com.zhuinden.jetpacknavigationftuecomposeexample.core.navigation.NavigationDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authenticationManager: AuthenticationManager,
    private val navigationDispatcher: NavigationDispatcher
) : ViewModel() {
    val activationCheck: LiveData<Unit> = object : LiveData<Unit>(Unit) {
        override fun onActive() {
            if (!authenticationManager.isAuthenticated()) {
                navigationDispatcher.emit {
                    navigate(Routes.NAVIGATION_LOGGED_OUT) {
                        popUpTo(Routes.NAVIGATION_LOGGED_IN) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }
}