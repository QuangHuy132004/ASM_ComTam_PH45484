package com.example.asm_kot_ph45484

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.asm_kot_ph45484.Model.History.Order

@Composable
fun OrderDetailScreen(order: Order) {
    val statusColor = if (order.status == "Đã thanh toán") Color.Green else Color.Red

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Chi tiết đơn hàng",
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Thông tin cửa hàng
        Text(text = order.storeName, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text(text = order.storeAddress)
        Text(text = order.storePhone)

        Spacer(modifier = Modifier.height(8.dp))

        // Thông tin hóa đơn
        Text(text = "Hóa Đơn Thanh Toán", fontWeight = FontWeight.Bold)
        Text(text = "Ngày: ${order.date}")
        Text(text = "Trạng thái đơn hàng: ${order.status}", color = statusColor)

        Spacer(modifier = Modifier.height(16.dp))

        // Chi tiết số lượng món ăn
        Text(text = "Số lượng món chính: ${order.mainDishQuantity}")

        // Danh sách tên món và số lượng từng món
        LazyColumn {
            items(order.dishes) { dish ->
                DishItemRow(dish = dish)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Địa chỉ giao hàng
        Text(text = "Số nhà: ${order.houseNumber}")
        Text(text = "Đường: ${order.street}")
        Text(text = "Phường: ${order.ward}")
        Text(text = "Quận: ${order.district}")
        Text(text = "Thành phố: ${order.city}")

        Spacer(modifier = Modifier.height(16.dp))

        // Tổng tiền
        Text(text = "Tổng tiền", fontWeight = FontWeight.Bold, fontSize = 24.sp)
    }
}

@Composable
fun DishItemRow(dish: CartData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = dish.product.name,
            modifier = Modifier.weight(1f),
            fontSize = 18.sp
        )
        Text(
            text = "${dish.quantity}",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}
