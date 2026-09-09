package com.munna.kmptesting

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onBack: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val message by viewModel.message.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Login", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.login(LoginRequest(username, password))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }
        if (message.isNotEmpty()) {
            Text(text = message, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(8.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = {
            viewModel.clearMessage()
            onNavigateToRegister()
        }) {
            Text("Don't have an account? Register")
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = {
            viewModel.clearMessage()
            onBack()
        }) {
            Text("Back")
        }
    }
}
