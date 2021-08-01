package com.zhuinden.jetpacknavigationftuecomposeexample.application

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.zhuinden.jetpacknavigationftuecomposeexample.features.login.LoginScreen
import com.zhuinden.jetpacknavigationftuecomposeexample.features.login.LoginViewModel
import com.zhuinden.jetpacknavigationftuecomposeexample.features.profile.ProfileScreen
import com.zhuinden.jetpacknavigationftuecomposeexample.features.profile.ProfileViewModel
import com.zhuinden.jetpacknavigationftuecomposeexample.features.registration.CreateLoginCredentialsScreen
import com.zhuinden.jetpacknavigationftuecomposeexample.features.registration.EnterProfileDataScreen
import com.zhuinden.jetpacknavigationftuecomposeexample.features.registration.RegistrationViewModel

object Routes {
    const val NAVIGATION_SPLASH = "splash"
    const val DESTINATION_SPLASH = "splashscreen"
    const val NAVIGATION_LOGGED_OUT = "logged_out"
    const val DESTINATION_LOGIN = "login"
    const val NAVIGATION_REGISTRATION = "registration"
    const val DESTINATION_ENTER_PROFILE_DATA = "enter_profile_data"
    const val DESTINATION_CREATE_LOGIN_CREDENTIALS = "create_login_credentials"
    const val NAVIGATION_LOGGED_IN = "logged_in"
    const val NAVIGATION_LOGGED_IN_ARG_USERNAME = "username"
    const val DESTINATION_PROFILE = "profile"
}
@Composable
fun AppNavGraph(authenticationManager: AuthenticationManager, navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.NAVIGATION_SPLASH) {
        navigation(route = Routes.NAVIGATION_SPLASH, startDestination = Routes.DESTINATION_SPLASH) {
            composable(Routes.DESTINATION_SPLASH) { // wrap in navigation tag for consistency. Otherwise, navigation-compose crashes with a ClassCastException
                LaunchedEffect(Unit) {
                    if (authenticationManager.isAuthenticated()) {
                        navController.navigate("${Routes.NAVIGATION_LOGGED_IN}/${authenticationManager.getAuthenticatedUser()}") {
                            popUpTo(Routes.NAVIGATION_SPLASH) {
                                inclusive = true
                            }
                        }
                    } else {
                        navController.navigate(Routes.NAVIGATION_LOGGED_OUT) {
                            popUpTo(Routes.NAVIGATION_SPLASH) {
                                inclusive = true
                            }
                        }
                    }
                }
            }
        }
        navigation(startDestination = Routes.DESTINATION_LOGIN, route = Routes.NAVIGATION_LOGGED_OUT) {
            composable(Routes.DESTINATION_LOGIN) { navBackStackEntry ->
                val loginViewModel: LoginViewModel = hiltViewModel(viewModelStoreOwner = navBackStackEntry)

                LoginScreen(loginViewModel)
            }
            navigation(startDestination = Routes.DESTINATION_ENTER_PROFILE_DATA, route = Routes.NAVIGATION_REGISTRATION) {
                composable(Routes.DESTINATION_ENTER_PROFILE_DATA) {
                    val context = LocalContext.current
                    val registrationNavBackStackEntry = remember { navController.getBackStackEntry(Routes.NAVIGATION_REGISTRATION) } // HACK: This no longer exists during animation to profile. We must remember this for it to survive for navigation

                    var viewModel by remember { mutableStateOf<RegistrationViewModel?>(null) } // HACK: Fix You cannot access the NavBackStackEntry's ViewModels until it is added to the NavController's back stack (i.e., the Lifecycle of the NavBackStackEntry reaches the CREATED state). Using hiltViewModel() does NOT work!
                    DisposableEffect(Unit) {
                        viewModel = ViewModelProvider(registrationNavBackStackEntry, HiltViewModelFactory(context, registrationNavBackStackEntry)).get(
                            RegistrationViewModel::class.java)  // ViewModelProvider is needed, don't forget, otherwise you get "SavedStateProvider with the given key is already registered" (and it's totally valid)

                        onDispose { }
                    }

                    val registrationViewModel = viewModel ?: return@composable

                    EnterProfileDataScreen(registrationViewModel)
                }
                composable(Routes.DESTINATION_CREATE_LOGIN_CREDENTIALS) {
                    val context = LocalContext.current
                    val registrationNavBackStackEntry = remember { navController.getBackStackEntry(Routes.NAVIGATION_REGISTRATION) } // HACK: This no longer exists during animation to profile. We must remember this for it to survive for navigation

                    var viewModel by remember { mutableStateOf<RegistrationViewModel?>(null) } // HACK: Fix You cannot access the NavBackStackEntry's ViewModels until it is added to the NavController's back stack (i.e., the Lifecycle of the NavBackStackEntry reaches the CREATED state). Using hiltViewModel() does NOT work!
                    DisposableEffect(Unit) {
                        viewModel = ViewModelProvider(registrationNavBackStackEntry, HiltViewModelFactory(context, registrationNavBackStackEntry)).get(
                            RegistrationViewModel::class.java) // ViewModelProvider is needed, don't forget, otherwise you get "SavedStateProvider with the given key is already registered" (and it's totally valid)

                        onDispose { }
                    }

                    val registrationViewModel = viewModel ?: return@composable

                    CreateLoginCredentialsScreen(registrationViewModel)
                }
            }
            navigation(startDestination = Routes.DESTINATION_PROFILE, route="${Routes.NAVIGATION_LOGGED_IN}/{${Routes.NAVIGATION_LOGGED_IN_ARG_USERNAME}}") {
                composable(route = Routes.DESTINATION_PROFILE) { navBackStackEntry ->
                    val profileViewModel: ProfileViewModel = hiltViewModel(viewModelStoreOwner = navBackStackEntry)

                    val username = navBackStackEntry.arguments!!.getString("username")!!

                    ProfileScreen(profileViewModel, username)
                }
            }
        }
    }
}