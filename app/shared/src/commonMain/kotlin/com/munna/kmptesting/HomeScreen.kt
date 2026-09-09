package com.munna.kmptesting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: AuthViewModel, onLogout: () -> Unit) {
    var selectedTab by remember { mutableStateOf(0) }
    val userName by viewModel.userName.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout") },
            text = { Text("Are sure to logout") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Continue")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = userName, fontWeight = FontWeight.Bold) }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Orders") },
                    label = { Text("Orders") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.CheckCircle, contentDescription = "Delivered") },
                    label = { Text("Delivered") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") },
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 }
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (selectedTab) {
                0 -> OrdersTab()
                1 -> DeliveredTab()
                2 -> ProfileTab()
                3 -> SettingsTab { showLogoutDialog = true }
            }
        }
    }
}

@Composable
fun OrdersTab() {
    val items = listOf("Milk" to "1L", "Bread" to "1 pkt", "Eggs" to "12 pcs", "Apples" to "1kg", "Rice" to "5kg")
    var selectedItems by remember { mutableStateOf(setOf<String>()) }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Text("Orders", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
        }
        items(items) { (name, qty) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        selectedItems = if (selectedItems.contains(name)) {
                            selectedItems - name
                        } else {
                            selectedItems + name
                        }
                    }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = selectedItems.contains(name),
                    onCheckedChange = {
                        selectedItems = if (it) selectedItems + name else selectedItems - name
                    }
                )
                Column {
                    Text(text = name, style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Qty: $qty", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
fun DeliveredTab() {
    val deliveredOrders = listOf(
        DeliveredOrder("John Doe", "500", "123 Street, NY", listOf("Milk" to "2L", "Eggs" to "6 pcs")),
        DeliveredOrder("Jane Smith", "1200", "456 Avenue, CA", listOf("Bread" to "2 pkt", "Rice" to "10kg", "Butter" to "500g")),
        DeliveredOrder("Bob Wilson", "300", "789 Road, TX", listOf("Apple" to "1kg"))
    )

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Text("Delivered", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
        }
        items(deliveredOrders) { order ->
            var expanded by remember { mutableStateOf(false) }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { expanded = !expanded },
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Customer: ${order.name}", fontWeight = FontWeight.Bold)
                    Text(text = "Total Paid: ₹${order.amount}")
                    Text(text = "Address: ${order.address}")
                    
                    if (expanded) {
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Purchased Items:", style = MaterialTheme.typography.bodySmall)
                        order.items.forEach { (item, qty) ->
                            Text(text = "• $item ($qty)", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}

data class DeliveredOrder(val name: String, val amount: String, val address: String, val items: List<Pair<String, String>>)

@Composable
fun ProfileTab() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
                .border(1.dp, Color.Gray, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = Color.Gray
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Munna's Grossary Shop", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(text = "Owner: Munna Kumar", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Address: Sector 62, Noida, UP", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
    }
}

@Composable
fun SettingsTab(onLogout: () -> Unit) {
    val settings = listOf(
        "Language" to Icons.Default.Info,
        "Help & Support" to Icons.Default.Call,
        "Logout" to Icons.Default.ExitToApp,
        "Delete Account" to Icons.Default.Delete
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        
        settings.forEach { (title, icon) ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { if (title == "Logout") onLogout() },
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFF0F0F0) // Light Gray
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(icon, contentDescription = null, tint = if (title == "Delete Account") Color.Red else Color.Black)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = title, color = if (title == "Delete Account") Color.Red else Color.Black)
                }
            }
        }
    }
}
