package com.example.asm_kot_ph45484

import CartViewModel
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter

@JvmOverloads
@Composable
fun CartScreen(viewModel: CartViewModel, navController: NavHostController) {
    val cartViewModel: CartViewModel = viewModel
    val cartItems by cartViewModel.cartItems.observeAsState(initial = emptyList())
    val totalItems = getTotalItems(cartItems)
    val totalPrice = getTotalPrice(cartItems)

    Log.e("CartScreen", "Current cart items: $cartItems") // Log để kiểm tra dữ liệu

    Column(modifier = Modifier.fillMaxSize()) {
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

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Giỏ hàng",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, fontSize = 27.sp),
            modifier = Modifier.padding(bottom = 16.dp).padding(10.dp)
        )
        if (cartItems.isNotEmpty()) {
            LazyColumn(modifier = Modifier.height(310.dp)) {
                items(cartItems) { cartItem ->
                    CartItemRow(cartItem) { newQuantity ->
                        viewModel.updateQuantity(
                            cartItem.product.id.toString(),
                            newQuantity
                        )
                        Log.e("sss", cartItem.product.name)
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f)) // Đẩy Row xuống cuối
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Số lượng món: ",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "$totalItems",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                }
                Text(
                    text = "Tổng tiền: $totalPrice VND",
                    color = Color(0xFFFFA500),
                    modifier = Modifier.padding(top = 8.dp),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            viewModel.clearCart()
                            navController.navigate("home")
                        },
                        modifier = Modifier.weight(1f).padding(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.LightGray
                        )
                    ) {
                        Text("Huỷ", fontSize = 18.sp, color = Color.Black)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = { navController.navigate("checkout") },
                        modifier = Modifier.weight(1f).padding(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFA500)
                        )
                    ) {
                        Text("Thanh Toán", fontSize = 18.sp, color = Color.White)
                    }
                }
            }
        } else {
            Text(text = "Giỏ hàng trống", modifier = Modifier.padding(16.dp))
        }
    }

}

@Composable
fun CartItemRow(cartItem: CartData, onQuantityChange: (Int) -> Unit) {
    val totalPrice = cartItem.product.price * cartItem.quantity

    // Adding a border around each item row
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(10.dp))
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberImagePainter(data = cartItem.product.thumbnail),
                contentDescription = "Selected Image",
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .size(50.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    text = cartItem.product.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "$totalPrice VND",
                    color = Color.Red,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            QuantitySelector(quantity = cartItem.quantity, onQuantityChange = onQuantityChange)
        }
    }
}

fun getTotalItems(cartItems: List<CartData>): Int {
    return cartItems.sumOf { it.quantity }
}

fun getTotalPrice(cartItems: List<CartData>): Double {
    return cartItems.sumOf { it.product.price * it.quantity }
}


