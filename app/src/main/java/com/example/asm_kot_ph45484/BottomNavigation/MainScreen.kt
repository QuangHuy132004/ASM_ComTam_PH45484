package com.example.asm_kot_ph45484

import CartViewModel
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.asm_kot_ph45484.ViewModel.HistoryViewModel


sealed class Screen(val route: String, val label: String, val icon: @Composable () -> Unit) {
    object Home : Screen("home", "Trang chủ", { Icon(Icons.Filled.Home, contentDescription = "Trang chủ") })
    object History : Screen("history", "Lịch sử", { Icon(Icons.Filled.DateRange, contentDescription = "Lịch sử") })
    object Cart : Screen("cart", "Giỏ hàng", { Icon(Icons.Filled.ShoppingCart, contentDescription = "Giỏ hàng") })
    object Profile : Screen("profile", "Hồ sơ", { Icon(Icons.Filled.Person, contentDescription = "Hồ sơ") })
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    // Tạo NavController để quản lý điều hướng
    val navController = rememberNavController()

    // Sử dụng Scaffold để cấu hình Bottom Navigation
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) } // Thanh Bottom Navigation
    ) { innerPadding ->
        // Phần thân của nội dung, nơi điều hướng giữa các màn hình khác nhau sẽ xảy ra
        NavigationHost(navController = navController, innerPadding) // Truyền innerPadding
    }
}
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    // Danh sách các màn hình cho Navigation Bar
    val items = listOf(
        Screen.Home,
        Screen.History,
        Screen.Cart,
        Screen.Profile
    )

    // Lấy màn hình hiện tại từ NavController
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

    // Sử dụng NavigationBar thay vì BottomNavigation trong Material 3
    NavigationBar {
        items.forEach { screen ->
            // Sử dụng NavigationBarItem thay vì BottomNavigationItem
            NavigationBarItem(
                label = { Text(screen.label) },  // Tên hiển thị cho từng mục
                selected = currentDestination == screen.route,  // Kiểm tra tab nào đang được chọn
                onClick = {
                    // Điều hướng đến màn hình tương ứng với launchSingleTop
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = screen.icon // Sử dụng icon từ đối tượng Screen
            )
        }
    }
}

@Composable
fun NavigationHost(navController: NavHostController, innerPadding: PaddingValues) {
    val cartViewModel: CartViewModel = viewModel(LocalContext.current as ComponentActivity)
    val historyViewModel: HistoryViewModel = viewModel(LocalContext.current as ComponentActivity)

    NavHost(navController, startDestination = Screen.Home.route, modifier = Modifier.padding(innerPadding)) {
        composable(Screen.Home.route) { HomeScreen(navController = navController, viewModel = cartViewModel) }
        composable(Screen.History.route) { HistoryScreen(viewModel = historyViewModel,navController) }
        composable(Screen.Cart.route) { CartScreen(viewModel = cartViewModel, navController = navController) }
        composable(Screen.Profile.route) { ProfileScreen() }
        composable("checkout") {
            CheckoutScreen(navController = navController, cartViewModel = cartViewModel, historyViewModel = historyViewModel)
        }

    }
}
