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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(viewModel: AuthViewModel, onBack: () -> Unit) {
    var userId by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    
    val userTypes = listOf("Customer", "Sub Admin/Seller", "Delivery")
    var userType by remember { mutableStateOf(userTypes[0]) }
    var expanded by remember { mutableStateOf(false) }

    val message by viewModel.message.collectAsState()
    val isSuccess by viewModel.isRegisterSuccess.collectAsState()

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            userId = ""
            fullName = ""
            newPassword = ""
            gender = "Male"
            userType = userTypes[0]
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
            value = userId,
            onValueChange = { userId = it },
            label = { Text("User ID") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("New Password") },
            visualTransformation = PasswordVisualTransformation(),
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
        Spacer(modifier = Modifier.height(16.dp))

        // User Type Dropdown
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = userType,
                onValueChange = {},
                readOnly = true,
                label = { Text("User Type") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                userTypes.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            userType = type
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.register(
                    RegisterRequest(userId, fullName, newPassword, gender, userType)
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
