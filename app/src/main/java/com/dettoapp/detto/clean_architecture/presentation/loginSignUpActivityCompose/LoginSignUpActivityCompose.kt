package com.dettoapp.detto.clean_architecture.presentation.loginSignUpActivityCompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.dettoapp.detto.clean_architecture.presentation.ui.theme.DettoTheme

class LoginSignUpActivityCompose : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DettoTheme {

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