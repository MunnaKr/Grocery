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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
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
fun HomeScreen(
    viewModel: AuthViewModel,
    onLogout: () -> Unit,
    onNavigateToOrderDetails: () -> Unit
) {
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
                title = { Text(text = userName.ifEmpty { "Grocery App" }, fontWeight = FontWeight.Bold) }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Store, contentDescription = "Groceries") },
                    label = { Text("Groceries") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Orders") },
                    label = { Text("Orders") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.CheckCircle, contentDescription = "Delivered") },
                    label = { Text("Delivered") },
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") },
                    selected = selectedTab == 4,
                    onClick = { selectedTab = 4 }
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (selectedTab) {
                0 -> GroceriesTab(viewModel)
                1 -> OrdersTab(onNavigateToOrderDetails)
                2 -> DeliveredTab()
                3 -> ProfileTab()
                4 -> SettingsTab { showLogoutDialog = true }
            }
        }
    }
}

@Composable
fun GroceriesTab(viewModel: AuthViewModel) {
    val groceries by viewModel.groceries.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var showUploadDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchGroceries()
    }

    if (showUploadDialog) {
        AlertDialog(
            onDismissRequest = { showUploadDialog = false },
            title = { Text("Upload XLS File") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Upload XLS file containing grocery items with details: name, icon, and type.")
                    Spacer(modifier = Modifier.height(4.dp))
                    Button(
                        onClick = {
                            val parsedItems = listOf(
                                GroceryItemDto("Fresh Mangoes", "mango_icon", "Fruits"),
                                GroceryItemDto("Greek Yogurt", "yogurt_icon", "Dairy"),
                                GroceryItemDto("Whole Wheat Flour", "flour_icon", "Staples"),
                                GroceryItemDto("Cheddar Cheese", "cheese_icon", "Dairy")
                            )
                            viewModel.uploadGroceries(parsedItems)
                            showUploadDialog = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Upload 'groceries.xls'")
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showUploadDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    val filteredGroceries = groceries.filter { 
        it.name.contains(searchQuery, ignoreCase = true) || it.type.contains(searchQuery, ignoreCase = true) 
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { showUploadDialog = true },
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Upload XLS", modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Upload XLS")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text("Groceries", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search groceries...") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(filteredGroceries) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = item.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Type: ${item.type}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Icon: ${item.icon}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrdersTab(onItemClick: () -> Unit) {
    var selectedTopTab by remember { mutableStateOf(0) }
    val topTabs = listOf("Packaging", "New Order", "Delivered")

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Orders", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        ScrollableTabRow(
            selectedTabIndex = selectedTopTab,
            edgePadding = 0.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            topTabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTopTab == index,
                    onClick = { selectedTopTab = index },
                    text = { Text(title, fontWeight = FontWeight.SemiBold) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedTopTab) {
            0 -> PackagingContent(onItemClick)
            1 -> NewOrderContent(onItemClick)
            2 -> DeliveredOrderContent(onItemClick)
        }
    }
}

data class PackagingOrder(
    val customerName: String,
    val address: String,
    val itemQuantity: String,
    val totalPrice: String,
    val status: String = "Packaging in progress"
)

@Composable
fun PackagingContent(onItemClick: () -> Unit) {
    val packagingOrders = listOf(
        PackagingOrder("Rahul Sharma", "Sector 62, Noida, UP", "Milk (2L), Bread (1 pkt)", "₹140"),
        PackagingOrder("Priya Singh", "Indirapuram, Ghaziabad", "Eggs (12 pcs), Apples (1kg)", "₹210"),
        PackagingOrder("Amit Kumar", "Sector 18, Noida, UP", "Basmati Rice (5kg)", "₹600")
    )

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(packagingOrders) { order ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { onItemClick() },
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Customer: ${order.customerName}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Address: ${order.address}", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Item Quantity: ${order.itemQuantity}", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Total Price: ${order.totalPrice}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "Status: ${order.status}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }
        }
    }
}

data class NewOrder(
    val orderId: String,
    val customerName: String,
    val address: String,
    val distance: String,
    val itemsWithQty: List<Pair<String, String>>,
    val totalQuantity: String
)

@Composable
fun NewOrderContent(onItemClick: () -> Unit) {
    val newOrders = listOf(
        NewOrder(
            orderId = "Order #101",
            customerName = "Rahul Sharma",
            address = "Sector 62, Noida, UP",
            distance = "2.4 km",
            itemsWithQty = listOf("Apples" to "1kg", "Rice" to "5kg"),
            totalQuantity = "6 kg"
        ),
        NewOrder(
            orderId = "Order #102",
            customerName = "Priya Singh",
            address = "Indirapuram, Ghaziabad",
            distance = "4.1 km",
            itemsWithQty = listOf("Tomatoes" to "2kg", "Onions" to "1kg"),
            totalQuantity = "3 kg"
        ),
        NewOrder(
            orderId = "Order #103",
            customerName = "Amit Kumar",
            address = "Sector 18, Noida, UP",
            distance = "1.8 km",
            itemsWithQty = listOf("Milk" to "2L", "Bread" to "1 pkt", "Eggs" to "12 pcs"),
            totalQuantity = "15 items"
        )
    )

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(newOrders) { order ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { onItemClick() },
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = order.orderId, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Text(text = order.distance, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "Name: ${order.customerName}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = "Address: ${order.address}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                    Spacer(modifier = Modifier.height(6.dp))
                    
                    Text(text = "Items:", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                    order.itemsWithQty.forEach { (item, qty) ->
                        Text(text = "• $item ($qty)", style = MaterialTheme.typography.bodyMedium)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Total Quantity: ${order.totalQuantity}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { /* Accept order */ },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Accept")
                        }
                        OutlinedButton(
                            onClick = { /* Cancel order */ },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                        ) {
                            Text("Cancel")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DeliveredOrderContent(onItemClick: () -> Unit) {
    val delivered = listOf("Order #99" to "Butter (500g), Bananas (1 doz)", "Order #98" to "Milk (2L)")
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(delivered) { (orderId, details) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { onItemClick() },
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = orderId, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Items: $details")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Status: Successfully Delivered", color = Color.Green, fontWeight = FontWeight.SemiBold)
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
        Spacer(modifier = Modifier.height(12.dp))
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
