package com.dettoapp.detto.clean_architecture.presentation.loginSignUpActivityCompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dettoapp.detto.clean_architecture.common.Screen
import com.dettoapp.detto.clean_architecture.presentation.ui.theme.DettoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginSignUpActivityCompose : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DettoTheme {
                val navController = rememberNavController()
                val scaffoldState = rememberScaffoldState()
                Scaffold(scaffoldState = scaffoldState) {
                    NavHost(
                        navController = navController,
                        startDestination = Screen.SplashScreen.route
                    )
                    {
                        composable(route = Screen.SplashScreen.route)
                        {
                            SplashScreen(navController = navController)
                        }
                        composable(route = Screen.LoginScreen.route)
                        {
                            LoginScreen(scaffoldState = scaffoldState)
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DettoTheme {

    }
}