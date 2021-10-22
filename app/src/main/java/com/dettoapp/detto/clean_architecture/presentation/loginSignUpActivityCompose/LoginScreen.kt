package com.dettoapp.detto.clean_architecture.presentation.loginSignUpActivityCompose


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.dettoapp.detto.R
import com.dettoapp.detto.UtilityClasses.Resource

@Composable
fun LoginScreen(viewModel: LoginSignUpActivityComposeViewModel = hiltViewModel(), scaffoldState: ScaffoldState) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    val userRole = remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    val loginUsersState = remember { viewModel.loginUsersState }

    Column(
        modifier = Modifier
            .fillMaxSize()
    )
    {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
        )
        Text(
            text = "Detto",
            style = MaterialTheme.typography.h3,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.15f)
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, bottom = 16.dp),
            label = { Text(text = "Email") },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_email_24),
                    contentDescription = ""
                )
            }
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, bottom = 16.dp),
            label = { Text(text = "Password") },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_vpn_key_24),
                    contentDescription = ""
                )
            },
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(
                mask = '*'
            ),
            trailingIcon = {
                val image = if (passwordVisibility)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                IconButton(onClick = {
                    passwordVisibility = !passwordVisibility
                }) {
                    Icon(imageVector = image, "")
                }
            }
        )
        CustomDropDownMenu(hint = "Select Role", listOfOptions = arrayListOf("Student", "Teacher"), userRole)
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
        )
        Button(
            modifier = Modifier.align(CenterHorizontally),
            onClick = { viewModel.loginUsers(userRole.value, email, password) }) {
            Text(text = "Login")
        }
        Row(
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(top = 12.dp)
        ) {
            Text(text = "Are u a new user?", modifier = Modifier.padding(end = 4.dp))
            Text(text = "Sign up", color = Color.Blue, style = TextStyle(textDecoration = TextDecoration.Underline))
        }
    }
    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false },
            DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            Box(
                contentAlignment= Center,
                modifier = Modifier
                    .size(100.dp)
                    .background(White, shape = RoundedCornerShape(8.dp))
            ) {
                Column(verticalArrangement = Arrangement.Center) {
                    CircularProgressIndicator(modifier = Modifier.align(CenterHorizontally))
                    Spacer(modifier = Modifier.fillMaxHeight(0.2f))
                    Text(text = "Loading...")
                }
                
            }
        }
    }

    LaunchedEffect(key1 = loginUsersState.value)
    {
        when (loginUsersState.value) {

            is Resource.Error -> {
                showDialog = false
                scaffoldState.snackbarHostState.showSnackbar(loginUsersState.value.message + "")
            }
            is Resource.Loading -> {
                showDialog = true
            }
            else -> {

            }
        }
    }

}


@Preview
@Composable
fun LoginPreview() {
    LoginScreen(scaffoldState = rememberScaffoldState())
}

@Composable
fun CustomDropDownMenu(hint: String, listOfOptions: ArrayList<String>, userRole: MutableState<String>) {
    var expanded by remember { mutableStateOf(false) }
    var dropDownWidth by remember { mutableStateOf(0) }
    val focusRequester = remember { FocusRequester() }

    val icon = if (expanded)
        Icons.Filled.ArrowDropUp
    else
        Icons.Filled.ArrowDropDown


    Column {
        Box {
            OutlinedTextField(
                value = userRole.value,
                onValueChange = { userRole.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp, end = 32.dp)
                    .focusRequester(focusRequester)
                    .onSizeChanged {
                        dropDownWidth = it.width
                    },
                label = { Text(hint) },
                trailingIcon = {
                    Icon(icon, "contentDescription", Modifier.clickable { expanded = !expanded })
                },
                readOnly = true
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clickable {
                        expanded = !expanded
                        focusRequester.requestFocus()
                    }
            ) {}
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(with(LocalDensity.current) { dropDownWidth.toDp() }),
            offset = DpOffset(32.dp, 0.dp)
        ) {
            listOfOptions.forEach { label ->
                DropdownMenuItem(onClick = {
                    userRole.value = label
                    expanded = !expanded
                }) {
                    Text(text = label)
                }
            }
        }
    }
}