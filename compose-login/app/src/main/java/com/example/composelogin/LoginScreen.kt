package com.example.composelogin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** The demo account. A real app checks a database or a server instead; see the SQLite and Firebase tutorials. */
private const val DEMO_USERNAME = "user"
private const val DEMO_PASSWORD = "123456"

/** What's wrong with the form, if anything. Kept apart from the UI so the rules are easy to read. */
fun loginError(username: String, password: String): String? = when {
    username.isBlank() -> "Enter your username"
    password.isEmpty() -> "Enter your password"
    username.trim() != DEMO_USERNAME || password != DEMO_PASSWORD -> "Wrong username or password"
    else -> null
}

@Composable
fun LoginScreen(onLoggedIn: (String) -> Unit) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var showPassword by rememberSaveable { mutableStateOf(false) }
    // The error appears only after Log in is pressed, not while the user is still typing.
    var error by rememberSaveable { mutableStateOf<String?>(null) }

    fun logIn() {
        error = loginError(username, password)
        if (error == null) onLoggedIn(username.trim())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("Welcome back", fontSize = 34.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 48.dp))
        Text("Log in to continue. Demo account: user / 123456", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

        OutlinedTextField(
            value = username,
            onValueChange = { username = it; error = null },
            label = { Text("Username") },
            leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) },
            singleLine = true,
            isError = error != null,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth(),
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it; error = null },
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null) },
            // The eye icon switches between dots and plain text.
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        if (showPassword) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                        contentDescription = if (showPassword) "Hide password" else "Show password",
                    )
                }
            },
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            isError = error != null,
            supportingText = { error?.let { Text(it) } },
            // The keyboard's Done key logs in too.
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { logIn() }),
            modifier = Modifier.fillMaxWidth(),
        )

        Button(
            onClick = { logIn() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
        ) {
            Text("Log in", fontSize = 18.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    ComposeLoginTheme { LoginScreen(onLoggedIn = {}) }
}
