package com.munna.kmptesting

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun RegisterScreen(viewModel: AuthViewModel, onBack: () -> Unit) {
    var fullName by remember { mutableStateOf("") }
    var employeeId by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var address by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val message by viewModel.message.collectAsState()
    val isSuccess by viewModel.isRegisterSuccess.collectAsState()

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            fullName = ""
            employeeId = ""
            email = ""
            gender = "Male"
            address = ""
            password = ""
            viewModel.resetRegisterSuccess()
        }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "Register", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = employeeId,
            onValueChange = { employeeId = it },
            label = { Text("Employee ID") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Official Mail ID") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Gender",
            modifier = Modifier.align(Alignment.Start),
            style = MaterialTheme.typography.bodyLarge
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = gender == "Male",
                onClick = { gender = "Male" }
            )
            Text("Male")
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = gender == "Female",
                onClick = { gender = "Female" }
            )
            Text("Female")
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = gender == "Other",
                onClick = { gender = "Other" }
            )
            Text("Other")
        }
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Address") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.register(
                    RegisterRequest(fullName, employeeId, email, gender, address, password)
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }
        if (message.isNotEmpty()) {
            SelectionContainer {
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = {
            viewModel.clearMessage()
            onBack()
        }) {
            Text("Back to Login")
        }
    }
}
