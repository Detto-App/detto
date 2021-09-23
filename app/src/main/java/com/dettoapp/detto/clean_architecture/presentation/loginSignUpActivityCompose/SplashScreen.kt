package com.dettoapp.detto.clean_architecture.presentation.loginSignUpActivityCompose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dettoapp.detto.clean_architecture.common.Constants2
import com.dettoapp.detto.clean_architecture.common.Screen
import com.dettoapp.detto.clean_architecture.presentation.ui.theme.DarkPurple

@Composable
fun SplashScreen(
    viewModel: LoginSignUpActivityComposeViewModel = hiltViewModel(),
    navController: NavController
) {
    val state = viewModel.userRoleState.value
    Column(
        modifier = Modifier
            .background(color = DarkPurple)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Detto",
            color = Color.White,
            style = MaterialTheme.typography.h1,
            modifier = Modifier.clickable {
                navController.navigate(Screen.LoginScreen.route)
            }
        )
    }
    LaunchedEffect(key1 = state)
    {
        when (state) {
            Constants2.USER_TEACHER -> {
                navController.navigate(Screen.LoginScreen.route) {
                    popUpTo(Screen.SplashScreen.route) { inclusive = true }
                }
            }
            Constants2.USER_STUDENT -> {
            }
            else -> {

            }
        }
    }

}