package com.example.asm_kot_ph45484

import CartViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.asm_kot_ph45484.ViewModel.HistoryViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CheckoutScreen(
    navController: NavHostController,
    historyViewModel: HistoryViewModel,
    cartViewModel: CartViewModel,
) {
    val context = LocalContext.current
    val cartItems by cartViewModel.cartItems.observeAsState(initial = emptyList())
    val totalItems = getTotalItems(cartItems)
    val totalPrice = getTotalPrice(cartItems)

    // Tạo ngày hiện tại
    val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Thanh Toán",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            fontSize = 28.sp, // Increased font size
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.CenterHorizontally)
        )
        // Thông tin địa chỉ nhận hàng
        Text(text = "Địa chỉ nhận hàng", fontWeight = FontWeight.Bold)
        Text(text = "Thành | 0967143333")
        Text(text = "33 Trịnh Văn Bô, Phường Phương Canh, Quận Từ Liêm, Thành Phố Hà Nội")
        Spacer(modifier = Modifier.height(16.dp))

        // Thông tin lựa chọn phương thức thanh toán
        PaymentMethodSelector(
            historyViewModel,
            navController,
            totalPrice,
            totalItems,
            cartItems,
            currentDate,
            cartViewModel
        )

    }
}

@Composable
fun PaymentMethodSelector(
    historyViewModel: HistoryViewModel,
    navController: NavHostController,
    totalPrice: Double,
    totalItems: Int,
    cartItems: List<CartData>,
    currentDate: String,
    cartViewModel: CartViewModel
) {
    val selectedPaymentMethod = remember { mutableStateOf("paypal") } // Giá trị mặc định

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Vui lòng chọn một trong những phương thức sau:", fontWeight = FontWeight.Bold)

        // Payment methods with images
        PaymentMethodRow("PayPal", R.drawable.paypal, selectedPaymentMethod, "Paypal")
        PaymentMethodRow("Visa", R.drawable.visa, selectedPaymentMethod, "Visa")
        PaymentMethodRow("Momo", R.drawable.momo, selectedPaymentMethod, "Momo")
        PaymentMethodRow("Zalo Pay", R.drawable.zalopay, selectedPaymentMethod, "Zalopay")
        PaymentMethodRow("Thanh toán trực tiếp", R.drawable.cash, selectedPaymentMethod, "Tiền Mặt")

        Button(
            onClick = {
                // Thêm lịch sử đơn hàng vào ViewModel
                cartItems.forEach { cartItem ->
                    historyViewModel.addHistory(
                        id = System.currentTimeMillis().toString(), // Tạo ID duy nhất
                        paymentMethod = selectedPaymentMethod.value,
                        products = cartItems, // Truyền danh sách sản phẩm vào
                        totalAmount = totalPrice,
                        date = currentDate
                    )
                }
                cartViewModel.clearCart()
                // Điều hướng ra khỏi màn hình sau khi hoàn thành
                navController.popBackStack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp), // Added more padding for better spacing
            shape = RoundedCornerShape(12.dp), // Rounded button
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF008CFF))
        ) {
            Text(
                text = "Xong",
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )        }
    }
}
@Composable
fun PaymentMethodRow(
    methodName: String,
    iconRes: Int,
    selectedPaymentMethod: androidx.compose.runtime.MutableState<String>,
    methodValue: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp) // Padding between payment options
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = methodName,
            modifier = Modifier.size(40.dp) // Set icon size
        )
        Spacer(modifier = Modifier.width(16.dp)) // Space between icon and text
        RadioButton(
            selected = selectedPaymentMethod.value == methodValue,
            onClick = { selectedPaymentMethod.value = methodValue }
        )
        Text(text = methodName, fontSize = 18.sp) // Larger text size for clarity
    }
}

