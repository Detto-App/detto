package com.dettoapp.detto.clean_architecture.common

import android.annotation.SuppressLint

sealed class Screen(val route: String) {
    @SuppressLint("CustomSplashScreen")
    object SplashScreen: Screen("splash_screen")
    object LoginScreen: Screen("login_screen")
}