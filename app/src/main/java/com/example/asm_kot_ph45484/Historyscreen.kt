package com.example.asm_kot_ph45484

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.asm_kot_ph45484.Model.History.HistoryData
import com.example.asm_kot_ph45484.ViewModel.HistoryViewModel

@Composable
fun HistoryScreen(viewModel: HistoryViewModel, navController: NavHostController) {
    val historyViewModel: HistoryViewModel = viewModel
    val historyItems by historyViewModel.historyItems.observeAsState(initial = emptyList())
    Log.e("historyScreen", "Current history items: $historyItems") // Log để kiểm tra dữ liệu

    Column(modifier = Modifier.fillMaxWidth()) {
        @Composable
        fun HeaderSection() {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.lo_go),
                    contentDescription = null,
                    modifier = Modifier.size(60.dp)
                )
                Text(
                    text = "Cưm Tứm Đim",
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 26.sp,
                    modifier = Modifier.padding(
                        start = 7.dp,
                        top = 14.dp
                    ) // Giảm padding giữa logo và chữ
                )
            }
        }
        HeaderSection()
        Column (modifier = Modifier.padding(16.dp)) {
            Text(text = "Lịch Sử Giao Dịch", style = MaterialTheme.typography.titleLarge)

            LazyColumn(modifier = Modifier.height(560.dp)) {
                items(historyItems) { historyItem ->
                    HistoryItemRow(historyItem) {
                        navController.navigate("orderDetail/${historyItem.id}")  // Điều hướng đến màn hình chi tiết đơn hàng
                    }
                }
            }
        }

    }
}

@Composable
fun HistoryItemRow(historyItem: HistoryData, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick() },  // Thêm onClick để điều hướng
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .padding(start = 20.dp)
        ) {
            Text(
                text = historyItem.paymentMethod,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .width(220.dp),
                fontSize = 20.sp,
            )
            Text(
                text = historyItem.date,
                modifier = Modifier,
                color = Color.Red,
                fontSize = 20.sp
            )

        }
        Column(
            modifier = Modifier
                .padding(start = 20.dp)
        ) {
            Text(
                text = "${historyItem.totalAmount} món",
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .width(220.dp),
                fontSize = 20.sp,
            )
            Text(
                text = "${historyItem.product.quantity} VND",
                modifier = Modifier,
                color = Color.Red,
                fontSize = 20.sp
            )

        }

    }
}



