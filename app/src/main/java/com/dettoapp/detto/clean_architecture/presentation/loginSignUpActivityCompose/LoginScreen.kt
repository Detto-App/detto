package com.dettoapp.detto.clean_architecture.presentation.loginSignUpActivityCompose


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
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
import com.dettoapp.detto.R

@Composable
fun LoginScreen() {

    var name by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {

    }
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
            value = name,
            onValueChange = { name = it },
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
            value = name,
            onValueChange = { name = it },
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
        CustomDropDownMenu(hint = "Select Role", listOfOptions = arrayListOf("Student", "Teacher"))
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
        )
        Button(modifier = Modifier.align(CenterHorizontally), onClick = { /*TODO*/ }) {
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

}


@Preview
@Composable
fun LoginPreview() {
    LoginScreen()
}

@Composable
fun CustomDropDownMenu(hint: String, listOfOptions: ArrayList<String>) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("") }
    var dropDownWidth by remember { mutableStateOf(0) }
    val focusRequester = remember { FocusRequester() }

    val icon = if (expanded)
        Icons.Filled.ArrowDropUp
    else
        Icons.Filled.ArrowDropDown


    Column {
        Box {
            OutlinedTextField(
                value = selectedText,
                onValueChange = { selectedText = it },
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
                    selectedText = label
                    expanded = !expanded
                }) {
                    Text(text = label)
                }
            }
        }
    }
}